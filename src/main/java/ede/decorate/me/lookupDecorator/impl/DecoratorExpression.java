package ede.decorate.me.lookupDecorator.impl;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.jvm.JvmParameter;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import ede.decorate.me.icons.ElegantObjects;
import ede.decorate.me.lookupDecorator.LookupDecorator;

import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecoratorExpression implements LookupDecorator {

    private final String original;
    private Class<? extends PsiElement> clazz;
    private final PsiMethod decorator;
    private final PsiClass parentInterface;
    private final Integer index;

    public DecoratorExpression(String original, Class<? extends PsiElement> clazz, PsiMethod decorator, PsiClass parentInterface, Integer index) {
        this.original = original;
        this.clazz = clazz;
        this.decorator = decorator;
        this.parentInterface = parentInterface;
        this.index = index;
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
                                   .withIcon(ElegantObjects.CLASS)
                                   .withInsertHandler((context, item) -> {
                                       WriteCommandAction.runWriteCommandAction(context.getProject(), () -> {
                                           int originalExpressionStartOffset = context.getStartOffset() - original.length() - 1;
                                           deleteOriginalExpression(context, originalExpressionStartOffset);
                                           PsiElement decoratorExpression = PsiTreeUtil.getParentOfType(
                                                   context.getFile().getViewProvider().findElementAt(originalExpressionStartOffset),
                                                   clazz
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

    String typeAndName(int ignored, JvmParameter parameter) {
        if (parameter instanceof PsiParameter) {
            return ((PsiParameter) parameter).getText();
        }
        return "NOT SUPPORTED TYPE";
    }

    private String expression() {
        //decorator.getContainingClass().getQualifiedName() - qName
        return "new " + decorator.getContainingClass().getQualifiedName() + parameters(this::typeAndNameDummy);
    }

    private String parameters(BiFunction<Integer, JvmParameter, String> typeAndName) {
        JvmParameter[] parameters = decorator.getParameters();
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

    private String typeAndNameDummy(int index, JvmParameter ignored) {
        String name = "";
        if (this.index == index) {
            name = original;
        }
        return name;
    }
}
