package ede.decorate.me.streamable.impl;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.util.PsiUtil;
import ede.decorate.me.lookupDecorator.impl.DecoratorExpression;
import ede.decorate.me.streamable.Streamable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Decorators implements Streamable<LookupElementBuilder> {
    private final PsiType psiType;
    private final TreeElement replaceableRefExp;
    private final PsiElement content;
    private final Streamable<VIsibleConstructorsWithParameters.ConstructorToSuperType> constructors;

    public Decorators(PsiType psiType, TreeElement replaceableRefExp, PsiElement content, Streamable<VIsibleConstructorsWithParameters.ConstructorToSuperType> constructors) {
        this.psiType = psiType;
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

    private Stream<LookupElementBuilder> decoratorsOf(VIsibleConstructorsWithParameters.ConstructorToSuperType ctorToSuperType) {//ConstructorToSuperType
        final PsiMethod ctor = ctorToSuperType.ctor;
        final PsiClass superType = ctorToSuperType.superType;
        final PsiClass decoratorType = ctorToSuperType.myType;
        List<Integer> indexes = indexesOfSuperTypeInCtrParametersList(superType, ctor.getParameterList().getParameters());
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
    private List<Integer> indexesOfSuperTypeInCtrParametersList(PsiClass parentInterface, PsiParameter[] parameters) {
        return IntStream.range(0, parameters.length)
                        .filter(i -> parentInterface
                                .isEquivalentTo(
                                        PsiUtil.resolveClassInType(
                                                parameters[i].getType()
                                        )
                                )
                        ).filter(i -> {
                    PsiType type = parameters[i].getType();
                    if (type instanceof PsiEllipsisType) {
                        //TODO to deep or not to deep
                        type = type.getDeepComponentType();
                    }
                    PsiType[] generics = ((PsiClassReferenceType) type).getParameters();
                    if (generics.length == 0) {
                        return true;
                    }
                    //TODO{PRIO-2} iterate and replace all PsiTypeParameter & PsiWildcard with java.lang.Object
                    PsiType next = generics[0];
                    PsiClass resolve;
                    if (next instanceof PsiWildcardType) {
                        resolve = ((PsiClassReferenceType) ((PsiWildcardType) next).getBound()).resolve();
                    } else {
                        //TODO{PRIO-1} idk what types are present -> want to have ClassCastException here.
                        try {
                            resolve = ((PsiClassReferenceType) next).resolve();
                        } catch (ClassCastException e) {
                            System.out.println("CLASS CAST:" + e);
                            return false;
                        }
                    }
                    if (resolve instanceof PsiTypeParameter) {
                        return true;
                    }
                    return !GenericsUtil
                            .checkNotInBounds(
                                    psiType,
                                    type,
                                    false
                            );
                })
                .boxed()
                .collect(Collectors.toList());
    }
}
