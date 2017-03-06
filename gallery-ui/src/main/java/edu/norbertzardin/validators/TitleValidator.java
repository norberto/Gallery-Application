package edu.norbertzardin.validators;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;

public class TitleValidator extends AbstractValidator {
    @Override
    public void validate(ValidationContext ctx) {
        Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());

        String name = (String) beanProps.get("title").getValue();
        Number maxLength = (Number) ctx.getBindContext().getValidatorArg("maxLength");
        if(name == null || name.equals("")) {
            addInvalidMessage(ctx, "title", "Name is required.");
        }
        if(name != null && name.length() > maxLength.intValue()) {
            addInvalidMessage(ctx, "title", "Name is too long.");
        }
    }
}
