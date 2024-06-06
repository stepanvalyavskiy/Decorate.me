package ede.decorate.me.streamable.impl;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.util.PsiUtil;
import ede.decorate.me.lookupDecorator.impl.DecoratorExpression;
import ede.decorate.me.streamable.Streamable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Decorators implements Streamable<LookupElementBuilder> {
    private final BiPredicate<PsiType, PsiTypeParameter[]> generic;
    private final TreeElement replaceableRefExp;
    private final PsiElement content;
    private final Streamable<VisibleConstructorsWithParameters.ConstructorToSuperType> constructors;

    public Decorators(BiPredicate<PsiType, PsiTypeParameter[]> generic, TreeElement replaceableRefExp, PsiElement content, Streamable<VisibleConstructorsWithParameters.ConstructorToSuperType> constructors) {
        this.generic = generic;
        this.replaceableRefExp = replaceableRefExp;
        this.content = content;
        this.constructors = constructors;
    }

    @Override
    public Stream<LookupElementBuilder> stream() {
        return constructors
                .stream()
                .flatMap(this::decoratorsOf);
    }

    public void flush(CompletionResultSet completionResultSet) {
        stream().forEach(completionResultSet::addElement);
    }

    private Stream<LookupElementBuilder> decoratorsOf(VisibleConstructorsWithParameters.ConstructorToSuperType ctorToSuperType) {//ConstructorToSuperType
        final PsiMethod ctor = ctorToSuperType.ctor;
        final PsiClass superType = ctorToSuperType.superType;
        List<Integer> indexes = indexesOfSuperTypeInCtrParametersList(
                superType,
                ctor.getParameterList().getParameters(),
                Stream.of(
                        ctor.getTypeParameters(),
                        ctor.getContainingClass().getTypeParameters()
                )
                        .flatMap(Stream::of).toArray(PsiTypeParameter[]::new)
        );
        if (indexes.size() == 1) {
            return Stream.of(
                    new DecoratorExpression(
                            replaceableRefExp,
                            content.getText(),
                            ctor,
                            superType,
                            indexes.get(0)
                    ).lookupElement()
            );
        } else {
            return indexes.stream()
                    .map(index ->
                            new DecoratorExpression(
                                    replaceableRefExp,
                                    content.getText(),
                                    ctor,
                                    superType,
                                    index,
                                    name -> String.format("[%s]", name)
                            ).lookupElement()
                    );
        }
    }

    @NotNull
    private List<Integer> indexesOfSuperTypeInCtrParametersList(PsiClass parentInterface, PsiParameter[] parameters, PsiTypeParameter[] typeParameters) {
        return IntStream.range(0, parameters.length)
                        .filter(i -> parentInterface
                                .isEquivalentTo(
                                        PsiUtil.resolveClassInType(
                                                parameters[i].getType()
                                        )
                                )
                        ).filter(i ->
                        generic.test(parameters[i].getType(), typeParameters)
                )
                .boxed()
                .collect(Collectors.toList());
    }
}
