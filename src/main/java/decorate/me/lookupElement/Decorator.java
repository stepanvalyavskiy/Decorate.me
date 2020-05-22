package decorate.me.lookupElement;

import com.intellij.codeInsight.lookup.LookupElementBuilder;

public interface Decorator {
    LookupElementBuilder lookupElement();
}
