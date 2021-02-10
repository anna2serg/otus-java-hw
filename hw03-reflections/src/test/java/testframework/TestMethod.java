package testframework;

import testframework.enumeration.TestState;

import java.lang.reflect.Method;

public class TestMethod {

    private final Class<?> clazz;
    private final Method method;
    private final String name;
    private TestState state;
    private Throwable throwable;

    public TestMethod(Class<?> clazz, Method method, String name) {
        this.clazz = clazz;
        this.method = method;
        this.name = name;
        this.throwable = null;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public TestState getState() {
        return state;
    }

    public void setState(TestState state) {
        this.state = state;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}
