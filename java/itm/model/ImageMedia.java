package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/

import java.awt.color.ColorSpace;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * This class describes an image.
 */
public class ImageMedia extends AbstractMedia {

	public final static int ORIENTATION_LANDSCAPE = 0;
	public final static int ORIENTATION_PORTRAIT = 1;

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	// add required properties (scope: protected!)


    protected int width;
    
    protected int height;
    
    protected int componentsNumber;
    
    protected int colorNumber;
    
    protected int transparency;
    
    protected int pixelSize;
    
    protected int colorSpaceType;
    
    protected int orientation;
	
	// add get/set methods for the properties

    public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getComponentsNumber() {
		return componentsNumber;
	}

	public void setComponentsNumber(int componentsNumber) {
		this.componentsNumber = componentsNumber;
	}

	public int getColorNumber() {
		return colorNumber;
	}

	public void setColorNumber(int colorNumber) {
		this.colorNumber = colorNumber;
	}

	public int getTransparency() {
		return transparency;
	}

	public void setTransparency(int transparency) {
		this.transparency = transparency;
	}

	public int getPixelSize() {
		return pixelSize;
	}

	public void setPixelSize(int pixelSize) {
		this.pixelSize = pixelSize;
	}

	public int getColorSpaceType() {
		return colorSpaceType;
	}

	public void setColorSpaceType(int colorSpaceType) {
		this.colorSpaceType = colorSpaceType;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	/**
	 * Constructor.
	 */
	public ImageMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public ImageMedia(File instance) {
		super(instance);
	}

	/**
	 * Converts a color space type to a human readable string
	 * 
	 * @return a string describing the passed colorspace
	 */
	protected String serializeCSType(int cstype) {
		switch (cstype) {
		case ColorSpace.CS_CIEXYZ:
			return "CS_CIEXYZ";
		case ColorSpace.CS_GRAY:
			return "CS_GRAY";
		case ColorSpace.CS_LINEAR_RGB:
			return "CS_LINEAR_RGB";
		case ColorSpace.CS_PYCC:
			return "CS_PYCC";
		case ColorSpace.CS_sRGB:
			return "CS_sRGB";
		case ColorSpace.TYPE_CMY:
			return "TYPE_CMY";
		case ColorSpace.TYPE_CMYK:
			return "TYPE_CMYK";
		case ColorSpace.TYPE_GRAY:
			return "TYPE_GRAY";
		case ColorSpace.TYPE_RGB:
			return "TYPE_RGB";
		case ColorSpace.TYPE_HLS:
			return "TYPE_HLS";
		default:
			return "" + cstype;
		}
	}

	/**
	 * Converts a human readable string string to a color space type
	 * 
	 * @return the colorspace corresponding to the passed string
	 */
	protected int deserializeCSType(String cstype) {
		if (cstype.equals("CS_CIEXYZ"))
			return ColorSpace.CS_CIEXYZ;
		if (cstype.equals("CS_GRAY"))
			return ColorSpace.CS_GRAY;
		if (cstype.equals("CS_LINEAR_RGB"))
			return ColorSpace.CS_LINEAR_RGB;
		if (cstype.equals("CS_PYCC"))
			return ColorSpace.CS_PYCC;
		if (cstype.equals("CS_sRGB"))
			return ColorSpace.CS_sRGB;
		if (cstype.equals("TYPE_CMY"))
			return ColorSpace.TYPE_CMY;
		if (cstype.equals("TYPE_CMYK"))
			return ColorSpace.TYPE_CMYK;
		if (cstype.equals("TYPE_GRAY"))
			return ColorSpace.TYPE_GRAY;
		if (cstype.equals("TYPE_RGB"))
			return ColorSpace.TYPE_RGB;
		if (cstype.equals("TYPE_HLS"))
			return ColorSpace.TYPE_HLS;
		return Integer.parseInt(cstype);
	}

	/**
	 * Serializes this object to a string buffer.
	 * 
	 * @return a StringBuffer containing a serialized version of this object.
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		// print writer for creating the output
		PrintWriter out = new PrintWriter(data);
		// print type
		out.println("type: image");
		StringBuffer sup = super.serializeObject();
		// print the serialization of the superclass (AbstractMedia)
		out.print(sup);

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// print properties
		out.println("width: " + getWidth());
        out.println("height: " + getHeight());
        out.println("numComponents: " + getComponentsNumber());
        out.println("numColorComponents: " + getColorNumber());

        out.println("transparency: " + getTransparency());
        out.println("pixelSize: " + getPixelSize());
        out.println("orientation: " + getOrientation());
        out.println("colorSpaceType: " + serializeCSType(getColorSpaceType()));
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
			int i;
			// read and set properties
			if(line.startsWith("width")){
        		i = Integer.parseInt(line.substring("width: ".length()));
        		setWidth(i);
        	}
        	if(line.startsWith("height")){
        		i = Integer.parseInt(line.substring("height: ".length()));
        		setHeight(i);
        	}
        	if(line.startsWith("numComponents")){
        		i = Integer.parseInt(line.substring("numComponents: ".length()));
        		setComponentsNumber(i);
        	}
        	if(line.startsWith("numColorComponents")){
        		i = Integer.parseInt(line.substring("numColorComponents: ".length()));
        		setColorNumber(i);
        	}
        	if(line.startsWith("transparency")){
        		i = Integer.parseInt(line.substring("transparency: ".length()));
        		setTransparency(i);
        	}
        	if(line.startsWith("pixelSize")){
        		i = Integer.parseInt(line.substring("pixelSize: ".length()));
        		setPixelSize(i);
        	}
        	if(line.startsWith("orientation")){
        		i = Integer.parseInt(line.substring("orientation: ".length()));
        		setOrientation(i);
        	}
        	if(line.startsWith("colorSpaceType")){
        		i = Integer.parseInt(line.substring("colorSpaceType: ".length()));
        		setColorSpaceType(i);
        	}
		}
	}
}
