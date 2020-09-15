package com.panda.p3c.idea.quickfix

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiIdentifier
import com.panda.p3c.idea.i18n.P3cBundle

/**
 * @desc: Android常用变量后缀处理  <br/>
 * @time: 2020/9/10 19:17<br/>
 * @author: Cauchy <br/>
 * @since: V1.0 <br/>
 */
object AndroidClassSuffixQuickFix : AliQuickFix {


    override val ruleName: String
        get() = "AndroidClassShouldEndWithRule"
    override val onlyOnThFly: Boolean
        get() = true

    override fun getName(): String = P3cBundle.getMessage("com.panda.p3c.idea.quickfix.AndroidPropertyShouldEndWithRule")

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val psiIdentifier = descriptor.psiElement as? PsiIdentifier ?: return
        val identifier = psiIdentifier.text
        AliQuickFix.doQuickFix(identifier, project, psiIdentifier)
    }

}