package ede.decorate.me.streamable.impl;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.jvm.JvmParameter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiUtil;
import ede.decorate.me.lookupDecorator.impl.DecoratorExpression;
import ede.decorate.me.lookupDecorator.impl.SharedDecoratorExpression;
import ede.decorate.me.streamable.Streamable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Decorators implements Streamable<LookupElementBuilder> {
    private final PsiElement content;
    private final Streamable<ConstructorsWithParameters.CtorToIfc> constructors;

    public Decorators(PsiElement content, Streamable<ConstructorsWithParameters.CtorToIfc> constructors) {
        this.content = content;
        this.constructors = constructors;
    }

    @Override
    public Stream<LookupElementBuilder> stream() {
        return constructors
                .stream()
                .flatMap(ctorToIfc -> decoratorsOf(ctorToIfc.ctor, ctorToIfc.ifc));
    }

    public void flush(CompletionResultSet completionResultSet) {
        stream().forEach(completionResultSet::addElement);
    }

    private Stream<LookupElementBuilder> decoratorsOf(PsiMethod ctor, PsiClass parentInterface) {
        List<Integer> indexes = indexes(parentInterface, ctor.getParameters());
        if (indexes.size() == 1) {
            return Stream.of(
                    new DecoratorExpression(
                            content.getText(),
                            content.getClass(),
                            ctor,
                            parentInterface,
                            indexes.get(0)
                    ).lookupElement()
            );
        } else {
            return indexes.stream()
                          .map(index ->
                                  new SharedDecoratorExpression(
                                          content.getText(),
                                          content.getClass(),
                                          ctor,
                                          parentInterface,
                                          index
                                  ).lookupElement()
                          );
        }
    }

    @NotNull
    private List<Integer> indexes(PsiClass parentInterface, JvmParameter[] parameters) {
        return IntStream.range(0, parameters.length)
                        .filter(i -> parentInterface
                                .isEquivalentTo(
                                        PsiUtil.resolveClassInType(
                                                (PsiType) parameters[i].getType()
                                        )
                                )
                        )
                        .boxed()
                        .collect(Collectors.toList());
    }

}