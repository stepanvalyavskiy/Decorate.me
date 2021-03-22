package ede.decorate.me.decoratablePsiExpressions.impl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;

import java.util.Optional;


public final class LeftBorderdExpression implements DecoratablePsiExpression {

    private final DecoratablePsiExpression origin;
    private final PsiType type;
    private final PsiClass clazz;

    public LeftBorderdExpression(DecoratablePsiExpression origin) {
        this.origin = origin;
        final Optional<PsiType> type;
        PsiElement parent = origin.content().getParent().getParent();
        if (parent instanceof PsiField || parent instanceof PsiLocalVariable) {
            type = Optional.of(parent)
                    .map(PsiVariable.class::cast)
                    .map(PsiVariable::getType);
            //type = ((PsiVariable)parent).getType();
        } else if (parent instanceof PsiAssignmentExpression) {
            type = Optional.of(parent)
                    .map(PsiAssignmentExpression.class::cast)
                    .map(PsiAssignmentExpression::getLExpression)
                    .map(PsiReferenceExpression.class::cast)
                    .map(PsiReferenceExpression::resolve)
                    .map(PsiVariable.class::cast)
                    .map(PsiVariable::getType);
//            PsiReferenceExpression lExpression = (PsiReferenceExpression) ((PsiAssignmentExpression)parent).getLExpression();
//            PsiVariable resolve = (PsiVariable) lExpression.resolve();
//            type = resolve.getType();
        } else if (parent instanceof PsiExpressionList) {
            final var psiCall =
                    Optional.of(parent)
                            .map(PsiElement::getParent)
                            .map(PsiCall.class::cast);
            type = psiCall
                    .map(PsiCall::getArgumentList)
                    .map(PsiExpressionList::getExpressions)
                    .map(expressions -> {
                        for (int i = 0; i < expressions.length; i++) {
                            if (expressions[i] == origin.content().getParent()) {
                                return i;
                            }
                        }
                        return null;
                    }).flatMap(idx ->
                            psiCall
                                    .map(PsiCall::resolveMethod)
                                    .or(() -> {
                                        final var psiCallCopy = psiCall
                                                .map(PsiCall::copy)
                                                .map(PsiCall.class::cast);
                                        psiCallCopy
                                                .map(PsiCall::getArgumentList)
                                                .map(PsiExpressionList::getExpressions)
                                                .map(exp -> exp[idx].replace(exp[idx].getFirstChild()));
                                        return psiCallCopy
                                                .map(PsiCall::resolveMethod);
                                    })
                                    .map(PsiMethod::getParameterList)
                                    .map(params -> params.getParameter(idx))
                                    .map(PsiParameter::getType)
                                    .map(
                                            t -> t instanceof PsiEllipsisType
                                            ? ((PsiEllipsisType)t).getComponentType()
                                            : t
                                    )
                    );

//            PsiElement copy = parent.getParent().copy();
//            PsiExpressionList argumentList = ((PsiCall) copy).getArgumentList();
//            PsiExpression[] expressions = argumentList.getExpressions();
//            expressions[0].replace((PsiExpression) expressions[0].getFirstChild());
//            ((PsiCall)copy).resolveMethod();
            //__________________________________________________________________
//            PsiCall parent1 = (PsiCall) parent.getParent();
//            int position = -1;
//            PsiExpression[] expressions = parent1.getArgumentList().getExpressions();
//            for (int i = 0; i < expressions.length; i++) {
//                if (expressions[i] == origin.content().getParent()) {
//                    position = i;
//                    break;
//                }
//            }
//            type = parent1.resolveMethod().getParameterList().getParameter(position).getType();
        } else if (parent instanceof PsiReturnStatement) {
            type = Optional.of(parent)
                    .map(PsiElement::getParent)
                    .map(PsiElement::getParent)
                    .map(PsiMethod.class::cast)
                    .map(PsiMethod::getReturnType);
//            PsiReturnStatement parent1 = (PsiReturnStatement) parent;
//            PsiMethod parent2 = (PsiMethod) parent1.getParent().getParent();
//            type = parent2.getReturnType();
        } else {
            this.type = origin.myType();
            clazz = null;
            return;
        }
        this.type = type.orElse(origin.myType());
        clazz = ((PsiClassReferenceType)this.type).resolve();
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
