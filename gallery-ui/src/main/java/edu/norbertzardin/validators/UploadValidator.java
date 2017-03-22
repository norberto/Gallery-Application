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
        validateTags(ctx, (String) beanProps.get("tags").getValue(), (Long) ctx.getValidatorArg("maxTags"));
        validateDescription(ctx, (String) beanProps.get("description").getValue(), (Long) ctx.getValidatorArg("maxDescription"));
    }

    private void validateName(ValidationContext ctx, String name, Long maxLength){
        if(name == null || "".equals(name)) {
            addInvalidMessage(ctx, "name", "Name is required.");
        }
        if(name != null && name.length() > maxLength.intValue()) {
            addInvalidMessage(ctx, "name", "Name is too long. (maximum " + maxLength + " characters allowed)");
        }
    }

    private void validateDescription(ValidationContext ctx, String description, Long maxLength) {
        if(description != null && description.length() > maxLength) {
            addInvalidMessage(ctx, "description", "Description is too long. (maximum " + maxLength + " characters allowed)");
        }
    }

    private void validateTags(ValidationContext ctx, String tags, Long maxTags) {
        String[] tagList = ImageUtil.parseTags(tags);
        if(tagList.length == 0) {
            addInvalidMessage(ctx, "tags","At least one tag is required. (maximum " + maxTags + " tags allowed)");
        }
        if(tagList.length > 5) {
            addInvalidMessage(ctx, "tags", "Too many tags. (maximum " + maxTags + " tags allowed)");
        }
    }
}
