import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VideoPlayer extends JPanel implements ActionListener {

    VideoPlayer(String path, int numFrames, int fps) {
        this.frameInterval = 1000 / fps;

        this.frameUpdateTimer = new Timer(this.frameInterval, this);
        this.frames = new ImageIcon[numFrames];
        for(int i = 0;i < numFrames;i++) {
            this.frames[i] = new ImageIcon(path + String.format("/image%03d.jpg", i + 1));
        }
    }

    public void playVideo() {
        this.frameIndex = 0;
        this.frameUpdateTimer.start();
    }

    public void stopVideo() {
        frameUpdateTimer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.frameIndex < this.frames.length) {
            if (this.frames[this.frameIndex].getImageLoadStatus() == MediaTracker.COMPLETE) {
                this.frames[this.frameIndex].paintIcon(this, g, 0, 0);
                this.frameIndex++;
            }
        }
        else {
            frameUpdateTimer.stop();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private int frameIndex;
    private int frameInterval;
    Timer frameUpdateTimer;
    private ImageIcon [] frames;
}
