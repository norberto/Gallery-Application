package edu.norbertzardin.validators;

import edu.norbertzardin.util.ImageUtil;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TagValidator extends AbstractValidator{
    @Override
    public void validate(ValidationContext ctx) {
        String tags = (String) ctx.getProperty().getValue();
        String[] tagList = ImageUtil.parseTags(tags);
        if(tagList.length == 0) {
            addInvalidMessage(ctx, "At least one tag is required. (maximum 5)");
        }
        if(tagList.length > 5) {
            addInvalidMessage(ctx, "Too many tags. (maximum 5)");
        }
    }
}
