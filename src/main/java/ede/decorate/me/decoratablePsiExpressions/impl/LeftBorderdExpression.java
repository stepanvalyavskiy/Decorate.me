package ede.decorate.me.decoratablePsiExpressions.impl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;



public final class LeftBorderdExpression implements DecoratablePsiExpression {

    private final DecoratablePsiExpression origin;
    private final PsiType type;
    private final PsiClass clazz;

    //TODO no logic in constructor
    public LeftBorderdExpression(DecoratablePsiExpression origin) {
        this.origin = origin;
        PsiElement parent = origin.content().getParent().getParent();
        if (parent instanceof PsiField || parent instanceof PsiLocalVariable) {
            type = ((PsiVariable)parent).getType();
        } else if (parent instanceof PsiAssignmentExpression) {
            PsiExpressionStatement parent2 = (PsiExpressionStatement) parent.getParent();
            PsiReferenceExpression lExpression = (PsiReferenceExpression) ((PsiAssignmentExpression)parent).getLExpression();
            PsiVariable resolve = (PsiVariable) lExpression.resolve();
            type = resolve.getType();

        } else if (parent instanceof PsiExpressionList) {
            PsiCall parent1 = (PsiCall) parent.getParent();
            int position = -1;
            PsiExpression[] expressions = parent1.getArgumentList().getExpressions();
            for (int i = 0; i < expressions.length; i++) {
                if (expressions[i] == origin.content().getParent()) {
                    position = i;
                    break;
                }
            }
            type = parent1.resolveMethod().getParameterList().getParameter(position).getType();


        } else if (parent instanceof PsiReturnStatement) {
            PsiReturnStatement parent1 = (PsiReturnStatement) parent;
            PsiMethod parent2 = (PsiMethod) parent1.getParent().getParent();
            type = parent2.getReturnType();

        } else {
            type = origin.myType();
            clazz = null;
            return;
        }
        clazz = ((PsiClassReferenceType)type).resolve();
    }

    @Override
    public PsiType myType() {
        return type;
    }

    @Override
    public PsiClass leftType() {
        return clazz;
    }

    @Override
    public PsiElement content() {
        return origin.content();
    }

    @Override
    public PsiClass myClass() {
        return origin.myClass();
    }

    @Override
    public PsiType[] myTypes() {
        return origin.myTypes();
    }
}
