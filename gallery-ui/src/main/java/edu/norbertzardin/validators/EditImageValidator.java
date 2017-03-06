package edu.norbertzardin.validators;

import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;

public class EditImageValidator extends AbstractValidator{

    private String name;
    private String description;
    private String tags;
    private Long maxLength;

    @Override
    public void validate(ValidationContext validationContext) {
        Map<String,Property> beanProps = validationContext.getProperties(validationContext.getProperty().getBase());

        name = (String) beanProps.get("name").getValue();
        description = (String) beanProps.get("description").getValue();
        tags = (String) beanProps.get("tags").getValue();

        maxLength = (Long) validationContext.getValidatorArg("maxLength");

        validateName(validationContext);
    }

    private void validateName(ValidationContext ctx){
        if(name == null || name.equals("")) {
            addInvalidMessage(ctx, "name", "Name is required.");
        }
        if(name != null && name.length() > maxLength.intValue()) {
            addInvalidMessage(ctx, "name", "Name is too long.");
        }
    }

    private void validateDescription(ValidationContext ctx) {
        // do nothing
    }

    private void validateTags(ValidationContext ctx) {
        // TO DO validate tag limit
    }
}
