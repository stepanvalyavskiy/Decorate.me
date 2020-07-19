package ede.decorate.me.streamable.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import ede.decorate.me.streamable.Streamable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Stream with a map constructor to interface/abstract class.
 */
public final class ConstructorsWithParameters implements Streamable<ConstructorsWithParameters.ConstructorToSuperType> {
    private final Streamable<PsiClass> superTypes;
    private final Project project;

    public ConstructorsWithParameters(Streamable<PsiClass> superTypes, Project project) {
        this.superTypes = superTypes;
        this.project = project;
    }

    /**
     * @return Stream with a map constructor to interface/abstract class.
     * If constructor has a parameter with the same type as given interface/abstract class (from {@link #superTypes}),
     * and belongs to the class that implements this interface/extends this abstract class
     * they will be mapped and added to result stream.
     * if one ctor has N parameters with the same type as given interface/abstract class, there will be N mapped pairs.
     */
    @Override
    public Stream<ConstructorToSuperType> stream() {
        return superTypes
                .stream()
                .flatMap(this::constructorsWithParametersToSuperType);
    }

    private Stream<ConstructorToSuperType> constructorsWithParametersToSuperType(PsiClass superType) {
        return subTypesFor(superType)
                .stream()
                .flatMap(impl -> Arrays.stream(impl.getConstructors()))
                .filter(PsiMethod::hasParameters)
                .map(ctor -> new ConstructorToSuperType(ctor, superType));
    }

    @NotNull
    private Collection<PsiClass> subTypesFor(PsiClass superType) {
        return ClassInheritorsSearch.search(
                superType,
                GlobalSearchScope.allScope(project),
                true
        ).findAll();
    }

    static public class ConstructorToSuperType {
        public final PsiMethod ctor;
        public final PsiClass superType;

        public ConstructorToSuperType(PsiMethod ctor, PsiClass superType) {
            this.ctor = ctor;
            this.superType = superType;
        }
    }

}
