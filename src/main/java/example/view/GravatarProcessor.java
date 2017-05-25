package example.view;

import example.MessageDigestUtil;
import example.model.User;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.processor.element.*;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashMap;
import java.util.Map;

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
        IEngineConfiguration configuration = context.getConfiguration();
        IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
        IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
        User user = (User)expression.execute(context);

        String gravatarId = MessageDigestUtil.md5(user.getEmail());
        String gravatarUrl = "https://secure.gravatar.com/avatar/" + gravatarId;

        IModelFactory modelFactory = context.getModelFactory();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("src", gravatarUrl);
        attributes.put("alt", user.getName());
        IOpenElementTag imgTag = modelFactory.createOpenElementTag("img", attributes, AttributeValueQuotes.DOUBLE, false);
        model.replace(0, imgTag);
        model.remove(1);
        model.replace(model.size() - 1, modelFactory.createCloseElementTag("img"));
    }

}
