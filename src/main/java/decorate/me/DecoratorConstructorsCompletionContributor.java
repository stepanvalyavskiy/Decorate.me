package decorate.me;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import decorate.me.expressions.ExpressionToDecorate;

public class DecoratorConstructorsCompletionContributor extends CompletionContributor {

    public DecoratorConstructorsCompletionContributor() {
        Iterable<ExpressionToDecorate> expressionsToDecorate = new InheritorsInstances<>(ExpressionToDecorate.class);
        expressionsToDecorate.forEach(
                expressionToDecorate ->
                        extend(CompletionType.SMART,
                                expressionToDecorate.pattern(),
                                new DecoratorConstructorsCompletionProvider(expressionToDecorate)
                        )
        );
    }
}
