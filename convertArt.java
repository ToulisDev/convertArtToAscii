package convertArtToAscii;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;


/**
 * This program converts any image (tested png and jpeg)
 * to ASCII characters as well as resize them to prevent
 * ASCII matrix from getting too large for your screen.
 * 
 * @author ToulisDev
 *
 */
public class convertArt {
	
	private String inputImagePath;
	private String outputImagePath;
	BufferedImage image;
	PrintWriter pw;
	FileWriter fw;
	double gValue;
	
	/**
	 * Initialize program by asking user full path
	 * of image and output as well as resize option.
	 */
	public void getImagePath() {
		double resizeValue = 1;
		try(Scanner sc = new Scanner(System.in)){
			do {
				System.out.println("Please type full path of Image you want to convert:");
				this.inputImagePath = sc.nextLine();
			}while(!new File(inputImagePath).exists());
			do {
				System.out.println("Please type the path you want to export the finished file:");
				this.outputImagePath = sc.nextLine();
			}while(!new File(outputImagePath).isDirectory());
			this.outputImagePath = this.outputImagePath + "\\result.txt";
			String option;
			do {
			System.out.println("Do you want to resize your input image? (Y/N)");
			option = sc.nextLine().toUpperCase();
			}while (!option.equals("Y") && !option.equals("N"));
			if (option.equals("Y")) {
				do {
					System.out.println("Please type a value between 0 and 1. ex. 0.25");
					option = sc.nextLine();
				}while(Double.valueOf(option) <= 0 && Double.valueOf(option) > 1);
				try {
					resizeValue = Double.valueOf(option);
				}catch (Exception e) {
					System.out.println("An error occured while converting string to Double.");
				}
			}
			try {
				pw = new PrintWriter(fw = new FileWriter((this.outputImagePath), true));
			}catch (IOException iox) {
				System.out.println("An error occured while making the output file.");
			}
			if (resizeValue != 1) {
				resizeConvertImage(resizeValue);
			}else {
				convertImage();
			}
		}
		
	}
	
	/**
	 * Converts an image to ASCII characters
	 */
	public void convertImage() {
		//Read Image file from path
		try {
			image = ImageIO.read(new File(inputImagePath));
		}catch (IOException iox) {
			System.out.println("An error occured while reading image.");
		}
		//Loads pixel data of image and uses helpful methods to create out output file  
		for (int y=0;y<image.getHeight();y++) {
			for(int x=0;x<image.getWidth();x++) {
				Color pixelColor = new Color(image.getRGB(x, y));
				gValue = (0.21*pixelColor.getRed()) + (0.07*pixelColor.getBlue()) + (0.72*pixelColor.getGreen());
				print(returnString(gValue));
			}
			try {
				pw.println("");
				pw.flush();
				fw.flush();
			} catch(Exception e) {
				
			}
			
		}
	}
	
	/**
	 * Resizes and Converts an image to ASCII characters
	 * @param resizeValue Double number specifies percentage of the output image
	 */
	public void resizeConvertImage(double resizeValue) {
		
		try {
			image = resize(inputImagePath, resizeValue);
		}catch (IOException iox) {
			System.out.println("An error occured while reading image.");
		}
		for (int y=0;y<image.getHeight();y++) {
			for(int x=0;x<image.getWidth();x++) {
				Color pixelColor = new Color(image.getRGB(x, y));
				gValue = (pixelColor.getRed() + pixelColor.getBlue() + pixelColor.getGreen()) / 3;
				print(returnString(gValue));
			}
			try {
				pw.println("");
				pw.flush();
				fw.flush();
			} catch(Exception e) {
				
			}
			
		}
	}
	
	/**
	 * This method returns converted pixel data into ASCII character
	 * @param g Double brightness value (Combined value of RGB divided by 3)
	 */
	public String returnString(double g) {
		//Helpful method to return pixel into ASCII character
		String str = " ";
		String[] characters = {" ","`","^","\"",",",":",";","I","l","!","i","~","+","_","-","?","]","[","}","{","1",")","(","|","\\","/","t","f","j","r","x","n","u","v","c","z","X","Y","U","J","C","L","Q","0","O","Z","m","w","q","p","d","b","k","h","a","o","*","#","M","W","&","8","%","B","@","$"}; 
		int selectedChar = (int) ((g/255)*(characters.length-1));
		str = characters[(characters.length-1)-selectedChar];
		return str;
	}
	
	/**
	 * Writes the corresponding string to output file.
	 * @param str String of converted pixel(ASCII characters)
	 */
	public void print(String str) {
		//
		try {
			pw.append(str);
			pw.flush();
			fw.flush();
		}catch(IOException iox) {
			System.out.println("An error occured while writing the corresponding character");
		}
	}
	
	/**
     * Resizes an image to a specific width and height
     * @param inputImagePath Path of the original image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
	public static BufferedImage resize(String inputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        BufferedImage outputImage = new BufferedImage(scaledWidth,scaledHeight, inputImage.getType());
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        return outputImage;
    }
	
	/**
     * Resizes an image by a percentage of original size (proportional)
     * and returns that image.
     * @param inputImagePath Path of the original image
     * @param percent Double number specifies percentage of the output image
     * over the input image.
     * @throws IOException
     */
	public static BufferedImage resize(String inputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        return resize(inputImagePath, scaledWidth, scaledHeight);
    }
		 
	public static void main(String[] args) {
		convertArt ascii = new convertArt();
		ascii.getImagePath();
		System.out.println("Finished Converting.");
		
	}

}
