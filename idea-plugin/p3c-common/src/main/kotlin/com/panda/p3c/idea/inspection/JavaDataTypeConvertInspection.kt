package com.panda.p3c.idea.inspection

import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil

/**
 * desc:  java代码 类型转换检测 <br/>
 * time: 2019/10/13 9:49 <br/>
 * author: Coffee <br/>
 * since V 1.0 <br/>
 */
class JavaDataTypeConvertInspection : LocalInspectionTool, AliBaseInspection {
    constructor()

    /**
     * For Javassist
     */
    constructor(any: Any?) : this()

    override fun ruleName(): String = "JavaDataTypeConvertRule"

    override fun getDisplayName(): String {
        return "数字类型与字符类型转换检测"
    }

    override fun getDefaultLevel(): HighlightDisplayLevel {
        return HighlightDisplayLevel.ERROR
    }

    override fun getStaticDescription(): String? {
        return "数字类型与字符类型转换检测\n\n " +
                "错误的例子：\n " +
                "double longitude =  Double.parseDouble(\"23.232233\") \n" +
                "正确的例子： \n" +
                "double longitude =  ToolNumber.toDouble(\"23.232233\")";
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession)
            : PsiElementVisitor {

        return object : JavaElementVisitor() {

            override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
                super.visitMethodCallExpression(expression)

                if (expression == null) {
                    return
                }
                if (!checkMethodIsDataConvert(expression)) {
                    return
                }
                val tryElement = PsiTreeUtil.getParentOfType(expression, PsiTryStatement::class.java)

                if (!tryElement.catchesNumberFormatException()) {
                    holder.registerProblem(expression, "可能会报NumberFormatException异常,请优先使用 " +
                            "ToolNumber 里的方法替代")
                }

            }
        }
    }

    /**
     * 判断Java代码中是不是类型转换的方法
     */
    private fun checkMethodIsDataConvert(expression: PsiExpression): Boolean {
        val text = expression.text


        if (
                text.contains("Byte.parseByte(")
                || text.contains("Short.parseShort(")
                || text.contains("Integer.parseInt(")
                || text.contains("Float.parseFloat(")
                || text.contains("Long.parseLong(")
                || text.contains("Double.parseDouble(")

                || text.contains("Byte.valueOf(")
                || text.contains("Short.valueOf(")
                || text.contains("Float.valueOf(")
                || text.contains("Long.valueOf(")
                || text.contains("Double.valueOf(")
        ) {
            return true
        } else if (text.contains("Integer.valueOf(")) {
            val index = text.indexOf("Integer.valueOf(");
            return index > 0 && !text.get(index - 1).isLetterOrDigit()
        }
        return false
    }
}

fun PsiTryStatement?.catchesNumberFormatException(): Boolean {
    if (this == null) {
        return false
    }
    val sections = this.catchSections
    for (section in sections) {
        val catchType = section.catchType
        if (catchType != null) {
            val typeText = catchType.canonicalText
            if (
                    "java.lang.NumberFormatException" == typeText
                    || "java.lang.Exception" == typeText
                    || "java.lang.IllegalArgumentException" == typeText
                    || "java.lang.RuntimeException" == typeText) {
                return true
            }
        }
    }
    return false
}
