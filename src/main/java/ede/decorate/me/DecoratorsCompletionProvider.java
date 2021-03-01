package ede.decorate.me;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.util.ProcessingContext;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;
import ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.PatternToDecoratablePsiExpression;
import ede.decorate.me.streamable.impl.VIsibleConstructorsWithParameters;
import ede.decorate.me.streamable.impl.Decorators;
import ede.decorate.me.streamable.impl.SuperTypesOf;
import org.jetbrains.annotations.NotNull;

public final class DecoratorsCompletionProvider extends CompletionProvider<CompletionParameters> {

    private final PatternToDecoratablePsiExpression expressionToDecorate;

    public DecoratorsCompletionProvider(PatternToDecoratablePsiExpression expressionToDecorate) {
        this.expressionToDecorate = expressionToDecorate;
    }

    /**
     * The method finds all decorators for given expression and provides them to completion list.
     * <p>
     * {@link #expressionToDecorate} - given expression
     *
     * @param completionResultSet - subset of completion list.
     */
    @Override
    protected void addCompletions(@NotNull CompletionParameters completionParameters, @NotNull ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
        DecoratablePsiExpression psiExpressionToDecorate = expressionToDecorate.psiDecoratableExpression(completionParameters.getPosition());
        new Decorators(
                psiExpressionToDecorate.content(),
                new VIsibleConstructorsWithParameters(
                        new SuperTypesOf(
                                psiExpressionToDecorate.myClass(),
                                psiExpressionToDecorate.myTypes()
                        ),
                        completionParameters.getEditor().getProject(),
                        completionParameters.getPosition()
                )
        ).flush(completionResultSet);
    }
}
