package itm.video;

import java.awt.image.BufferedImage;

/*******************************************************************************
 This file is part of the ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

/**
 * 
 * This class creates JPEG thumbnails from from video frames grabbed from the
 * middle of a video stream It can be called with 2 parameters, an input
 * filename/directory and an output directory.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */

public class VideoFrameGrabber {

	/**
	 * Constructor.
	 */
	public VideoFrameGrabber() {
	}

	/**
	 * Processes the passed input video file / video file directory and stores
	 * the processed files in the output directory.
	 * 
	 * @param input
	 *            a reference to the input video file / input directory
	 * @param output
	 *            a reference to the output directory
	 */
	public ArrayList<File> batchProcessVideoFiles(File input, File output) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<File> ret = new ArrayList<File>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {
				if (f.isDirectory())
					continue;

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4")) {
					File result = processVideo(f, output);
					System.out.println("converted " + f + " to " + result);
					ret.add(result);
				}

			}

		} else {
			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4")) {
				File result = processVideo(input, output);
				System.out.println("converted " + input + " to " + result);
				ret.add(result);
			}
		}
		return ret;
	}

	/**
	 * Processes the passed audio file and stores the processed file to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input audio File
	 * @param output
	 *            a reference to the output directory
	 */
	protected File processVideo(File input, File output) throws IOException, IllegalArgumentException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		File outputFile = new File(output, input.getName() + "_thumb.jpg");
		// load the input video file

		//**********************************************
		//source: https://gist.github.com/tuler/5713172
		//***********************************************
        IContainer container = IContainer.make(); 
        container.open(input.getAbsolutePath(), IContainer.Type.READ, null);
        int numOfStreams = container.getNumStreams(); 
        //to store id of the video stream later
        int theVidID = -1;
        //to detect when we found our video stream
        boolean vidIDFound = false;
        //when video stream will be found, set the coder
        IStreamCoder theVidCoder = null; 
        //go through all streams (audio also)
        for (int i = 0; i < numOfStreams; i++) { 
            IStreamCoder coder = container.getStream(i).getStreamCoder(); 
            //check if it is a video stream
            if (coder.getCodecType() == com.xuggle.xuggler.ICodec.Type.CODEC_TYPE_VIDEO) { 
            	theVidID = i; 
            	theVidCoder = coder; 
            	vidIDFound = true;
                break; 
            } 
        } 

		if (vidIDFound == false)
		   throw new RuntimeException( "No video stream in the file!");

        if (theVidCoder.open() < 0) 
                throw new RuntimeException("Could not open video decoder!"); 

        long durationInMs = container.getDuration();
		if (durationInMs == Global.NO_PTS) {
			throw new RuntimeException("Duration of container is unknown");
		}
        //the middle of the stream
        long milisecondToCapture = durationInMs/2;
        
        IPacket packet = IPacket.make();
        //read every packet until the needed one will be found
        EXTRACTINGFRAMES: while (container.readNextPacket(packet) >= 0) {
			//if it is out stream
			if (packet.getStreamIndex() == theVidID) {
				IVideoPicture frame = IVideoPicture.make(
										theVidCoder.getPixelType(), 
										theVidCoder.getWidth(),
										theVidCoder.getHeight()
										);
				int offset = 0;
				while (offset < packet.getSize()) {
					//grab number of decoded bytes
					int bytesDecoded = theVidCoder.decodeVideo(frame, packet, offset);
					if (bytesDecoded < 0)
						throw new RuntimeException("Error! Cannot decode video...");
					//keep track of decoded bytes
					offset += bytesDecoded;
					if (frame.isComplete()) {
						long timestamp = frame.getTimeStamp();
						if (timestamp >= milisecondToCapture) {
							BufferedImage img = Utils.videoPictureToImage(frame);
							ImageIO.write(img, "jpg", outputFile);
							//since only one picture needs to be stored, break out of the outer while
							break EXTRACTINGFRAMES;
						}
					}
				}
			}
		}
		if (theVidCoder != null) {
			theVidCoder.close();
			theVidCoder = null;
		}
		if (container != null) {
			container.close();
			container = null;
		}

		return outputFile;

	}
	

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		// args = new String[] { "./media/video", "./test" };

		if (args.length < 2) {
			System.out.println("usage: java itm.video.VideoFrameGrabber <input-videoFile> <output-directory>");
			System.out.println("usage: java itm.video.VideoFrameGrabber <input-directory> <output-directory>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		File fo = new File(args[1]);
		VideoFrameGrabber grabber = new VideoFrameGrabber();
		grabber.batchProcessVideoFiles(fi, fo);
	}

}
