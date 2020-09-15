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
package com.alibaba.p3c.pmd.lang.java.rule.naming.android;

import com.alibaba.p3c.pmd.I18nResources;
import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;
import com.alibaba.p3c.pmd.lang.java.rule.util.NodeUtils;
import com.alibaba.p3c.pmd.lang.java.util.ViolationUtils;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import org.apache.commons.lang3.StringUtils;

/**
 * desc: Activity 命名  <br/>
 * time: 2020/9/8 16:39 <br/>
 * author: Cauchy <br/>
 * since: V1.0 <br/>
 */
public class AndroidClassShouldEndWithRule extends AbstractAliRule {

    private static final String ACTIVITY_END_SUFFIX = "Activity";
    private static final String FRAGMENT_END_SUFFIX = "Fragment";
    private static final String FRAGMENT_DIALOG_END_SUFFIX = "FragmentDialog";
    private static final String ADAPTER_END_SUFFIX = "Adapter";
    private static final String RECEIVER_END_SUFFIX = "Receiver";
    private static final String SERVICE_END_SUFFIX = "Service";
    private static final String INTENT_SERVICE_END_SUFFIX = "IntentService";
    private static final String CONTENT_RESOLVER_END_SUFFIX = "Resolver";

    private static final String BROADCAST_RECEIVER = "BroadcastReceiver";
    private static final String CONTENT_RESOLVER = "ContentResolver";
    private static final String DIALOG_FRAGMENT = "DialogFragment";

    @Override
    public Object visit(ASTExtendsList node, Object data) {
        processAndroidClass(node, data, ACTIVITY_END_SUFFIX, ACTIVITY_END_SUFFIX, "java.naming.ActivityClassShouldEndWithActivityRule.violation.msg");
        processAndroidClass(node, data, FRAGMENT_END_SUFFIX, FRAGMENT_END_SUFFIX, "java.naming.FragmentClassShouldEndWithFragmentRule.violation.msg");
        processAndroidClass(node, data, FRAGMENT_DIALOG_END_SUFFIX, DIALOG_FRAGMENT, "java.naming.FragmentDialogClassShouldEndWithFragmentDialogRule.violation.msg");
        processAndroidClass(node, data, ADAPTER_END_SUFFIX, ADAPTER_END_SUFFIX, "java.naming.AdapterClassShouldEndWithAdapterRule.violation.msg");
        processAndroidClass(node, data, RECEIVER_END_SUFFIX, BROADCAST_RECEIVER, "java.naming.BroadcastReceiverClassShouldEndWithBroadcastReceiverRule.violation.msg");
        processAndroidClass(node, data, SERVICE_END_SUFFIX, SERVICE_END_SUFFIX, "java.naming.ServiceClassShouldEndWithServiceRule.violation.msg");
        processAndroidClass(node, data, INTENT_SERVICE_END_SUFFIX, INTENT_SERVICE_END_SUFFIX, "java.naming.IntentServiceClassShouldEndWithIntentServiceRule.violation.msg");
        processAndroidClass(node, data, CONTENT_RESOLVER_END_SUFFIX, CONTENT_RESOLVER, "java.naming.ContentResolverClassShouldEndWithContentResolverRule.violation.msg");
        return super.visit(node, data);
    }

    private void processAndroidClass(ASTExtendsList node, Object data, String classEndSuffix, String parentClass, String message) {
        ASTClassOrInterfaceType astClassOrInterfaceType = node.getFirstChildOfType(ASTClassOrInterfaceType.class);
        if ((astClassOrInterfaceType == null) || (!(NodeUtils.isChild(astClassOrInterfaceType, parentClass)))) {
            return;
        }
        ASTClassOrInterfaceDeclaration astClassOrInterfaceDeclaration = node.getFirstParentOfType(
                ASTClassOrInterfaceDeclaration.class);
        boolean isExceptionViolation = StringUtils.isNotEmpty(astClassOrInterfaceDeclaration.getImage())
                && !astClassOrInterfaceDeclaration.getImage().endsWith(classEndSuffix);
        if (isExceptionViolation) {
            ViolationUtils.addViolationWithPrecisePosition(this, astClassOrInterfaceDeclaration, data,
                    I18nResources.getMessage(message,
                            astClassOrInterfaceDeclaration.getImage()));
        }
    }
}
