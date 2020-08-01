package ede.decorate.me;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProofOfConceptTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/testData/baseStructure";
    }

    public void testCompletionOverConstructor() {
        myFixture.configureByFiles("a/main.java", "a/interface.java", "a/original.java", "x/decorator.java", "a/object.java");
        myFixture.complete(CompletionType.SMART, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertTrue(strings != null && !strings.isEmpty());
        assertTrue(strings.contains("new x.Decorator(new Original())"));
        myFixture.type("\t");
        myFixture.checkResultByFile("result/completion.java");
    }

    public void testCompletionSharedOverConstructor() {
        myFixture.configureByFiles("a/main.java", "a/interface.java", "a/original.java", "x/sharedDecorator.java", "a/object.java");
        myFixture.complete(CompletionType.SMART, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertTrue(strings != null && !strings.isEmpty());
        List<String> collect = Arrays.stream(Objects.requireNonNull(myFixture.getLookupElements()))
                                     .map(LookupElement::getLookupString)
                                     .collect(Collectors.toList());
        assertTrue(collect.contains("new x.SharedDecorator(new Original(), )"));
        assertTrue(collect.contains("new x.SharedDecorator(, new Original())"));
        myFixture.type("\t");
        myFixture.checkResultByFile("result/completionShared.java");
    }

    public void testCompletionOverMethodCall() {
        myFixture.configureByFiles("a/mainForMethodCall.java", "a/interface.java", "a/original.java", "x/decorator.java", "a/object.java");
        myFixture.complete(CompletionType.SMART, 1);
        List<String> strings = myFixture.getLookupElementStrings();
        assertTrue(strings != null && !strings.isEmpty());
        assertTrue(strings.contains("new x.Decorator(new Main().init())"));
        myFixture.type("\t");
        myFixture.checkResultByFile("result/completionMethodCall.java");
    }
}
