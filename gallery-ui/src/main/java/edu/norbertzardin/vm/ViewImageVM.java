package edu.norbertzardin.vm;

import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

public class ViewImageVM {
//    @Wire("#nameContainter")
//    private Div nameContainter;
//
//    @Wire("#descriptionContainer")
//    private Div descriptionContainer;

//    @Wire("#imageContainer")
//    private Image imageContainer;

    @WireVariable
    private ImageService imageService;

    private int id;

//    @AfterCompose
//    public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("imageArg") ImageEntity imageEntity) {
//        Selectors.wireComponents(view, this, false);
//        if(imageEntity != null){
//            System.out.println("COMPOSE VIEW IMAGE " + imageEntity.getId());
//        } else {
//            System.out.println("Image is null. COMPOSE");
//        }
//    }
//
    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("imageArg") ImageEntity imageEntity){
        Selectors.wireComponents(view, this, false);

    }


    @Command
    public void viewImage(){

        // Image begin -------------------------------------
//        // Set up
//        Image imageComponent = new Image();
//        ImageEntity imageEntity = imageService.getImageById(id);
//        AImage image = ImageUtil.byteArrayToImageConverter(imageEntity.getImageData());
//        // Scaling
//        double scale = ImageUtil.getScalingRatio(image, 500.0);
//        imageComponent.setContent(image);
//        imageComponent.setHeight(image.getHeight() * scale + "px");
//        imageComponent.setWidth(image.getWidth() * scale + "px");
//
//        // Parent setting
//        imageComponent.setParent(imageContainer);
        // Image end ----------------------------------------
    }
}
