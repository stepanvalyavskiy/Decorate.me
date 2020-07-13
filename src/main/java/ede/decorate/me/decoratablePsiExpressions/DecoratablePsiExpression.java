package ede.decorate.me.decoratablePsiExpressions;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

public interface DecoratablePsiExpression {
    PsiElement content();
    PsiClass myClass();
    PsiType[] myTypes();
}
