package decorate.me.lookupElement.impl;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.jvm.JvmParameter;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import decorate.me.lookupElement.Decorator;
import icons.ElegantObjects;

import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecoratorExpression implements Decorator {

    private final String original;
    private final PsiMethod decorator;
    private final PsiClass parentInterface;
    private final Integer index;

    public DecoratorExpression(String original, PsiMethod decorator, PsiClass parentInterface, Integer index) {
        this.original = original;
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
                                   .withIcon(ElegantObjects.CACTUS)
                                   .withInsertHandler((context, item) -> {
                                       WriteCommandAction.runWriteCommandAction(context.getProject(), () -> {
                                           int startOffset = context.getStartOffset() - original.length() - 1;
                                           context.getDocument().deleteString(startOffset, context.getStartOffset());
                                           //TODO if i can solve it with PsiElement.replace?
                                           PsiDocumentManager.getInstance(context.getProject())
                                                             .commitDocument(context.getDocument());
                                           PsiNewExpression newExpression = PsiTreeUtil.getParentOfType(
                                                   context.getFile().getViewProvider().findElementAt(startOffset),
                                                   PsiNewExpression.class
                                           );
                                           JavaCodeStyleManager.getInstance(context.getProject()).shortenClassReferences(newExpression);
                                       });
                                   });
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
