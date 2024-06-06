package ede.decorate.me;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.ElementPattern;
import ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.PatternToDecoratablePsiExpression;
import ede.decorate.me.dependencyManagment.Implementations;

/**
 * Completion contributor is needed to register completion providers in Intellij IDEA.
 */
public final class DecoratorsCompletionContributor extends CompletionContributor {

    /**
     * The preferred way to implement code completion
     * is to extend {@link CompletionContributor} and register completion providers in NoArgsConstructor via
     * {@link CompletionContributor#extend(CompletionType, ElementPattern, CompletionProvider)}.
     */
    public DecoratorsCompletionContributor() {
        new Implementations<>(PatternToDecoratablePsiExpression.class)
                .forEach(
                        expressionToDecorate ->
                                extend(CompletionType.SMART,
                                        expressionToDecorate.pattern(),
                                        new DecoratorsCompletionProvider(expressionToDecorate)
                                )
                );
    }
}
