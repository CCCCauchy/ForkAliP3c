/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.p3c.pmd.lang.java.rule.comment;

import com.alibaba.p3c.pmd.I18nResources;
import com.alibaba.p3c.pmd.lang.java.rule.util.NodeSortUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;

import java.util.*;

/**
 * [Mandatory] Single line comments in a method should be put above the code to be commented, by using // and
 * multiple lines by using \/* *\/. Alignment for comments should be noticed carefully.
 *
 * @date 2016/12/14
 */
public class AvoidCommentBehindStatementRule extends AbstractAliCommentRule {

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {
        SortedMap<Integer, Node> itemsByLineNumber = orderedCommentsAndExpressions(cUnit);
        AbstractJavaNode lastNode = null;

        Iterator<Node> valueIterator = itemsByLineNumber.values().iterator();
        while (valueIterator.hasNext()) {
            Node value = valueIterator.next();
            if (value instanceof AbstractJavaNode) {
                lastNode = (AbstractJavaNode) value;

                //添加switch case必须添加行尾注释
                if (value.jjtGetParent() instanceof ASTSwitchLabel && valueIterator.hasNext()) {
                    Node nextValue = valueIterator.next();
                    if (!(nextValue instanceof Comment)) {
                        addViolationWithMessage(data, value,
                                I18nResources.getMessage("java.comment.AvoidCommentBehindStatementRule.violation.msg.case"),
                                value.getBeginLine(), value.getEndLine());
                    }
                }

            } else if (value instanceof Comment) {
                Comment comment = (Comment) value;
                if (lastNode != null && !(lastNode.jjtGetParent() instanceof ASTSwitchLabel)) {
                    if (comment.getBeginLine() == lastNode.getBeginLine()
                            && comment.getEndColumn() > lastNode.getBeginColumn()) {
                        addViolationWithMessage(data, lastNode,
                                I18nResources.getMessage("java.comment.AvoidCommentBehindStatementRule.violation.msg"),
                                comment.getBeginLine(), comment.getEndLine());
                    }
                }
            }
        }

        return super.visit(cUnit, data);
    }

    /**
     * Check comments behind nodes.
     *
     * @param cUnit compilation unit
     * @return sorted comments and expressions
     */
    protected SortedMap<Integer, Node> orderedCommentsAndExpressions(ASTCompilationUnit cUnit) {

        SortedMap<Integer, Node> itemsByLineNumber = new TreeMap<>();

        // expression nodes
        List<ASTExpression> expressionNodes = cUnit.findDescendantsOfType(ASTExpression.class);
        List<ASTExpression> notSwitchNodes = new ArrayList<>();
        for (int i = 0; i < expressionNodes.size(); i++) {
            if (expressionNodes.get(i).jjtGetParent() instanceof ASTSwitchLabel) {
                expressionNodes.get(i).comment();
//                continue;
            }
            notSwitchNodes.add(expressionNodes.get(i));
        }
        NodeSortUtils.addNodesToSortedMap(itemsByLineNumber, notSwitchNodes);

        // filed declaration nodes
        List<ASTFieldDeclaration> fieldNodes =
                cUnit.findDescendantsOfType(ASTFieldDeclaration.class);
        NodeSortUtils.addNodesToSortedMap(itemsByLineNumber, fieldNodes);

        // enum constant nodes
        List<ASTEnumConstant> enumConstantNodes =
                cUnit.findDescendantsOfType(ASTEnumConstant.class);
        NodeSortUtils.addNodesToSortedMap(itemsByLineNumber, enumConstantNodes);

        NodeSortUtils.addNodesToSortedMap(itemsByLineNumber, cUnit.getComments());

        return itemsByLineNumber;
    }

}
