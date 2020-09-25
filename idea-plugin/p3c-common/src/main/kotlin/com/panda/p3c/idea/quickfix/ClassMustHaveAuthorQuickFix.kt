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
package com.panda.p3c.idea.quickfix

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.impl.source.javadoc.PsiDocCommentImpl
import com.intellij.psi.javadoc.PsiDocToken
import com.panda.p3c.idea.i18n.P3cBundle
import com.siyeh.ig.InspectionGadgetsFix
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 *
 * @author caikang
 * @date 2017/02/27
 */
object ClassMustHaveAuthorQuickFix : InspectionGadgetsFix(), AliQuickFix {

    val authorName = System.getProperty("user.name") ?: System.getenv("USER")
    val timeString = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date())
    val authorTag = "author: ${authorName} </br>"
    val timeTag = "time: $timeString </br>"

    val defaultComment = """
/**
 * desc: TODO: $authorName </br>
 * $timeTag
 * $authorTag
 * since: TODO: $authorName </br>
 */
"""

    override fun doFix(project: Project?, descriptor: ProblemDescriptor?) {
        descriptor ?: return
        val psiClass = descriptor.psiElement as? PsiClass ?: descriptor.psiElement?.parent as? PsiClass ?: return

        val document = psiClass.docComment
        val psiFacade = JavaPsiFacade.getInstance(project)
        val factory = psiFacade.elementFactory
        if (document == null) {
            val doc = factory.createDocCommentFromText(defaultComment)
            if (psiClass.isEnum) {
                psiClass.containingFile.addAfter(doc, psiClass.prevSibling)
            } else {
                psiClass.addBefore(doc, psiClass.firstChild)
            }
            return
        }

        val regex = Regex("Created by (.*) on (.*)\\.")
        for (line in document.descriptionElements) {
            if (line is PsiDocToken && line.text.contains(regex)) {
                val groups = regex.find(line.text)?.groups ?: continue
                val author = groups[1]?.value ?: continue
                val date = groups[2]?.value ?: continue
                line.delete()
                val doc = factory.createDocCommentFromText("""
/**
 * desc: TODO: $authorName </br>
 * time: $date </br>
 * author: ${author} </br>
 * since: TODO: $authorName </br>
 */
""")
                psiClass.addBefore(doc,psiClass.firstChild)
                document.delete()

                return
            }
        }

        val doc = factory.createDocCommentFromText(defaultComment)
        psiClass.addBefore(doc,psiClass.firstChild)
        document.delete()
    }

    override val ruleName: String
        get() = "ClassMustHaveAuthorRule"
    override val onlyOnThFly: Boolean
        get() = true

    override fun getName(): String {
        return P3cBundle.getMessage("com.alibaba.p3c.idea.quickfix.generate.author")
    }

}
