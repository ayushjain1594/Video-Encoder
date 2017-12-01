import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class VSegment{
    VSegment() {
        vSegIn = null;
    }

    void addFrames(VFrame [] frameList) {
        vSegIn = frameList;
        numFrames = frameList.length;
    }

    ArrayList<TreeMap<Byte,Byte>> compressSegment(String fileName, int epsilon) throws IOException {
        ArrayList<TreeMap<Byte,Byte>> data = new ArrayList<>();
        TreeMap<Byte,Byte> sigPixels;
        vSegInNumPixels = vSegIn[0].width * vSegIn[0].height;
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(ByteBuffer.allocate(1).put((byte)numFrames).array());
        bos.write(ByteBuffer.allocate(4).putInt(vSegIn[0].width).array());
        bos.write(ByteBuffer.allocate(4).putInt(vSegIn[0].height).array());
        for(Integer i = 0;i < vSegInNumPixels;i++) {
            sigPixels = runDPAlgorithm((byte) 0, (byte) (vSegIn.length - 1), i, epsilon);
            data.add(sigPixels);
            bos.write(ByteBuffer.allocate(4).putInt(i).array());
            bos.write(2 * sigPixels.size());
            for(byte j : sigPixels.keySet()) {
                bos.write(j);
                bos.write(sigPixels.get(j));
            }
        }
        bos.flush();
        bos.close();
        return data;
    }

    private BufferedImage [] toBufferedImage(int width, int height) {
        BufferedImage [] img = new BufferedImage[numFrames];
        for(int i = 0;i < numFrames;i++)
            img[i] = vSegOut[i].toBufferedImage(width,height);
        return img;
    }

    BufferedImage [] decompressSegment(String fname) {
        int index = 0;
        Byte keyStart;
        Byte keyEnd;
        byte num;
        ArrayList<TreeMap<Byte,Byte>> arr = new ArrayList<>();
        TreeMap<Byte,Byte> temp = new TreeMap<>();
        int width, height;
        ByteBuffer b = null;
        Path file = Paths.get(fname);
        try {
            b = ByteBuffer.wrap(Files.readAllBytes(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        numFrames = b.get();
        width = b.getInt();
        height = b.getInt();
        while(index < width * height) {
            index = b.getInt();
            num = b.get();
            for(int i = 0;i < num ;i+=2) {
                byte key = b.get();
                byte val = b.get();
                temp.put(key,val);
            }
            arr.add(new TreeMap<>(temp));
            temp.clear();
            index++;
        }
        int numPixels = width * height;
        BufferedImage [] img = null;
        vSegOut = new VFrame[numFrames];
        for(int i = 0;i < numFrames;i++)
            vSegOut[i] = new VFrame(numPixels, i, width, height);
        for(int i = 0;i < numPixels;i++) {
            keyEnd = 0;
            for(int j = 0;j < arr.get(i).size() - 1;j++) {
                keyStart = keyEnd;
                keyEnd = arr.get(i).ceilingKey((byte) (keyStart + 1));
                vSegOut[keyStart].pixelData[i] = arr.get(i).get(keyStart);
                vSegOut[keyEnd].pixelData[i] = arr.get(i).get(keyEnd);
                for(int k = keyStart + 1;k < keyEnd;k++)
                    vSegOut[k].pixelData[i] = (byte) ((vSegOut[keyStart].pixelData[i] * (keyEnd - k) + (k - keyStart) * vSegOut[keyEnd].pixelData[i]) / (keyEnd - keyStart));
            }
        }
        img = toBufferedImage(width, height);
        return img;
    }

    private TreeMap<Byte,Byte> runDPAlgorithm(byte startTempIndex, byte endTempIndex, int spatialIndex, int epsilon) {
        byte index = 0;
        int maxDist = 0;
        int dist = 0;
        TreeMap<Byte,Byte> sigPixels = new TreeMap<>();
        byte startIndexPixelValue = vSegIn[startTempIndex].pixelData[spatialIndex];
        byte endIndexPixelValue = vSegIn[endTempIndex].pixelData[spatialIndex];
        for(byte i = (byte) (startTempIndex + 1); i < endTempIndex; i++) {
            dist = Math.abs((endIndexPixelValue - startIndexPixelValue) * i - (endTempIndex - startTempIndex) * vSegIn[i].pixelData[spatialIndex]
                            + endTempIndex * startIndexPixelValue - endIndexPixelValue * startTempIndex);
            if(dist > maxDist) {
                index = i;
                maxDist = dist;
            }
        }
        if(maxDist > epsilon) {
            sigPixels.putAll(runDPAlgorithm(startTempIndex, index, spatialIndex, epsilon));
            sigPixels.putAll(runDPAlgorithm(index, endTempIndex, spatialIndex, epsilon));
        }
        else {
            sigPixels.put(startTempIndex, startIndexPixelValue);
            sigPixels.put(endTempIndex, endIndexPixelValue);
        }
        return sigPixels;
    }

    private VFrame [] vSegIn;
    private int vSegInNumPixels;
    private int numFrames;
    private VFrame [] vSegOut;
}
