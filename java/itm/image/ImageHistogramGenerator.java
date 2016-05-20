package itm.image;

/*******************************************************************************
    This file is part of the ITM course 2016
    (c) University of Vienna 2009-2016
*******************************************************************************/


import itm.util.Histogram;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
  * This class creates color and grayscale histograms for various images.
  * It can be called with 3 parameters, an input filename/directory, an output directory and a various bin/interval size.
  * It will read the input image(s), count distinct pixel values and then plot the histogram.
  * 
  * If the input file or the output directory do not exist, an exception is thrown.
  */
public class ImageHistogramGenerator {
	

    /**
     *  Constructor.
     */
    public ImageHistogramGenerator() {
    }

    /**
     * Processes an image directory in a batch process.
     * @param input a reference to the input image file
     * @param output a reference to the output directory
     * @param bins the histogram interval
     * @return a list of the created files
     */
    public ArrayList<File> batchProcessImages( File input, File output, int bins) throws IOException
    {
        if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );

        ArrayList<File> ret = new ArrayList<File>();
        
        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, bins );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                    }
                 }
            } else {
            try {
                File result = processImage( input, output, bins );
                System.out.println( "created " + input + " for " + result );
                ret.add( result );
            } catch ( Exception e0 ) { System.err.println( "Error creating histogram from " + input + " : " + e0.toString() ); }
            } 
        return ret;
    }  
    
    /**
     * Processes the passed input image and stores it to the output directory.
     * @param input a reference to the input image file
     * @param output a reference to the output directory
     * @param bins the histogram interval
     * already existing files are overwritten automatically
     */   
	protected File processImage( File input, File output, int bins ) throws IOException, IllegalArgumentException
    {
		if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( input.isDirectory() ) 
            throw new IOException( "Input file " + input + " is a directory!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );


		// compose the output file name from the absolute path, a path separator and the original filename
		String outputFileName = "";
		outputFileName += output.toString() + File.separator + input.getName().toString();
		File outputFile = new File( output, input.getName() + ".hist.png" );
		
       
		// ***************************************************************
        //  Fill in your code here!
        // ***************************************************************
		
		ImageReader reader;
		
        // load the input image
		int i = input.getName().lastIndexOf('.');
        String inputFormat = input.getName().substring(i+1);
        
        Iterator<ImageReader> readerType = ImageIO.getImageReadersByFormatName(inputFormat);
        reader = readerType.next();
        
        BufferedImage image = null;
        
        ImageInputStream imageInput = ImageIO.createImageInputStream(input);
        reader.setInput(imageInput, true);
        
        image = reader.read(0);
		// get the color model of the image and the amount of color components
		ColorModel colmod = image.getColorModel();
		int numColorComp = colmod.getNumColorComponents();
		// initiate a Histogram[color components] [bins]
		Histogram hist = new Histogram(numColorComp,bins);
		
		// create a histogram array histArray[color components][bins]
		int[][] histArray = new int[numColorComp][bins];
		int width = image.getWidth();
		int height = image.getHeight();
		// read the pixel values and extract the color information
		if(colmod.getColorSpace().getType() == ColorSpace.TYPE_GRAY){
			for(int countWidth = 0; countWidth < width; ++countWidth){
				for(int countHeight = 0; countHeight < height; ++countHeight){
					 Color col = new Color(image.getRGB(countWidth, countHeight));
					 if(col.getGreen()/255.0*bins<bins){
						 histArray[0][(int) (col.getGreen()/255.0*bins)]++;
					 }
					 else{
						 histArray[0][bins-1]++;
					 }
					 
				}
			}
		}
		else if(colmod.getColorSpace().getType() == ColorSpace.TYPE_RGB){
			for(int countWidth = 0; countWidth < width; ++countWidth){
				for(int countHeight = 0; countHeight < height; ++countHeight){
					 Color col = new Color(image.getRGB(countWidth, countHeight));

					 if(col.getRed()/255.0*bins<bins){
						 histArray[0][(int) (col.getRed()/255.0*bins)]++;
					 }
					 else{
						 histArray[0][bins-1]++;
					 }
					 if(col.getGreen()/255.0*bins<bins){
						 histArray[1][(int) (col.getGreen()/255.0*bins)]++;
					 }
					 else{
						 histArray[1][bins-1]++;
					 }
					 if(col.getBlue()/255.0*bins<bins){
						 histArray[2][(int) (col.getBlue()/255.0*bins)]++;
					 }
					 else{
						 histArray[2][bins-1]++;
					 }
				}
			}
		}
		else{
			throw new IllegalArgumentException("The image has an incompatible Colorspacetype.");
		}
		
		// fill the array setHistogram(histArray)
		hist.setHistogram(histArray);
		// plot the histogram, try different dimensions for better visualization
		BufferedImage buffy = hist.plotHistogram(1200, 400);
        // encode and save the image as png
        ImageIO.write(buffy, "png", outputFile);
	
        return outputFile;
    }
    
        
    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
    	if ( args.length < 3 ) {
            System.out.println( "usage: java itm.image.ImageHistogramGenerator <input-image> <output-directory> <bins>" );
            System.out.println( "usage: java itm.image.ImageHistogramGenerator <input-directory> <output-directory> <bins>" );
            System.out.println( "");
            System.out.println( "bins:default 256" );
            System.exit( 1 );
            }
        // read params
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        int bins = Integer.parseInt(args[2]);
        ImageHistogramGenerator histogramGenerator = new ImageHistogramGenerator();
        histogramGenerator.batchProcessImages( fi, fo, bins );        
    }    
}
