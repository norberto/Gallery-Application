package edu.norbertzardin.validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class DescriptionValidator extends AbstractValidator {

    @Override
    public void validate(ValidationContext ctx) {
        String description = (String) ctx.getProperty().getValue();
        Number maxLength = (Number) ctx.getValidatorArg("maxLength");
        if(description != null && description.length() > maxLength.intValue()) {
            addInvalidMessage(ctx, "description", "Description is too long. (maximum " + maxLength + " characters)");
        }
    }
}
