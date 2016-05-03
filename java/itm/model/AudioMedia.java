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
import java.util.Date;

import javax.sound.sampled.AudioFormat.Encoding;

public class AudioMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	//properties
	Long durationMicroS;
	String author, title, copyright, comment, album, date, durationMinS;
	Encoding encoding;
	Float sampleRate;
	Integer bitrate, channels;
	
	//for mp3
	String track, composer, genre;
	
	/**
	 * Constructor.
	 */
	public AudioMedia() {
		super();
	}


	/**
	 * Constructor.
	 */
	public AudioMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */

	public Long getDuration() {
		return durationMicroS;
	}

	public void setDurationMicroS(Long durationMicroS) {
		this.durationMicroS = durationMicroS;
		Integer all_seconds =  (int) (durationMicroS/1000000);
		Integer minutes = all_seconds/60;
		Integer seconds = all_seconds%60;
		this.durationMinS = 
				  ((minutes>10) ? minutes.toString() : "0" + minutes.toString()) 
				+ ":" 
				+ ((seconds>10) ? seconds.toString() : "0" + seconds.toString());
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Encoding getEncoding() {
		return encoding;
	}

	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}

	public Float getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(Float f) {
		this.sampleRate = f;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public Integer getChannels() {
		return channels;
	}

	public void setChannels(Integer channels) {
		this.channels = channels;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String getDurationMinS(){
		return durationMinS;
	}
	
	public void setDurationMinS(String durationMinS){
		this.durationMinS = durationMinS;
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
		out.println("type: audio");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************
		
		if(getDurationMinS() != null)
			out.println("duration: " + getDurationMinS() + " (mm:ss)");
		if(getEncoding() != null)
			out.println("encoding: " + getEncoding());
		if(getSampleRate() != null)
			out.println("sample rate: " + getSampleRate());
		if(getBitrate() != null)
			out.println("bitrate: " + getBitrate());
		if(getChannels() != null)
			out.println("channels: " + getChannels());
		
		if(getAlbum() != null)
			out.println("album: " + getAlbum());
		if(getAuthor() != null)
			out.println("author: " + getAuthor());
		if(getComposer() != null)
			out.println("composer: " + getComposer());
		if(getCopyright() != null)
			out.println("copyright: " + getCopyright());
		if(getTitle() != null)
			out.println("title: " + getTitle());
		if(getTrack() != null)
			out.println("track: " + getTrack());
		if(getDate() != null)
			out.println("date: " + getDate());
		if(getComment() != null)
			out.println("comment: " + getComment());

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
		while ((line = br.readLine()) != null) {

			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************
			
			if ( line.startsWith( "duration: " ) ) {
                setDurationMicroS(Long.parseLong((line.substring( "duration: ".length() ))));
        	} else
            if ( line.startsWith( "encoding: " ) ){
            	setEncoding(new Encoding(line.substring( "encoding: : ".length() )));
            } else
        	if ( line.startsWith( "sample rate: " ) ){
            	setSampleRate(Float.parseFloat((line.substring( "sample rate: ".length() ))));
            } else
        	if ( line.startsWith( "bitrate: " ) ){
            	setBitrate(Integer.parseInt((line.substring( "bitrate: ".length() ))));
            } else
        	if ( line.startsWith( "channels: " ) ){
            	setChannels(Integer.parseInt((line.substring( "channels: ".length() ))));
            } else
        	if ( line.startsWith( "album: " ) ){
            	setAlbum((line.substring( "album: ".length() )));
            } else
        	if ( line.startsWith( "author: " ) ){
            	setAuthor((line.substring( "author: ".length() )));
            } else
        	if ( line.startsWith( "composer: " ) ){
            	setComposer((line.substring( "composer: ".length() )));
            } else
        	if ( line.startsWith( "copyright: " ) ){
            	setCopyright((line.substring( "copyright: ".length() )));
            } else
        	if ( line.startsWith( "title: " ) ){
            	setTitle((line.substring( "title: ".length() )));
            } else
        	if ( line.startsWith( "track: " ) ){
            	setTrack((line.substring( "track: ".length() )));
            } else
        	if ( line.startsWith( "date: " ) ){
            	setDate((line.substring( "date: ".length() )));
            } else
           	if ( line.startsWith( "comment: " ) ){
            	setComment((line.substring( "comment: ".length() )));
            }

		}
	}




}
