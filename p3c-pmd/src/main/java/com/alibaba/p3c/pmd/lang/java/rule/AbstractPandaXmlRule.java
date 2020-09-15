package com.alibaba.p3c.pmd.lang.java.rule;

import com.alibaba.p3c.pmd.I18nResources;
import com.alibaba.p3c.pmd.fix.FixClassTypeResolver;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.xml.ast.XmlNode;
import net.sourceforge.pmd.lang.xml.rule.AbstractXmlRule;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc: xml规则基类 <br/>
 * @time: 2020/9/14 15:49<br/>
 * @author: Cauchy <br/>
 * @since: V1.0 <br/>
 */
public abstract class AbstractPandaXmlRule extends AbstractXmlRule {

    private static final Map<String, Boolean> TYPE_RESOLVER_MAP = new ConcurrentHashMap<>(16);

    private static final String EMPTY_FILE_NAME = "n/a";
    private static final String DELIMITER = "-";

    @Override
    protected void visit(XmlNode node, RuleContext ctx) {
        String sourceCodeFilename =  ctx.getSourceCodeFilename();
        // Do type resolve if file name is empty(unit tests).
        if (StringUtils.isBlank(sourceCodeFilename) || EMPTY_FILE_NAME.equals(sourceCodeFilename)) {
            return ;
        }

        // If file name is not empty, use filename + hashcode to identify a compilation unit.
        String uniqueId = sourceCodeFilename + DELIMITER + node.hashCode();
        if (!TYPE_RESOLVER_MAP.containsKey(uniqueId)) {
            TYPE_RESOLVER_MAP.put(uniqueId, true);
        }

        super.visit(node, ctx);
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(I18nResources.getMessage(description));
    }

    @Override
    public void setMessage(String message) {
        super.setMessage(I18nResources.getMessageWithExceptionHandled(message));
    }

    @Override
    public void addViolationWithMessage(Object data, Node node, String message) {
        super.addViolationWithMessage(data, node, I18nResources.getMessageWithExceptionHandled(message));
    }

    @Override
    public void addViolationWithMessage(Object data, Node node, String message, Object[] args) {
        super.addViolationWithMessage(data, node,
                String.format(I18nResources.getMessageWithExceptionHandled(message), args));
    }

}
