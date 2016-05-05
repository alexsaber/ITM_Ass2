package itm.video;

/*******************************************************************************
 This file is part of the ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/

import itm.util.ImageCompare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

/**
 * This class reads video files, extracts metadata for both the audio and the
 * video track, and writes these metadata to a file.
 * 
 * It can be called with 3 parameters, an input filename/directory, an output
 * directory and an "overwrite" flag. It will read the input video file(s),
 * retrieve the metadata and write it to a text file in the output directory.
 * The overwrite flag indicates whether the resulting output file should be
 * overwritten or not.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */
public class VideoThumbnailGenerator {

	/**
	 * Constructor.
	 */
	public VideoThumbnailGenerator() {
	}

	/**
	 * Processes a video file directory in a batch process.
	 * 
	 * @param input
	 *            a reference to the video file directory
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing output files should be overwritten
	 *            or not
	 * @return a list of the created media objects (videos)
	 */
	public ArrayList<File> batchProcessVideoFiles(File input, File output, boolean overwrite, int timespan) throws IOException {
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

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4"))
					try {
						File result = processVideo(f, output, overwrite, timespan);
						System.out.println("processed file " + f + " to " + output);
						ret.add(result);
					} catch (Exception e0) {
						System.err.println("Error processing file " + input + " : " + e0.toString());
					}
			}
		} else {

			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4"))
				try {
					File result = processVideo(input, output, overwrite, timespan);
					System.out.println("processed " + input + " to " + result);
					ret.add(result);
				} catch (Exception e0) {
					System.err.println("Error when creating processing file " + input + " : " + e0.toString());
				}

		}
		return ret;
	}

	/**
	 * Processes the passed input video file and stores a thumbnail of it to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input video file
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing files should be overwritten or not
	 * @return the created video media object
	 */
	protected File processVideo(File input, File output, boolean overwrite, int timespan) throws Exception {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		// create output file and check whether it already exists.
		File outputFile = new File(output, input.getName() + "_thumb.avi");

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// extract frames from input video
		
		
		//**********************************************
		//source: https://gist.github.com/tuler/5713172
		//***********************************************
        IContainer container = IContainer.make(); 
        container.open(input.getAbsolutePath(), IContainer.Type.READ, null);
        //System.out.println("container.getDuration(): " + container.getDuration());
        long durationInMs = container.getDuration();
		if (durationInMs == Global.NO_PTS) {
			throw new RuntimeException("Duration of container is unknown");
		}
        long durationInS = durationInMs/1000000;
        
        int numOfStreams = container.getNumStreams(); 
        // and iterate through the streams to find the first video stream 
        int theVidID = -1;
        boolean vidIDFound = false;
        IStreamCoder theVidCoder = null; 
        for (int i = 0; i < numOfStreams; i++) { 
                IStreamCoder coder = container.getStream(i).getStreamCoder(); 

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

        IVideoResampler resampler = null; 
		// if this stream is not in BGR24, we're going to need to
		// convert it.  The VideoResampler does that for us.
        if (theVidCoder.getPixelType() != IPixelFormat.Type.BGR24) { 
        	resampler = IVideoResampler.make(theVidCoder.getWidth(), 
						            		 theVidCoder.getHeight(), 
						            		 IPixelFormat.Type.BGR24, 
						            		 theVidCoder.getWidth(), 
						            		 theVidCoder.getHeight(), 
						            		 theVidCoder.getPixelType()); 
            if (resampler == null) 
            	throw new RuntimeException("No color space!"); 
        } 


        
        IRational timeBase = container.getStream(theVidID).getTimeBase(); 

        //System.out.println("Timebase " + timeBase.toString()); 
        
        ArrayList<IVideoPicture> capturedFrames = new ArrayList<IVideoPicture>();
        
        for (int timeToCapture = 0; timeToCapture < durationInS; timeToCapture = timeToCapture + timespan){
        	
	        long time_stampToCapture = (timeBase.getDenominator() / timeBase.getNumerator()) * timeToCapture; 
	        
	       System.out.println("time_stampToCapture " + time_stampToCapture); 
	        //long target = container.getStartTime() + timeStampOffset; 
	
	        container.seekKeyFrame(theVidID, time_stampToCapture, 0); 
	        
	        boolean isFinished = false; 
	        
	        IPacket packet = IPacket.make(); 
	        while(container.readNextPacket(packet) >= 0 && !isFinished ) { 
	        	System.out.println("inside while");
	
	                if (packet.getStreamIndex() == theVidID) { 
	                	
	            		IVideoPicture picture = IVideoPicture.make(theVidCoder.getPixelType(), 
	                    											theVidCoder.getWidth(),
	                    											theVidCoder.getHeight()); 
	                    int offset = 0; 
	                    while (offset < packet.getSize()) { 
	                    	System.out.println("inside second while");
	
	                            int bytesDecoded = theVidCoder.decodeVideo(picture, packet, offset); 
	                            if (bytesDecoded < 0) { 
	                                    System.err.println("No video was decoded in the packet!"); 
	                            } 
	                            offset += bytesDecoded; 
	
	                            if (picture.isComplete()) { 
	
	                                    IVideoPicture newPic = picture; 
	
	                                    if (resampler != null) { 
	                                        newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), 
	                                        							picture.getWidth(),
	                                        							picture.getHeight()); 
	                                        if (resampler.resample(newPic, picture) < 0) 
	                                                throw new RuntimeException("Could not resample video!"); 
	                                    } 
	
	                                    if (newPic.getPixelType() != IPixelFormat.Type.BGR24) 
	                                            throw new RuntimeException("Could not decode video as BGR 24 bit data!"); 
	
	                                    BufferedImage screenshot = Utils.videoPictureToImage(newPic); 
	
	                                    File file = new File(outputFile.getAbsolutePath() + "-" + timeToCapture + ".jpg");
	                                    
	                                    ImageIO.write(screenshot, "jpg", file);
	                                    isFinished = true; 
	                            } 
	                        } 
	                } 
	        } 
	        
        }

    	theVidCoder.close(); 
    	container.close(); 
		
		
		
		System.out.println("for " + input.getName() + " found " + capturedFrames.size() + " frames");
		
		
		// add a watermark of your choice and paste it to the image
        // e.g. text or a graphic

		// create a video writer

		// add a stream with the proper width, height and frame rate
		
		// if timespan is set to zero, compare the frames to use and add 
		// only frames with significant changes to the final video

		// loop: get the frame image, encode the image to the video stream
		
		// Close the writer

		return outputFile;
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {
		

		if (args.length < 3) {
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-video> <output-directory> <timespan>");
            System.out.println("usage: java itm.video.VideoThumbnailGenerator <input-directory> <output-directory> <timespan>");
            System.exit(1);
        }
        File fi = new File(args[0]);
        File fo = new File(args[1]);
        int timespan = 5;
        if(args.length == 3)
            timespan = Integer.parseInt(args[2]);
        
        VideoThumbnailGenerator videoMd = new VideoThumbnailGenerator();
        videoMd.batchProcessVideoFiles(fi, fo, true, timespan);
	}
}