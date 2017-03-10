package edu.norbertzardin.validators;

import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Map;

public class EditImageValidator extends AbstractValidator {

    @Override
    public void validate(ValidationContext ctx) {
        Map<String, Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());

        validateName(ctx, (String) beanProps.get("name").getValue(), (Long) ctx.getValidatorArg("maxLength"));
        validateDescription(ctx, (String) beanProps.get("description").getValue(),(Long) ctx.getValidatorArg("maxDescription"));
        validateTags(ctx, (String) beanProps.get("tags").getValue(), (Integer) ctx.getValidatorArg("tagLimit"));
    }

    private void validateName(ValidationContext ctx, String name, Long maxLength) {
        if (name == null || name.equals("")) {
            addInvalidMessage(ctx, "name", "Name is required.");
        }
        if (name != null && name.length() > maxLength) {
            addInvalidMessage(ctx, "name", "Name is too long.");
        }
    }

    private void validateDescription(ValidationContext ctx, String description, Long maxLength) {
        if(description != null && description.length() > maxLength) {
            addInvalidMessage(ctx, "description", "Description is too long. (maximum " + maxLength + " characters)");
        }
    }

    private void validateTags(ValidationContext ctx, String tags, Integer limit) {
        String[] tagList = ImageUtil.parseTags(tags);
        String error;
        if(limit == 0) {
            error = "No more tags allowed.";
        } else if(limit == 1) {
            error = "Too many tags specified, only one more tag is allowed.";
        } else {
            error = "Too many tags specified, only " + limit + " tags are allowed.";
        }
        if(tagList.length > limit) {
            addInvalidMessage(ctx, "tags", error);
        }
    }
}
