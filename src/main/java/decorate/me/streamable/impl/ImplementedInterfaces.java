package decorate.me.streamable.impl;

import com.intellij.psi.PsiClass;
import decorate.me.streamable.Streamable;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ImplementedInterfaces implements Streamable<PsiClass> {

    private final PsiClass sourcePsiClass;
    private final Set<PsiClass> implementedInterfaces;

    public ImplementedInterfaces(PsiClass sourcePsiClass) {
        this.sourcePsiClass = sourcePsiClass;
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
            psiFirstLevelInterfaces.addAll(Arrays.asList(sourcePsiClass.getInterfaces()));
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
