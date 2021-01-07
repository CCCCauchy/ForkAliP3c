package com.alibaba.p3c.pmd.lang.java.rule.xml;

import com.alibaba.p3c.pmd.I18nResources;
import com.alibaba.p3c.pmd.lang.java.rule.AbstractPandaXmlRule;
import com.alibaba.p3c.pmd.lang.java.util.ViolationUtils;
import com.sun.org.apache.xerces.internal.dom.AttributeMap;
import com.sun.org.apache.xerces.internal.dom.DeferredAttrNSImpl;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.xml.ast.XmlNode;
import net.sourceforge.pmd.lang.xml.ast.XmlNodeWrapper;

import java.util.regex.Pattern;

/**
 * desc: 颜色文件命名规则 <br/>
 * time: 2020/9/14 15:33<br/>
 * author: Cauchy <br/>
 * since: V1.0 <br/>
 */
public class StringNameRule extends AbstractPandaXmlRule {

    //文案命名正则
    private Pattern stringNamePattern = Pattern.compile("^[a-z0-9]+[_a-z0-9]+[a-z0-9]");

    @Override
    protected void visit(XmlNode node, RuleContext ctx) {
        XmlNodeWrapper xmlNodeWrapper = (XmlNodeWrapper) node;

        if ("string".equals(xmlNodeWrapper.getLocalName())) {
            AttributeMap attributeMap = (AttributeMap) xmlNodeWrapper.getAttributes();
            DeferredAttrNSImpl nameNode = (DeferredAttrNSImpl) attributeMap.getNamedItem("name");
            if (!stringNamePattern.matcher(nameNode.getValue()).matches()) {
                String warn = I18nResources.getMessage("xml.naming.StringNameRule.violation.msg"
                        , nameNode.getValue());
                ViolationUtils.addViolationWithPrecisePosition(this, node, ctx, warn);
            }
        }

        super.visit(node, ctx);
    }
}
