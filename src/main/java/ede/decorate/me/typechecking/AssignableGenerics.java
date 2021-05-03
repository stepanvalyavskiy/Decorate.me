package ede.decorate.me.typechecking;

import com.intellij.openapi.project.Project;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.TypeConversionUtil;

import java.util.function.BiPredicate;


public class AssignableGenerics implements BiPredicate<PsiType, PsiTypeParameter[]> {

    private final PsiType decoratedType;
    private final PsiResolveHelper resolveHelper;
    private final LanguageLevel languageLevel;

    public AssignableGenerics(PsiType decoratedType, Project project) {
        this.decoratedType = decoratedType;
        this.resolveHelper = JavaPsiFacade.getInstance(project).getResolveHelper();
        this.languageLevel = PsiUtil.getLanguageLevel(project);
    }

    @Override
    public boolean test(final PsiType candidateType, final PsiTypeParameter[] typeParameters) {
        return TypeConversionUtil.isAssignable(
                resolveHelper.inferTypeArguments(
                        typeParameters,
                        new PsiType[]{ candidateType },
                        new PsiType[]{ decoratedType },
                        languageLevel
                ).substitute(candidateType),
                decoratedType
        );
    }
}
