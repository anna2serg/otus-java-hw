package ru.otus.aop;

//java -javaagent:proxyDemo.jar=ru.otus.aop.TestLogging -jar proxyDemo.jar
public class DemoAop {
    public static void main(String[] args) {
        TestLogging testLogging = new TestLogging();
        testLogging.calculation();
        testLogging.calculation(10);
        testLogging.calculation(2, 4.85);
        testLogging.calculation(6, "test-calculation");
        testLogging.calculation(5, 2.15, "test-calculation");
        testLogging.calc("test-calc");
    }
}