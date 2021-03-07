package ede.decorate.me.decoratablePsiExpressions;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;

public interface DecoratablePsiExpression {
    PsiElement content();
    PsiClass myClass();
    PsiType[] myTypes();
    //TODO default methods are shit
    default TreeElement replaceableRefExp() {
        return (TreeElement)
                PsiTreeUtil.getParentOfType(
                        content(),
                        PsiReferenceExpression.class
                );
    }
}
