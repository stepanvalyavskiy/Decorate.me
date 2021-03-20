package ede.decorate.me.streamable.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import ede.decorate.me.streamable.Streamable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Stream with a set of interfaces and abstract classes that are parents for given class {@link #sourcePsiClass}
 */
public final class SuperTypesOf implements Streamable<PsiClass> {

    private final PsiClass sourcePsiClass;
//    private final PsiType[] sourcePsiTypes;
    private final Set<PsiClass> superTypes;

    public SuperTypesOf(PsiClass sourcePsiClass, PsiType[] sourcePsiTypes) {
        this.sourcePsiClass = sourcePsiClass;
//        this.sourcePsiTypes = sourcePsiTypes;
        this.superTypes = superTypes();
    }

    /**
     * @return Stream with a set of interfaces and abstract classes that are parents for given class {@link #sourcePsiClass}
     */
    @Override
    public Stream<PsiClass>  stream() {
        return superTypes.stream();
    }

    /**
     * InputStream is an abstract class
     * and doesn't implement some interface related to input streams.
     * All its decorating constructor have this abstract class as argument.
     * Therefor I should consider abstract classes on a par with interfaces
     * to provide decorators completions for such classes
     */
    private Set<PsiClass> superTypes() {
        return abstractSuperClassesAndAllLevelInterfaces(
                abstractSuperClassesAndFirstLevelInterfaces()
        );
    }

    private Set<PsiClass> abstractSuperClassesAndAllLevelInterfaces(ArrayDeque<PsiClass> subSet) {
        Set<PsiClass> psiSuperTypes = new HashSet<>();
        while (!subSet.isEmpty()) {
            PsiClass pop = subSet.pop();
            boolean firstOccurrence = psiSuperTypes.add(pop);
            if (firstOccurrence) {
                Arrays.asList(pop.getInterfaces()).forEach(subSet::push);
            }
        }
        return psiSuperTypes;
    }

    private ArrayDeque<PsiClass> abstractSuperClassesAndFirstLevelInterfaces() {
        Set<PsiClass> psiSuperTypes = new HashSet<>();
        for(PsiClass psiClass = this.sourcePsiClass; hasSuperClass(psiClass); psiClass = nextParentOf(psiClass)) {
            psiSuperTypes.addAll(
                    firstLevelInterfacesOf(psiClass)
            );
            if (isAbstract(psiClass)) {
                psiSuperTypes.add(psiClass);
            }
        }
        return new ArrayDeque<>(psiSuperTypes);
    }

    private PsiClass nextParentOf(PsiClass psiClass) {
        return psiClass.getSuperClass();
    }

    /**
     * Greatest parent of all interfaces is Object!
     */
    private boolean hasSuperClass(PsiClass psiClass) {
        return psiClass.getSuperClass() != null;
    }

    /**
     * Interface also has an ABSTRACT modifier!
     */
    private boolean isAbstract(PsiClass psiClass) {
        return psiClass.getModifierList() != null && psiClass.getModifierList().hasModifierProperty(PsiModifier.ABSTRACT);
    }

    @NotNull
    private List<PsiClass> firstLevelInterfacesOf(PsiClass psiClass) {
//            TODO with types
//            Arrays.stream(psiClass.getInterfaceTypes())
//                  .collect(Collectors.toMap(
//                          JvmReferenceType::resolve,
//                          JvmReferenceType::typeArguments
//                          )
//                  );
        return Arrays.asList(psiClass.getInterfaces());
    }
}
