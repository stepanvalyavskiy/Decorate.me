package ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.impl;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReferenceExpression;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;
import ede.decorate.me.decoratablePsiExpressions.impl.DecoratablePsiNewExpression;
import ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.PatternToDecoratablePsiExpression;

import static com.intellij.patterns.PlatformPatterns.psiElement;

@SuppressWarnings("unused")
public final class NewExpression implements PatternToDecoratablePsiExpression {

    @Override
    public PsiElementPattern.Capture<PsiIdentifier> pattern() {
        return psiElement(PsiIdentifier.class)
                .withParent(psiElement(PsiReferenceExpression.class)
                        .withChild(psiElement(PsiNewExpression.class)));
    }

    @Override
    public DecoratablePsiExpression psiExpression(PsiElement position) {
        return new DecoratablePsiNewExpression(position);
    }
}
