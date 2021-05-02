package ede.decorate.me.lookupDecorator.impl;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.util.PsiTreeUtil;
import ede.decorate.me.icons.ElegantObjects;
import ede.decorate.me.lookupDecorator.LookupDecorator;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class DecoratorExpression implements LookupDecorator {

    private final TreeElement replaceableRefExp;
    private final String original;
    private final PsiMethod decorator;
    private final PsiClass parentInterface;
    private final Integer index;
    private final UnaryOperator<String> marker;

    public DecoratorExpression(TreeElement replaceableRefExp, String original, PsiMethod decorator, PsiClass parentInterface, Integer index) {
        this(replaceableRefExp, original, decorator, parentInterface, index, noMarkedString -> noMarkedString);
    }

    public DecoratorExpression(TreeElement replaceableRefExp, String original, PsiMethod decorator, PsiClass parentInterface, Integer index, UnaryOperator<String> marker) {
        this.replaceableRefExp = replaceableRefExp;
        this.original = original;
        this.decorator = decorator;
        this.parentInterface = parentInterface;
        this.index = index;
        this.marker = marker;
    }

    @Override
    public final LookupElementBuilder lookupElement() {
        return LookupElementBuilder.create(expression())
                                   .withPsiElement(decorator)
                                   .withTailText(parameters(this::typeAndName))
                                   .withTypeText(parentInterface())
                                   .withPresentableText(name())
                                   .withLookupString(name())
                                   .withLookupString("wrap " + name())
                                   .withIcon(ElegantObjects.CACTOOS)
                                   .withInsertHandler((context, item) -> {
                                       //TODO{PRIO-0} cursor position & selected all empty elements
                                       WriteCommandAction.runWriteCommandAction(context.getProject(), () -> {
                                           deleteOriginalExpression(context, replaceableRefExp.getStartOffset());
                                           PsiElement decoratorExpression = PsiTreeUtil.getParentOfType(
                                                   context.getFile().getViewProvider().findElementAt(replaceableRefExp.getStartOffset()),
                                                   PsiNewExpression.class
                                           );
                                           JavaCodeStyleManager.getInstance(context.getProject()).shortenClassReferences(decoratorExpression);
                                       });
                                   });
    }

    private void deleteOriginalExpression(InsertionContext context, int originalExpressionStartOffset) {
        context.getDocument().deleteString(originalExpressionStartOffset, context.getStartOffset());
        PsiDocumentManager.getInstance(context.getProject())
                          .commitDocument(context.getDocument());
    }

    private String typeAndName(int index, PsiParameter parameter) {
        String name = parameter.getText();
        if (index == this.index) {
            name =  marker.apply(name);
        }
        return name;
    }

    private String expression() {
        Optional<PsiClass> psiClass =
                Optional.of(decorator)
                        .map(PsiMethod::getContainingClass);
        boolean hasTypeParameters =
                psiClass
                        .map(PsiClass::hasTypeParameters)
                        .orElse(false);
        return psiClass
                .map(PsiClass::getQualifiedName)
                .map(className ->
                        String.format(
                                "new %s%s%s",
                                className,
                                hasTypeParameters ? "<>" : "",
                                parameters(this::typeAndNameDummy))
                )
                .orElse("");
    }

    private String parameters(BiFunction<Integer, PsiParameter, String> typeAndName) {
        PsiParameter[] parameters = decorator.getParameterList().getParameters();
        return String.format("(%s)", IntStream.range(0, parameters.length)
                                              .mapToObj(i -> typeAndName.apply(i, parameters[i]))
                                              .collect(Collectors.joining(", "))
        );
    }

    private String parentInterface() {
        return parentInterface.getName();
    }

    private String name() {
        return decorator.getName();
    }

    private String typeAndNameDummy(int index, PsiParameter parameter) {
        String name = "";

        if (this.index == index) {
            name = original;
        } else {
            PsiType type = parameter.getType();
            if (type instanceof PsiPrimitiveType) {
                if (type.equals(PsiType.SHORT)) name = "(short) 0";
                if (type.equals(PsiType.CHAR)) name = "(char) 0";
                if (type.equals(PsiType.BYTE))name = "(byte) 0";
                if (type.equals(PsiType.INT)) name = "0";
                if (type.equals(PsiType.LONG)) name = "0L";
                if (type.equals(PsiType.FLOAT)) name = "0.0f";
                if (type.equals(PsiType.DOUBLE)) name = "0.0d";
                if (type.equals(PsiType.BOOLEAN)) name = "false";
            } else {
                name = "null";
            }
        }
        return name;
    }
}
