package testframework;

import testframework.exception.TestClassNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static testframework.enumeration.TestState.FAILED;
import static testframework.enumeration.TestState.PASSED;

public class TestRunner {

    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";

    public static void main(String[] args) {
        TestRunner.run(Arrays.asList("homework.LifeCycleTest", "homework.CustomerTest"));
    }

    private static void run(List<String> testClassNameList) {

        List<TestMethod> testList = new ArrayList<>();

        testClassNameList.forEach(className -> {
            try {
                TestClass<?> testClass = new TestClass<>(Class.forName(className));
                testList.addAll(testClass.execute());
            } catch (ClassNotFoundException e) {
                throw new TestClassNotFoundException(className);
            }
        });

        System.out.println("\n");
        testList.forEach(m -> System.out.println(
                m.getClazz().getName() + " > " +
                m.getName() + " : " +
                m.getState() +
                (Objects.isNull(m.getThrowable()) ? "" :
                        "\n" + RED + printStackTrace(m.getThrowable()) + RESET)
        ));

        long allTestsCount = testList.size();
        long passedTestsCount = testList.stream()
                .filter(t -> Objects.equals(t.getState(), PASSED))
                .count();
        long failedTestsCount = testList.stream()
                .filter(t -> Objects.equals(t.getState(), FAILED))
                .count();
        System.out.printf((failedTestsCount>0 ? RED : RESET) +
                "Всего было запущено %d тестов\n" +
                "Из них: %d завершились успешно, %d не пройдено" + RESET,
                allTestsCount,
                passedTestsCount,
                failedTestsCount);
    }

    private static String printStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}