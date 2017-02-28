package edu.norbertzardin.validators;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TitleValidator extends AbstractValidator {
    @Override
    public void validate(ValidationContext ctx) {
        String name = (String) ctx.getProperty().getValue();
        Number maxLength = (Number) ctx.getBindContext().getValidatorArg("maxLength");
        if(name == null || name.equals("")) {
            addInvalidMessage(ctx, "Name is required.");
        }
        if(name != null && name.length() > maxLength.intValue()) {
            addInvalidMessage(ctx, "Name is too long.");
        }
    }
}
