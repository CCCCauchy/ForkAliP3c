package com.alibaba.p3c.pmd.lang.java.rule.naming.android;

import com.alibaba.p3c.pmd.I18nResources;
import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;
import com.alibaba.p3c.pmd.lang.java.util.ViolationUtils;
import net.sourceforge.pmd.lang.java.ast.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * desc: 变量名称 规范 <br/>
 * time: 2020/9/8 21:01<br/>
 * author: Cauchy <br/>
 * since: V1.0 <br/>
 */
public class AndroidPropertyShouldEndWithRule extends AbstractAliRule {

    private static final String LIST_END_SUFFIX = "List";
    private static final String MAP_END_SUFFIX = "Map";
    private static final String SET_END_SUFFIX = "Set";
    private static final String SA_END_SUFFIX = "Sa";
    private static final String ARRAY_END_SUFFIX = "Array";

    private static final String SPARSE_ARRAY = "SparseArray";
    private static final String SPARSE_BOOLEAN_ARRAY = "SparseBooleanArray";
    private static final String SPARSE_INT_ARRAY = "SparseIntArray";
    private static final String SPARSE_LONG_ARRAY = "SparseLongArray";

    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {
        processAndroidFieldNaming(node, data, LIST_END_SUFFIX, LIST_END_SUFFIX, "java.naming.ListPropertyShouldEndWithRule.violation.msg");
        processAndroidFieldNaming(node, data, MAP_END_SUFFIX, MAP_END_SUFFIX, "java.naming.MapPropertyShouldEndWithRule.violation.msg");
        processAndroidFieldNaming(node, data, SET_END_SUFFIX, SET_END_SUFFIX, "java.naming.SetPropertyShouldEndWithRule.violation.msg");
        processAndroidFieldNaming(node, data, SPARSE_ARRAY, SA_END_SUFFIX, "java.naming.SparseArrayPropertyShouldEndWithRule.violation.msg");
        processAndroidFieldNaming(node, data, SPARSE_BOOLEAN_ARRAY, SA_END_SUFFIX, "java.naming.SparseArrayPropertyShouldEndWithRule.violation.msg");
        processAndroidFieldNaming(node, data, SPARSE_INT_ARRAY, SA_END_SUFFIX, "java.naming.SparseArrayPropertyShouldEndWithRule.violation.msg");
        processAndroidFieldNaming(node, data, SPARSE_LONG_ARRAY, SA_END_SUFFIX, "java.naming.SparseArrayPropertyShouldEndWithRule.violation.msg");
        processArrayFieldNaming(node, data);
        return super.visit(node, data);
    }


    private void processAndroidFieldNaming(TypeNode node, Object data, String fieldClass, String classEndSuffix, String message) {
        boolean notMatched = StringUtils.isEmpty(node.getImage())
                || !node.getImage().endsWith(classEndSuffix);
        ASTFieldDeclaration astFieldDeclaration = node.getFirstParentOfType(ASTFieldDeclaration.class);
        if (astFieldDeclaration != null && isChild(astFieldDeclaration, fieldClass) && notMatched) {
            ViolationUtils.addViolationWithPrecisePosition(this, node, data,
                    I18nResources.getMessage(message, node.getImage()));
        }

        ASTLocalVariableDeclaration astLocalVariableDeclaration = node.getFirstParentOfType(ASTLocalVariableDeclaration.class);
        if (astLocalVariableDeclaration != null && isChild(astLocalVariableDeclaration, fieldClass) && notMatched) {
            ViolationUtils.addViolationWithPrecisePosition(this, node, data,
                    I18nResources.getMessage(message, node.getImage()));
        }
    }

    private void processArrayFieldNaming(TypeNode node, Object data) {
        boolean notMatched = StringUtils.isEmpty(node.getImage())
                || !node.getImage().endsWith(ARRAY_END_SUFFIX);
        ASTFieldDeclaration astFieldDeclaration = node.getFirstParentOfType(ASTFieldDeclaration.class);
        if (astFieldDeclaration != null && astFieldDeclaration.isArray() && notMatched) {
            ViolationUtils.addViolationWithPrecisePosition(this, node, data,
                    I18nResources.getMessage("java.naming.ArrayPropertyShouldEndWithRule.violation.msg", node.getImage()));
        }
        ASTLocalVariableDeclaration astLocalVariableDeclaration = node.getFirstParentOfType(ASTLocalVariableDeclaration.class);
        if (astLocalVariableDeclaration != null && astLocalVariableDeclaration.isArray() && notMatched) {
            ViolationUtils.addViolationWithPrecisePosition(this, node, data,
                    I18nResources.getMessage("java.naming.ArrayPropertyShouldEndWithRule.violation.msg", node.getImage()));
        }
    }

    /**
     * 判断是否为某个类的子类
     *
     * @param clazzEndSuffix 父类名称后缀
     */
    private boolean isChild(AbstractJavaAccessNode parentNode, @NotNull String clazzEndSuffix) {
        if (parentNode.jjtGetFirstToken().getImage() != null) {
            return clazzEndSuffix.equals(parentNode.jjtGetFirstToken().getImage()) ||
                    parentNode.jjtGetFirstToken().getImage().endsWith(clazzEndSuffix);
        }
        return false;
    }


}
