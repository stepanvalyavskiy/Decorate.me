package ede.decorate.me.streamable.impl;

import com.intellij.psi.PsiClass;
import ede.decorate.me.streamable.Streamable;

import java.util.stream.Stream;

public final class LeftBorderedSuperTypes implements Streamable<PsiClass> {
    private final Streamable<PsiClass> superTypes;
    private final PsiClass psiClass;

    public LeftBorderedSuperTypes(Streamable<PsiClass> superTypes, PsiClass psiClass) {

        this.superTypes = superTypes;
        this.psiClass = psiClass;
    }

    @Override
    public Stream<PsiClass> stream() {
        return psiClass == null
                ? superTypes.stream()
                : superTypes
                .stream()
                .filter(clazz -> clazz.isEquivalentTo(psiClass) || clazz.isInheritor(psiClass, true));
    }
}
