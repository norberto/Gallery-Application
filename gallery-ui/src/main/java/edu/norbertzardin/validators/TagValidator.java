package edu.norbertzardin.validators;

import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TagValidator extends AbstractValidator{
    @Override
    public void validate(ValidationContext ctx) {
        String tags = (String) ctx.getProperty().getValue();
        String[] tagList = ImageUtil.parseTags(tags);
        Integer limit = (Integer) ctx.getValidatorArg("tagsLeft");
        Integer removed = (Integer) ctx.getValidatorArg("removed");
        limit += removed;
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
