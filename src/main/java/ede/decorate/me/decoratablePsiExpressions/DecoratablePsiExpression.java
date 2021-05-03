package ede.decorate.me.decoratablePsiExpressions;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;

public interface DecoratablePsiExpression {
    PsiElement content();
    PsiClass myClass();
    PsiType[] myTypes();
    default TreeElement replaceableRefExp() {
        return (TreeElement)
                PsiTreeUtil.getParentOfType(
                        content(),
                        PsiReferenceExpression.class
                );
    }

    default PsiType myType() {
        return ((PsiExpression) content()).getType();
    }

    default PsiClass leftType() {
        return null;
    }
}
