package decorate.me.psiExpressions;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

public interface PsiExpressionToDecorate {
    PsiElement content();
    PsiClass myClass();
    PsiType[] myTypes();
}
