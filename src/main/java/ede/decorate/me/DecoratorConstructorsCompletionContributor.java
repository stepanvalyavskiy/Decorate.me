package ede.decorate.me;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiNewExpression;
import ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.PatternToDecoratablePsiExpression;
import ede.decorate.me.dependencyManagment.InheritorsInstances;

/**
 * Completion contributor is needed to register completion providers in Intellij IDEA.
 */
public class DecoratorConstructorsCompletionContributor extends CompletionContributor {

    /**
     *  The preferred way to implement code completion is to provide constructor in a contributor and register completion providers there:
     *  {@link CompletionContributor#extend(CompletionType, ElementPattern, CompletionProvider)}.
     *
     *  I can't supply dependencies through the class's constructor
     *  because only NoArgsConstructor is allowed here to be automatically registered in Intellij IDEA as {@link CompletionContributor} inheritor.
     *
     *  To avoid tight coupling and follow SOLID Principles,
     *  I have done this {@link InheritorsInstances} handmade IOC.
     *
     *  The Decorators' completion is shown as an example after {@link PsiNewExpression}.
     *  ({@link PsiNewExpression} corresponds to Java Constructor.)
     *
     *  To implement Decorators' completion after other expressions (after method call or after reference to object)
     *  you shouldn't create yet another CompletionContributor or CompletionProvider.
     *  Just provide correspond implementation of {@link PatternToDecoratablePsiExpression} and it will be automatically registered here.
     *
     *  {@link DecoratorConstructorsCompletionProvider} is also designed to be able to work after other expressions.
     */
    public DecoratorConstructorsCompletionContributor() {
        Iterable<PatternToDecoratablePsiExpression> expressionsToDecorate = new InheritorsInstances<>(PatternToDecoratablePsiExpression.class);
        expressionsToDecorate.forEach(
                expressionToDecorate ->
                        extend(CompletionType.SMART,
                                expressionToDecorate.pattern(),
                                new DecoratorConstructorsCompletionProvider(expressionToDecorate)
                        )
        );
    }
}
