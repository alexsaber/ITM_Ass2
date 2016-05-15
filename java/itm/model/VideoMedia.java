package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.ICodec.Type;
import com.xuggle.xuggler.IRational;

public class VideoMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* video format metadata */

	Type videoCodec;
	ID videoCodecID;
	IRational videoFrameRate;
	Long videoLength;
	Integer videoHeight;
	Integer videoWidth;
	
	/* audio format metadata */

	Type audioCodec;
	ID audioCodecID;
	Integer audioChannels;
	Integer audioSampleRate; //[Hz]
	Integer audioBitRate; // [kb/s] 
	/**
	 * Constructor.
	 */
	public VideoMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public VideoMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */

	public Type getVideoCodec() {
		return videoCodec;
	}

	public void setVideoCodec(Type videoCodec) {
		this.videoCodec = videoCodec;
	}

	public ID getVideoCodecID() {
		return videoCodecID;
	}

	public void setVideoCodecID(ID videoCodecID) {
		this.videoCodecID = videoCodecID;
	}

	public IRational getVideoFrameRate() {
		return videoFrameRate;
	}

	public void setVideoFrameRate(IRational videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	public Long getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(long videoLength) {
		this.videoLength = videoLength;
	}

	public Integer getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}

	public Integer getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	public Type getAudioCodec() {
		return audioCodec;
	}

	public void setAudioCodec(Type audioCodec) {
		this.audioCodec = audioCodec;
	}

	public ID getAudioCodecID() {
		return audioCodecID;
	}

	public void setAudioCodecID(ID audioCodecID) {
		this.audioCodecID = audioCodecID;
	}

	public Integer getAudioChannels() {
		return audioChannels;
	}

	public void setAudioChannels(int audioChannels) {
		this.audioChannels = audioChannels;
	}

	public Integer getAudioSampleRate() {
		return audioSampleRate;
	}

	public void setAudioSampleRate(int audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}

	public Integer getAudioBitRate() {
		return audioBitRate;
	}

	public void setAudioBitRate(int audioBitRate) {
		this.audioBitRate = audioBitRate;
	}
	
	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* (de-)serialization */

	/**
	 * Serializes this object to the passed file.
	 * 
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("type: video");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		/* video fields */
		out.println("Videofields:");
		if(getVideoCodec()!=null){
			out.println("Codec: " + getVideoCodec());
		}
		if(getVideoCodecID()!=null){
			out.println("CodecID: " + getVideoCodecID());
		}
		if(getVideoFrameRate()!=null){
			out.println("Framerate: " + getVideoFrameRate());
		}
		if(getVideoLength()!=null){
			out.println("Length: " + getVideoLength());
		}
		if(getVideoHeight()!=null){
			out.println("Height: " + getVideoHeight());
		}
		if(getVideoWidth()!=null){
			out.println("Width: " + getVideoWidth() + "\n");
		}
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************
		out.println("Audiofields:");
		if(getAudioCodec()!=null){
			out.println("Codec: " + getAudioCodec());
		}
		if(getAudioCodecID()!=null){
			out.println("CodecID: " + getAudioCodecID());
		}
		if(getAudioChannels()!=null){
			out.println("Channels: " + getAudioChannels());
		}
		if(getAudioSampleRate()!=null){
			out.println("Samplerate: " + getAudioSampleRate());
		}
		if(getAudioBitRate()!=null){
			out.println("Bitrate: " + getAudioBitRate());
		}

		return data.getBuffer();
	}

	/**
	 * Deserializes this object from the passed string buffer.
	 */
	@Override
	public void deserializeObject(String data) throws IOException {
		super.deserializeObject(data);

		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		boolean vidfields = true;
		while ((line = br.readLine()) != null) {

			/* video fields */
			if(vidfields){
				if(line.startsWith("Codec:")){
					setVideoCodec(ICodec.Type.valueOf(line.substring("Codec: ".length())));
				}
				else if(line.startsWith("CodecID")){
					setVideoCodecID(ICodec.ID.valueOf(line.substring("CodecID: ".length())));
				}
				else if(line.startsWith("Framerate")){
					setVideoFrameRate(IRational.make(Integer.parseInt(line.substring("Framerate: ".length(), line.indexOf("/"))),Integer.parseInt(line.substring(line.indexOf("/")))));
				}
				else if(line.startsWith("Length")){
					setVideoLength(Integer.parseInt(line.substring("Length: ".length())));
				}
				else if(line.startsWith("Height")){
					setVideoHeight(Integer.parseInt(line.substring("Height: ".length())));
				}
				else if(line.startsWith("Width")){
					setVideoWidth(Integer.parseInt(line.substring("Width: ".length())));
				}
				else if(line.startsWith("CodAudiofieldsec")){
					vidfields = false;
				}
			}
			else{
				if(line.startsWith("Codec:")){
					setAudioCodec(ICodec.Type.valueOf(line.substring("Codec: ".length())));
				}
				else if(line.startsWith("CodecID")){
					setAudioCodecID(ICodec.ID.valueOf(line.substring("CodecID: ".length())));
				}
				else if(line.startsWith("Channels")){
					setAudioChannels(Integer.parseInt(line.substring("Channels: ".length())));
				}
				else if(line.startsWith("Samplerate")){
					setAudioSampleRate(Integer.parseInt(line.substring("Samplerate: ".length())));
				}
				else if(line.startsWith("Bitrate")){
					setAudioBitRate(Integer.parseInt(line.substring("Bitrate: ".length())));
				}
			}
			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************
		}
	}

}
