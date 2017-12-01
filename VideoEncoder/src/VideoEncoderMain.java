import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class VideoEncoderMain {

    public static void main(String [] arg) {
        BufferedImage [] imgOut;
        VideoPlayer v;
        VSegment vSeg = new VSegment();
        ArrayList<TreeMap<Byte,Byte>> arr;
        String loc=System.getProperty("user.dir")+"\\Images"; //Location where the images are stored.
        VFrame [] frames = new VFrame[100];
        try {
            for (int i = 0; i < 100; i++) {
                frames[i] = new VFrame(ImageIO.read(new File(loc + String.format("\\dimage%03d.jpg", i))), i);
            }
            vSeg.addFrames(frames);
            arr = vSeg.compressSegment("cvideo.sar", 100);
            imgOut = vSeg.decompressSegment("cvideo.sar");
            v = new VideoPlayer(imgOut,10);
            v.playVideo();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }


}
