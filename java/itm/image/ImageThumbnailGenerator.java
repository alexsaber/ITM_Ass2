package itm.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/*******************************************************************************
    This file is part of the ITM course 2016
    (c) University of Vienna 2009-2016
*******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
    This class converts images of various formats to PNG thumbnails files.
    It can be called with 3 parameters, an input filename/directory, an output directory and a compression quality parameter.
    It will read the input image(s), grayscale and scale it/them and convert it/them to a PNG file(s) that is/are written to the output directory.

    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageThumbnailGenerator 
{

    /**
        Constructor.
    */
    public ImageThumbnailGenerator()
    {
    }

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param rotation
        @param overwrite indicates whether existing thumbnails should be overwritten or not
        @return a list of the created files
    */
    public ArrayList<File> batchProcessImages( File input, File output, double rotation, boolean overwrite ) throws IOException
    {
        if ( ! input.exists() ) {
            throw new IOException( "Input file " + input + " was not found!" );
        }
        if ( ! output.exists() ) {
            throw new IOException( "Output directory " + output + " not found!" );
        }
        if ( ! output.isDirectory() ) {
            throw new IOException( output + " is not a directory!" );
        }

        ArrayList<File> ret = new ArrayList<File>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, rotation, overwrite );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        } else {
            try {
                File result = processImage( input, output, rotation, overwrite );
                System.out.println( "converted " + input + " to " + result );
                ret.add( result );
            } catch ( Exception e0 ) {
                System.err.println( "Error converting " + input + " : " + e0.toString() );
            }
        } 
        return ret;
    }  

    /**
        Processes the passed input image and stores it to the output directory.
        This function should not do anything if the outputfile already exists and if the overwrite flag is set to false.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param dimx the width of the resulting thumbnail
        @param dimy the height of the resulting thumbnail
        @param overwrite indicates whether existing thumbnails should be overwritten or not
    */
    protected File processImage( File input, File output, double rotation, boolean overwrite ) throws IOException, IllegalArgumentException
    {
        if ( ! input.exists() ) {
            throw new IOException( "Input file " + input + " was not found!" );
        }
        if ( input.isDirectory() ) {
            throw new IOException( "Input file " + input + " is a directory!" );
        }
        if ( ! output.exists() ) {
            throw new IOException( "Output directory " + output + " not found!" );
        }
        if ( ! output.isDirectory() ) {
            throw new IOException( output + " is not a directory!" );
        }

        // create outputfilename and check whether thumb already exists
        File outputFile = new File( output, input.getName() + ".thumb.png" );
        if ( outputFile.exists() ) {
            if ( ! overwrite ) {
                return outputFile;
            }
        }

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        // load the input image
        BufferedImage b_img = ImageIO.read(input);
        
        System.out.println("Old sizes. H: " + b_img.getHeight() + " W " + b_img.getWidth());
        
        //rotate if in portrait
    	BufferedImage rotImage = new BufferedImage(b_img.getHeight(), b_img.getWidth(), BufferedImage.TYPE_INT_ARGB);
        if (b_img.getWidth() < b_img.getHeight()) {//portrait
        	
            Graphics2D graphics1 = (Graphics2D) rotImage.getGraphics();
            graphics1.rotate(Math.toRadians(90), rotImage.getWidth() / 2, rotImage.getHeight() / 2);
            graphics1.translate((rotImage.getWidth() - b_img.getWidth()) / 2, (rotImage.getHeight() - b_img.getHeight()) / 2);
            graphics1.drawImage(b_img, 0, 0, b_img.getWidth(), b_img.getHeight(), null);
           
          }else{
        	  rotImage = b_img;
          }
        
        System.out.println("New sizes. H: " + rotImage.getHeight() + " W " + rotImage.getWidth());
        
        // add a watermark of your choice and paste it to the image
        // e.g. text or a graphic

        //reference: http://stackoverflow.com/questions/5459701/how-can-i-watermark-an-image-in-java
        
        Graphics graphics = rotImage.getGraphics();
        graphics.drawImage(rotImage, 0, 0, null);
        int font_size = (rotImage.getHeight() + rotImage.getWidth() ) / 10;
        graphics.setFont(new Font("Arial", Font.BOLD, font_size));

        String watermark = "\u00a9 1263258";

        graphics.drawString(watermark, 0, rotImage.getHeight() / 2);
        graphics.dispose();

        // scale the image to a maximum of [ 200 w X 100 h ] pixels - do not distort!
        // if the image is smaller than [ 200 w X 100 h ] - print it on a [ dim X dim ] canvas!
        
        Dimension boundary = null;
        if (rotImage.getWidth() < 200 && rotImage.getHeight() < 100){
        	boundary = new Dimension(200,100);
        }
        else
        	boundary = new Dimension(200,rotImage.getHeight());
        
        Dimension newDimension = getScaledDimension(new Dimension(rotImage.getWidth(), rotImage.getHeight()),boundary);
        
        if(newDimension.height >= 100) 
        	boundary.height = newDimension.height;
        
        System.out.println("newDimension was set to H: " + newDimension.getHeight() + " W " + newDimension.getWidth());
        
        //create canvases 200x100
        BufferedImage canvas_resized = new BufferedImage(boundary.width, boundary.height, rotImage.getType());
        BufferedImage canvas_rotated = new BufferedImage(boundary.width, boundary.height, rotImage.getType());
        		
        BufferedImage resized_img = new BufferedImage((int)newDimension.getWidth(), (int)newDimension.getHeight(), rotImage.getType());
        

        Graphics2D graphic_resized = resized_img.createGraphics();
        AffineTransform at_resize = 
        		AffineTransform.getScaleInstance((double)newDimension.getWidth()/rotImage.getWidth()
        											, (double)newDimension.getHeight()/rotImage.getHeight());
        graphic_resized.drawRenderedImage(rotImage, at_resize);

        // rotate you image by the given rotation parameter
        // save as extra file - say: don't return as output file
   
        AffineTransform aff_rot_n = new AffineTransform();
        
        aff_rot_n.translate(canvas_rotated.getWidth() / 2, canvas_rotated.getHeight() / 2);
        
        aff_rot_n.rotate(Math.toRadians(rotation));//rotation is one of the passed parameters
        
        aff_rot_n.translate(-resized_img.getWidth() / 2, -resized_img.getHeight() / 2);


        
        Graphics2D g2 = canvas_rotated.createGraphics();
        g2.drawImage(resized_img, aff_rot_n, null);
        

        File rotated_file = new File( output, input.getName() + ".thumb.rotated.png" );
        ImageIO.write(canvas_rotated, "png", rotated_file);
        

        // encode and save the image  
        
        //placing the resized image in the center of the 200x100 canvas
        Graphics2D canvas_resized_graphics = canvas_resized.createGraphics();
        canvas_resized_graphics.drawImage(resized_img, (boundary.width - resized_img.getWidth()) / 2, (boundary.height - resized_img.getHeight()) / 2, null);
        
        ImageIO.write(canvas_resized, "png", outputFile);

        return outputFile;

        /**
            ./ant.sh ImageThumbnailGenerator -Dinput=media/img/ -Doutput=test/ -Drotation=90
        */
    }
    
    //reference: http://stackoverflow.com/questions/10245220/java-image-resize-maintain-aspect-ratio
    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
    
    

    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 3 ) {
            System.out.println( "usage: java itm.image.ImageThumbnailGenerator <input-image> <output-directory> <rotation degree>" );
            System.out.println( "usage: java itm.image.ImageThumbnailGenerator <input-directory> <output-directory> <rotation degree>" );
            System.exit( 1 );
        }
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        double rotation = Double.parseDouble( args[2] );

        ImageThumbnailGenerator itg = new ImageThumbnailGenerator();
        itg.batchProcessImages( fi, fo, rotation, true );
    }    
}