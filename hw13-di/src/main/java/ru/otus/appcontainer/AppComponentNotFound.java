package ru.otus.appcontainer;

public class AppComponentNotFound extends RuntimeException {

    private static final String message = "Component '%s' not found";

    public AppComponentNotFound(String componentName) {
        super(String.format(message, componentName));
    }

    public AppComponentNotFound(Class<?> componentClass) {
        super(String.format(message, componentClass.getName()));
    }
}
