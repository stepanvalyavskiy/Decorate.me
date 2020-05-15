package decorate.me.lookupElement;

import com.intellij.lang.jvm.JvmParameter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DecoratedExpression {

    private final String original;
    private final PsiMethod decorator;
    private final PsiClass parentInterface;

    public DecoratedExpression(String original, PsiMethod decorator, PsiClass parentInterface) {
        this.original = original;
        this.decorator = decorator;
        this.parentInterface = parentInterface;
    }

    @Override
    public String toString() {
        String template = parameters()
                .replaceAll(parentInterface(), "!")
                .replaceAll("[^(),!]*", "")
                .replaceAll(",", ", ");
        //decorator.getContainingClass().getQualifiedName() - qName
        return "new " + decorator.getName() + template.replace("!", original);
    }

    public String parentInterface() {
        return parentInterface.getName();
    }

    public String name() {
        return decorator.getName();
    }

    public String parameters() {
        return String.format("(%s)",  Arrays.stream(decorator.getParameters()).map(typeAndName).collect(Collectors.joining(", ")));
    }

    private Function<JvmParameter, String>  typeAndName = p -> {
        if (p instanceof PsiParameter) {
            return ((PsiParameter) p).getText();
        }
        return "NOT SUPPORTED TYPE";
    };
}
