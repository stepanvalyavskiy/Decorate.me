package decorate.me.streamable.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import decorate.me.streamable.Streamable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static decorate.me.streamable.impl.ConstructorsWithParameters.CtorToIfc;

public class ConstructorsWithParameters implements Streamable<CtorToIfc> {
    private final Streamable<PsiClass> implementedInterfaces;
    private final Project project;

    public ConstructorsWithParameters(Streamable<PsiClass> implementedInterfaces, Project project) {
        this.implementedInterfaces = implementedInterfaces;
        this.project = project;
    }

    @Override
    public Stream<CtorToIfc> stream() {
        return implementedInterfaces
                .stream()
                .flatMap(this::constructorsWithParameters);
    }

    private Stream<CtorToIfc> constructorsWithParameters(PsiClass parentInterface) {
        return candidatesForIfc(parentInterface)
                .stream()
                .flatMap(impl -> Arrays.stream(impl.getConstructors()))
                .filter(PsiMethod::hasParameters)
                .map(ctor -> new CtorToIfc(ctor, parentInterface));
    }

    @NotNull
    private Collection<PsiClass> candidatesForIfc(PsiClass parentInterface) {
        return ClassInheritorsSearch.search(
                parentInterface,
                GlobalSearchScope.allScope(project),
                true
        ).findAll();
    }

    static public class CtorToIfc {
        public final PsiMethod ctor;
        public final PsiClass ifc;

        public CtorToIfc(PsiMethod ctor, PsiClass ifc) {
            this.ctor = ctor;
            this.ifc = ifc;
        }
    }

}
