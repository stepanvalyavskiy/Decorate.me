package decorate.me;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.util.ProcessingContext;
import decorate.me.expressions.ExpressionToDecorate;
import decorate.me.psiExpressions.PsiExpressionToDecorate;
import decorate.me.streamable.impl.ConstructorsWithParameters;
import decorate.me.streamable.impl.Decorators;
import decorate.me.streamable.impl.ImplementedInterfaces;
import org.jetbrains.annotations.NotNull;

class DecoratorConstructorsCompletionProvider extends CompletionProvider<CompletionParameters> {

    private final ExpressionToDecorate expressionToDecorate;

    public DecoratorConstructorsCompletionProvider(ExpressionToDecorate expressionToDecorate) {
        this.expressionToDecorate = expressionToDecorate;
    }


    @Override
    protected void addCompletions(@NotNull CompletionParameters completionParameters, @NotNull ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
        PsiExpressionToDecorate psiExpressionToDecorate = expressionToDecorate.psiExpression(completionParameters);
                new Decorators(
                        psiExpressionToDecorate.content(),
                        new ConstructorsWithParameters(
                                new ImplementedInterfaces(psiExpressionToDecorate.myClass()),
                                completionParameters.getEditor().getProject()
                        )
                ).flush(completionResultSet);
    }
}
