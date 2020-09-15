package com.alibaba.p3c.pmd.lang.java.rule.other;

import com.alibaba.p3c.pmd.I18nResources;
import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;
import com.alibaba.p3c.pmd.lang.java.util.ViolationUtils;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTTypeArgument;
import net.sourceforge.pmd.lang.java.ast.ASTTypeParameters;

/**
 * @desc: 方法参数太多 <br/>
 * @time: 2020/9/3 11:41<br/>
 * @author: Cauchy <br/>
 * @since: V1.0 <br/>
 */
public class MethodParameterTooMuchRule extends AbstractAliRule {

    private static final int MAX_PARAMETER_COUNT = 5;


    @Override
    public Object visit(ASTTypeParameters node, Object data) {
        if (node.jjtGetNumChildren() > MAX_PARAMETER_COUNT) {
            ViolationUtils.addViolationWithPrecisePosition(this, node, data,
                    I18nResources.getMessage("java.other.AvoidNewDateGetTimeRule.violation.msg"));
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTFormalParameters node, Object data) {
        if (node.jjtGetNumChildren() > MAX_PARAMETER_COUNT) {
            ViolationUtils.addViolationWithPrecisePosition(this, node, data,
                    I18nResources.getMessage("java.other.AvoidNewDateGetTimeRule.violation.msg"));
        }
        return super.visit(node, data);
    }


}
