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
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project

/**
 *
 *
 * @author caikang
 * @date 2017/01/26
 */
object VmQuietReferenceQuickFix : AliQuickFix {
    override val onlyOnThFly: Boolean
        get() = true

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val textRange = descriptor.textRangeInElement ?: return
        val document = FileDocumentManager.getInstance().getDocument(
                descriptor.startElement.containingFile.virtualFile) ?: return
        document.insertString(textRange.startOffset + 1, "!")
    }

    override val ruleName = "UseQuietReferenceNotationRule"

    override fun getName(): String {
        return "为变量添加!"
    }

}
