import javax.swing.*;
import java.awt.*;

public class VideoEncoderMain {

    public static void main(String [] arg) {
        JFrame disp = new JFrame("SAR Video Player");
        VideoPlayer player = new VideoPlayer("/home/ravi/Desktop/Sample_images", 100, 10);
        //player.setOpaque(true);
        //player.setBackground(Color.BLACK);
        disp.setLayout(new GridLayout());
        disp.add(player, BorderLayout.NORTH);
        disp.pack();
        disp.setSize(800,800);
        disp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        disp.setVisible(true);
        player.playVideo();
    }
}
