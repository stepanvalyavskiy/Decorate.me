package ede.decorate.me.lookupDecorator.impl;

import com.intellij.lang.jvm.JvmParameter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

public class SharedDecoratorExpression extends DecoratorExpression {

    private final Integer index;

    public SharedDecoratorExpression(String original, Class<? extends PsiElement> clazz , PsiMethod decorator, PsiClass parentInterface, Integer index) {
        super(original, clazz, decorator, parentInterface, index);
        this.index = index;
    }

    @Override
     String typeAndName(int index, JvmParameter parameter) {
        String name = super.typeAndName(index, parameter);
            if (this.index == index) {
                name =  String.format("[%s]", name);
            }
        return name;
    }
}
