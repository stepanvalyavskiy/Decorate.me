package ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression;

import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;

/**
 * This class is a pair of pattern and corresponding DecoratablePsiExpression's implementation.
 * This is an abstract factory.
 *
 * This interface has been separated from {@link DecoratablePsiExpression}
 * because {@link ElementPattern} is needed before it is possible to fully initialize a {@link DecoratablePsiExpression} instance.
 * It allows you to pass all dependencies through {@link DecoratablePsiExpression}'s constructor and avoid setters and nulls.
 */
public interface PatternToDecoratablePsiExpression {
    /**
     * @return pattern that is used to realise whether completion was called at the relevant position.
     * You stay after dot and should check whether the expression to the left could be wrapped.
     * If expression return an object (ctor, method or reference), it could potentially be wrapped.
     */
    ElementPattern<? extends PsiElement> pattern();

    /**
     * For each expression we need to resolve its class and expression itself.
     * It differs for every expression type.
     * This Method provide corresponding implementation that can work with expression that matches to {@link #pattern()}
     *
     * @param position - the position where the completion was called.
     * @return expression to the left of current position that matches to {@link #pattern()}.
     */
    DecoratablePsiExpression psiDecoratableExpression(PsiElement position);
}
