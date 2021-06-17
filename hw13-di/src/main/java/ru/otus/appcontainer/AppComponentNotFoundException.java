package ru.otus.appcontainer;

public class AppComponentNotFoundException extends RuntimeException {

    private static final String message = "Component '%s' not found";

    public AppComponentNotFoundException(String componentName) {
        super(String.format(message, componentName));
    }

    public AppComponentNotFoundException(Class<?> componentClass) {
        super(String.format(message, componentClass.getName()));
    }
}
