package edu.norbertzardin.util;

import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.zul.Filedownload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    public static BufferedImage getScaledInstance(BufferedImage img,
                                           int targetWidth,
                                           int targetHeight,
                                           Object hint,
                                           boolean higherQuality)
    {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage)img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }


    public static byte[] scaleImageToSize(Image img, Integer maxSize) {
        byte[] result;
        try {
            Dimension imgSize = new Dimension(img.getWidth(), img.getHeight());
            Dimension boundary = new Dimension(maxSize, maxSize);


            BufferedImage bufferedImage = ImageUtil.getScaledInstance(ImageIO.read(
                    new ByteArrayInputStream(img.getByteData())),
                    (int) getScaledDimension(imgSize, boundary).getWidth(),
                    (int) getScaledDimension(imgSize, boundary).getHeight(),
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, img.getFormat(), baos);
            baos.flush();
            result = baos.toByteArray();
            baos.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] parseTags(String tags) {
        if(tags == null || tags.equals("")) return new String[0];
        String[] tagList = tags.split("\\W+");

        List<String> list = new ArrayList<String>(Arrays.asList(tagList));
        return removeDuplicates(list);
    }

    private static String[] removeDuplicates(List<String> array) {
        Set set = new HashSet<String>(array); //values must default to false
        return (String[]) set.toArray(new String[set.size()]);
    }

    public static String tagsToString(List<TagEntity> tags) {
        String tagString = "";

        for(TagEntity tag : tags) {
            tagString = tagString.concat(tag.getName() + ", ");
        }

        return tagString.substring(0, tagString.length() - 2);
    }

    public static void download(ImageEntity image) {
        Filedownload.save(image.getDownload().getData(),
                "image/" + image.getDatatype(),
                "download_" + image.toString());
    }
}
