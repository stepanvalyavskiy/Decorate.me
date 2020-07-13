package ede.decorate.me.streamable.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import ede.decorate.me.streamable.Streamable;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ImplementedInterfaces implements Streamable<PsiClass> {

    private final PsiClass sourcePsiClass;
    private PsiType[] sourcePsiTypes;
    private final Set<PsiClass> implementedInterfaces;

    public ImplementedInterfaces(PsiClass sourcePsiClass, PsiType[] sourcePsiTypes) {
        this.sourcePsiClass = sourcePsiClass;
        this.sourcePsiTypes = sourcePsiTypes;
        this.implementedInterfaces = implementedInterfaces();
    }

    @Override
    public Stream<PsiClass>  stream() {
        return implementedInterfaces.stream();
    }

    private Set<PsiClass> implementedInterfaces() {
        Set<PsiClass> psiFirstLevelInterfaces = new HashSet<>(Arrays.asList(sourcePsiClass.getInterfaces()));
        PsiClass sourcePsiClass = this.sourcePsiClass;
        while (sourcePsiClass.getSuperClass() != null) {
            sourcePsiClass = sourcePsiClass.getSuperClass();
            if (sourcePsiClass.getModifierList().hasModifierProperty(PsiModifier.ABSTRACT)) {
                psiFirstLevelInterfaces.add(sourcePsiClass);
            }
            psiFirstLevelInterfaces.addAll(Arrays.asList(sourcePsiClass.getInterfaces()));
//            TODO with types
//            Arrays.stream(sourcePsiClass.getInterfaceTypes())
//                  .collect(Collectors.toMap(
//                          JvmReferenceType::resolve,
//                          JvmReferenceType::typeArguments
//                          )
//                  );
        }
        ArrayDeque<PsiClass> interfaces = new ArrayDeque<>(psiFirstLevelInterfaces);
        Set<PsiClass> psiInterfacesTree = new HashSet<>();
        while (!interfaces.isEmpty()) {
            PsiClass pop = interfaces.pop();
            boolean firstOccurrence = psiInterfacesTree.add(pop);
            if (firstOccurrence) {
                Arrays.asList(pop.getInterfaces()).forEach(interfaces::push);
            }
        }
        return psiInterfacesTree;
    }
}
