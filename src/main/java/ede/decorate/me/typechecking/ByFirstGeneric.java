package ede.decorate.me.typechecking;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;

import java.util.function.BiPredicate;

/**
 * Stupid as fuck. Also doesn't work properly.
 */
//TODO Remove ASAP
@Deprecated
public final class ByFirstGeneric implements BiPredicate<PsiType, PsiTypeParameter[]> {
    private final PsiType psiType;

    public ByFirstGeneric(PsiType psiType) {
        this.psiType = psiType;
    }

    @Override
    public boolean test(PsiType type, final PsiTypeParameter[] typeParameters) {
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
    }
}
