package decorate.me.psiExpressions;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

public interface PsiExpressionToDecorate {
    PsiElement content();
    PsiClass myClass();
}
