package edu.norbertzardin.util;

import org.zkoss.image.AImage;

public class ImageUtil {
    public static AImage byteArrayToImageConverter(byte[] data){
        try{
            return new AImage("", data);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static double getScalingRatio(AImage image, double maxSize){
        double scale = 1;
        if(image == null) return 1;
        if(image.getHeight() >= image.getWidth()) {
            if (image.getHeight() > maxSize) scale = maxSize / image.getHeight();
        } else {
            if(image.getWidth() > maxSize) scale = maxSize / image.getWidth();
        }
        return scale;
    }
}
