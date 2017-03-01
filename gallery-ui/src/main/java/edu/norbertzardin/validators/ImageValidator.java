package edu.norbertzardin.validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class ImageValidator extends AbstractValidator{
    @Override
    public void validate(ValidationContext ctx) {
        String image = (String) ctx.getProperty().getValue();

        if(image.equals("false")) {
            addInvalidMessage(ctx, "Selected image is required.");
        }
    }
}
