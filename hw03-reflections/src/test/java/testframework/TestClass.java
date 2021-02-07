package testframework;

import org.assertj.core.util.Strings;
import testframework.annotation.After;
import testframework.annotation.Before;
import testframework.annotation.DisplayName;
import testframework.annotation.Test;
import testframework.enumeration.TestState;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestClass<T> {

    private final Class<T> clazz;
    private final List<Method> beforeMethodList;
    private final List<TestMethod> testMethodList;
    private final List<Method> afterMethodList;

    public TestClass(Class<T> clazz) {
        this.clazz = clazz;
        this.beforeMethodList = new ArrayList<>();
        this.testMethodList = new ArrayList<>();
        this.afterMethodList = new ArrayList<>();
        classParse();
    }

    public List<TestMethod> execute() {
        testMethodList.forEach(this::testMethodExecute);
        return Collections.unmodifiableList(testMethodList);
    }

    private void classParse() {
        Method[] classMethods = clazz.getDeclaredMethods();
        Arrays.stream(classMethods).forEach(this::methodParse);
    }

    private void methodParse(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        boolean isTestMethod = false;
        String testName = "";
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.equals(Before.class)) {
                beforeMethodList.add(method);
            } else if (annotationType.equals(Test.class)) {
                isTestMethod = true;
            } else if (annotationType.equals(After.class)) {
                afterMethodList.add(method);
            } else if (annotationType.equals(DisplayName.class)) {
                testName = method.getAnnotation(DisplayName.class).value();
            }
        }
        if (isTestMethod) {
            if (Strings.isNullOrEmpty(testName)) {
                testName = method.getName();
            }
            testMethodList.add(new TestMethod(clazz, method, testName));
        }
    }

    private void processFailedTest(TestMethod testMethod, Exception e) {
        if (Objects.isNull(testMethod.getThrowable())) {
            testMethod.setThrowable(e);
        } else {
            Throwable baseThrowable = testMethod.getThrowable();
            baseThrowable.addSuppressed(e);
        }
        testMethod.setState(TestState.FAILED);
    }

    private void testMethodExecute(TestMethod testMethod) {
        T classInstance = getClassInstance();
        try {
            beforeMethodList.forEach(beforeMethod -> methodExecute(classInstance, beforeMethod));
            methodExecute(classInstance, testMethod.getMethod());
            testMethod.setState(TestState.PASSED);
        } catch (Exception e) {
            processFailedTest(testMethod, e);
        } finally {
            try {
                afterMethodList.forEach(afterMethod -> methodExecute(classInstance, afterMethod));
            } catch (Exception e) {
                processFailedTest(testMethod, e);
            }
        }
    }

    private T getClassInstance() {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private void methodExecute(T classInstance, Method method) {
        method.setAccessible(true);
        try {
            method.invoke(classInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

}
