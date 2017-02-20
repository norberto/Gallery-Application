package edu.norbertzardin.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.image.AImage;
import org.zkoss.zul.Image;

import java.io.IOException;


public class ImageConverter implements Converter<AImage, byte[], Image> {
    private Log logger = LogFactory.getLog(ImageConverter.class);

    public byte[] coerceToBean(AImage compAttr, Image component, BindContext ctx) {
        logger.debug("Converting the image");
        return compAttr.getByteData();
    }

    public AImage coerceToUi(byte[] beanProp, Image component, BindContext ctx) {
        try {
            if (beanProp != null && beanProp.length > 0) {
                AImage im = new AImage("", beanProp);
                String maxSize = (String) ctx.getConverterArg("maxSize");
                System.out.println("Max size: " + maxSize);
                double size = Double.parseDouble(maxSize);
                double scale = ImageUtil.getScalingRatio(im, size);

                component.setHeight(im.getHeight() * scale + "px");
                component.setWidth(im.getWidth() * scale + "px");

                component.setContent(im);
                return im;
            }
            logger.debug("Return null => image is empty");
            return null;
        } catch (IOException e) {
            logger.error("Error occured, returning null", e);
            return null;
        }
    }
}
