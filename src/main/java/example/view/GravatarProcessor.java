package example.view;

import example.MessageDigestUtil;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.processor.element.*;
import org.thymeleaf.templatemode.TemplateMode;

public class GravatarProcessor extends AbstractAttributeModelProcessor {

    private static final String ATTR_NAME = "gravatar";
    private static final int PRECEDENCE = 10000;

    protected GravatarProcessor(String dialectPrefix) {
        super(
                TemplateMode.HTML,
                dialectPrefix,
                null,
                false,
                ATTR_NAME,
                true,
                PRECEDENCE,
                false
        );
    }

    @Override
    protected void doProcess(ITemplateContext context, IModel model, AttributeName attributeName, String attributeValue, IElementModelStructureHandler structureHandler) {
        String gravatarId = MessageDigestUtil.md5(attributeValue);
        String gravatarUrl = "https://secure.gravatar.com/avatar/" + gravatarId;

        IModelFactory modelFactory = context.getModelFactory();
        model.replace(0, modelFactory.createOpenElementTag("img", "src", gravatarUrl));
        model.replace(model.size() - 1, modelFactory.createCloseElementTag("img"));
    }

}
