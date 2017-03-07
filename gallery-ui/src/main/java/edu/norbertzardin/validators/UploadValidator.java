package edu.norbertzardin.validators;

import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;

public class UploadValidator extends AbstractValidator{

    @Override
    public void validate(ValidationContext ctx) {
        Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());

        validateName(ctx, (String) beanProps.get("name").getValue(), (Long) ctx.getValidatorArg("maxLength"));
        validateDescription(ctx, (String) beanProps.get("description").getValue());
        validateTags(ctx, (String) beanProps.get("tags").getValue());
    }

    private void validateName(ValidationContext ctx, String name, Long maxLength){
        if(name == null || name.equals("")) {
            addInvalidMessage(ctx, "name", "Name is required.");
        }
        if(name != null && name.length() > maxLength.intValue()) {
            addInvalidMessage(ctx, "name", "Name is too long.");
        }
    }

    private void validateDescription(ValidationContext ctx, String description) {
        // do nothing
    }

    private void validateTags(ValidationContext ctx, String tags) {
        String[] tagList = ImageUtil.parseTags(tags);
        if(tagList.length == 0) {
            addInvalidMessage(ctx, "tags", "At least ONE tag is required.");
        }
    }
}
