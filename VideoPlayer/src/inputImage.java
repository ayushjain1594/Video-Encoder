//Import modules
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

//Class to read images and pixel values.
public class inputImage {
    int h;
    int w;
    int[][][] pixelR;
    int[][][] pixelG;
    int[][][] pixelB;
    boolean flag=false;

    inputImage(String location){
        try {
            for (int j = 1; j <= 100; j++) {   //Looping over all the images in the folder path 'location'
                File path=new File(location+"\\image"+String.format("%03d",j)+".jpg");
                BufferedImage im = ImageIO.read(path);      //Reading image as im

                //Get the dimensions of the images
                if(!flag){
                    getDimensions(im);  //Called once
                    flag=true;
                }

                //Get the pixel values of each image
                pixelValues(im,j);
            }
        } catch (IOException err){
            System.out.println("Could not load image/s");
        }
        segmentFrames segF=new segmentFrames(pixelR,pixelG,pixelB,100);
    }

    // Method to get the dimension of the images
    public void getDimensions(BufferedImage image){
        h=image.getHeight();
        w=image.getWidth();
        // Create 3 dimensional arrays each for R, G and B
        // Assuming 100 images in the folder
        pixelR=new int [w][h][100];
        pixelG=new int [w][h][100];
        pixelB=new int [w][h][100];
    }

    // Method reads the pixel values from each frame and store them.
    public void pixelValues(BufferedImage image,int frame_num){
        for (int i=0;i<w;i++){
            for (int j=0;j<h;j++){
                pixelR[i][j][frame_num-1]=Integer.parseInt(Integer.toBinaryString(image.getRGB(i,j)).substring(8,15),2);
                pixelG[i][j][frame_num-1]=Integer.parseInt(Integer.toBinaryString(image.getRGB(i,j)).substring(16,23),2);
                pixelR[i][j][frame_num-1]=Integer.parseInt(Integer.toBinaryString(image.getRGB(i,j)).substring(24,31),2);
            }
        }

    }
}
