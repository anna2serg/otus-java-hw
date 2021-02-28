package ru.otus.aop;

public class TestLogging {

    @Log
    public void calculation() {
        System.out.println("this is a calculation with empty parameters");
    }

    @Log
    public void calculation(int param) {
        System.out.println("this is a calculation with single parameter");
    }

    @Log
    public void calculation(int param1, double param2) {
        System.out.println("this is a calculation with two parameters (int&double)");
    }

    public void calculation(int param1, String param2) {
        System.out.println("this is a calculation with two parameters (int&String)");
        System.out.println();
    }

    @Log
    public void calculation(int param1, double param2, String param3) {
        System.out.println("this is a calculation with three parameters");
    }

    @Log
    public void calc(String param0) {
        System.out.println("this is a calc with single parameter");
    }
}
