package com.alibaba.p3c.pmd.lang.java.rule.xml;

import com.alibaba.p3c.pmd.I18nResources;
import com.sun.org.apache.xerces.internal.dom.AttributeMap;
import com.sun.org.apache.xerces.internal.dom.DeferredAttrNSImpl;
import com.sun.org.apache.xerces.internal.dom.DeferredTextImpl;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.xml.ast.XmlNode;
import net.sourceforge.pmd.lang.xml.ast.XmlNodeWrapper;
import net.sourceforge.pmd.lang.xml.rule.AbstractXmlRule;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * desc: 颜色文件命名规则 <br/>
 * time: 2020/9/14 15:33<br/>
 * author: Cauchy <br/>
 * since: V1.0 <br/>
 */
public class ColorNameRule extends AbstractXmlRule {

    //颜色命名正则
    private Pattern colorNamePattern = Pattern.compile("^(c_|m_base_c_)(([A-Fa-f0-9]{6})|([A-Fa-f0-9]{8}))");

    public ColorNameRule() {

    }

    @Override
    protected void visit(XmlNode node, RuleContext ctx) {
        XmlNodeWrapper xmlNodeWrapper = (XmlNodeWrapper) node;

        if ("color".equals(xmlNodeWrapper.getLocalName())) {
            AttributeMap attributeMap = (AttributeMap) xmlNodeWrapper.getAttributes();
            DeferredAttrNSImpl nameNode = (DeferredAttrNSImpl) attributeMap.getNamedItem("name");
            if (colorNamePattern.matcher(nameNode.getValue()).find()) {
                String[] nameArray = nameNode.getValue().split("_");
                DeferredTextImpl valueNode = (DeferredTextImpl) xmlNodeWrapper.getFirstChild();
                if (StringUtils.isEmpty(valueNode.getData()) || !nameArray[nameArray.length - 1]
                        .equalsIgnoreCase(valueNode.getData().substring(1))) {
                    addViolationWithMessage(ctx, node
                            , I18nResources.getMessage("xml.naming.ColorNameRule.violation.msg", nameNode.getValue()));
                }
            } else {
                addViolationWithMessage(ctx, node
                        , I18nResources.getMessage("xml.naming.ColorNameRule.violation.msg", nameNode.getValue()));
            }
        }

        super.visit(node, ctx);
    }
}
