package decorate.me;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.ProcessingContext;
import decorate.me.lookupElement.DecoratedExpression;
import icons.ElegantObjects;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class DecoratorConstructorsCompletionContributor extends CompletionContributor {

    public DecoratorConstructorsCompletionContributor() {
        extend(CompletionType.BASIC,
                psiElement(PsiIdentifier.class)
                        .withParent(psiElement(PsiReferenceExpression.class)
                                .withChild(psiElement(PsiNewExpression.class))),
                new DecoratorConstructorsCompletionProvider()
        );

    }

    private static class DecoratorConstructorsCompletionProvider extends CompletionProvider<CompletionParameters> {
        private PsiNewExpression originalExpression;
        private Project project;


        @Override
        protected void addCompletions(@NotNull CompletionParameters completionParameters, @NotNull ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
            project = completionParameters.getEditor().getProject();
            Optional.of(completionParameters.getPosition())
                    .map(this::replaceableExpression)
                    .flatMap(this::targetPsiClassOfReplaceableExpression)
                    .map(this::constructorsThatDecorateTargetExpressionGroupedByInterface)
                    .map(this::lookupElements)
                    .ifPresent(lookupElements ->
                            lookupElements.forEach(completionResultSet::addElement));
        }

        private PsiNewExpression replaceableExpression(PsiElement myPosition) {
            //TODO better way to find newExpression?
            originalExpression = PsiTreeUtil.getChildOfType(
                    PsiTreeUtil.getParentOfType(myPosition, PsiReferenceExpression.class),
                    PsiNewExpression.class);
            return originalExpression;
        }

        private Optional<PsiClass> targetPsiClassOfReplaceableExpression(PsiNewExpression originalExpression) {
            //TODO ReferencesSearch.search(completionParameters.getPosition(), GlobalSearchScope.projectScope(completionParameters.getPosition().getProject()));
            return Optional.of(originalExpression)
                           .map(PsiNewExpression::getClassReference)
                           .map(PsiJavaCodeReferenceElement::resolve)
                           .map(PsiClass.class::cast);
        }

        private Map<PsiClass, List<PsiMethod>> constructorsThatDecorateTargetExpressionGroupedByInterface(PsiClass targetClass) {
            Set<PsiClass> psiInterfaces = implementedInterfaces(targetClass);
            final SearchScope scope = GlobalSearchScope.allScope(project);
            return psiInterfaces.stream()
                                .collect(Collectors.toMap(
                                        parentInterface -> parentInterface,
                                        parentInterface -> inheritorsConstructorsThatHaveInterfaceAsParameter(scope, parentInterface)
                                ));
        }

        private Set<PsiClass> implementedInterfaces(PsiClass targetClass) {
            Set<PsiClass> psiFirstLevelInterfaces = new HashSet<>(Arrays.asList(targetClass.getInterfaces()));
            while (targetClass.getSuperClass() != null) {
                targetClass = targetClass.getSuperClass();
                psiFirstLevelInterfaces.addAll(Arrays.asList(targetClass.getInterfaces()));
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

        private List<PsiMethod> inheritorsConstructorsThatHaveInterfaceAsParameter(SearchScope scope, PsiClass parentInterface) {
            Predicate<PsiMethod> hasParentInterfaceAsParameter =
                    ctor -> Arrays.stream(ctor.getParameters())
                                  .anyMatch(param -> parentInterface
                                          .isEquivalentTo((PsiUtil.resolveClassInType((PsiType) param.getType()))));
            return ClassInheritorsSearch.search(parentInterface, scope, true).findAll()
                                        .stream()
                                        .flatMap(impl -> Arrays.stream(impl.getConstructors()))
                                        .filter(PsiMethod::hasParameters)
                                        .filter(hasParentInterfaceAsParameter)
                                        .collect(Collectors.toList());
        }

        private List<LookupElementBuilder> lookupElements(Map<PsiClass, List<PsiMethod>> interfacesToDecoratedConstructors) {
            return interfacesToDecoratedConstructors
                    .entrySet()
                    .stream()
                    .flatMap(this::lookupElements)
                    .collect(Collectors.toList());
        }

        private Stream<LookupElementBuilder> lookupElements(Map.Entry<PsiClass, List<PsiMethod>> interfaceToDecoratedConstructors) {
            PsiClass parentInterface = interfaceToDecoratedConstructors.getKey();
            List<PsiMethod> decorators = interfaceToDecoratedConstructors.getValue();
            return decorators.stream().map(decorator -> lookupElement(parentInterface, decorator));
        }

        private LookupElementBuilder lookupElement(PsiClass parentInterface, PsiMethod decorator) {
            DecoratedExpression constructor = new DecoratedExpression(originalExpression.getText(), decorator, parentInterface);
            return LookupElementBuilder.create(constructor)
                                       .withTailText(constructor.parameters())
                                       .withTypeText(constructor.parentInterface())
                                       .withPresentableText(constructor.name())
                                       .withLookupString(constructor.name())
                                       .withLookupString("wrap " + constructor.name())
                                       .withIcon(ElegantObjects.CACTUS)
                                       .withInsertHandler((context, item) -> {
                                           //TODO avoid getDocument
                                           WriteCommandAction.runWriteCommandAction(context.getProject(), () -> {
                                               context.getDocument().replaceString(context.getStartOffset() - originalExpression.getTextLength() - 1, context.getStartOffset(), "");
                                               //TODO if i can solve it with PsiElement.replace?
                                               //PsiExpression decoratingExpression = JavaPsiFacade.getInstance(originalExpression.getProject()).getParserFacade().createExpressionFromText(constructor.toString(), null);
                                               //PsiElement newExpression = originalExpression.replace(decoratingExpression);
                                               //JavaCodeStyleManager.getInstance(context.getProject()).shortenClassReferences(newExpression);
                                           });
                                       });
        }
    }
}
