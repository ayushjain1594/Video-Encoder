import javax.swing.*;
import java.awt.*;

public class VideoEncoderMain {

    public static void main(String [] arg) {
        String loc=System.getProperty("user.dir")+"\\Sample_images"; //Location where the images are stored.
        //inputImage images= new inputImage(loc);       //Input all images
        JFrame disp = new JFrame("SAR Video Player");
        VideoPlayer player = new VideoPlayer(loc, 100, 10);
        //player.setOpaque(true);
        //player.setBackground(Color.BLACK);
        disp.setLayout(new GridLayout());
        disp.add(player, BorderLayout.NORTH);
        disp.pack();
        disp.setSize(800,800);
        disp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        disp.setVisible(true);
        player.playVideo();
        //System.out.println(System.getProperty("user.dir"));
    }
}
