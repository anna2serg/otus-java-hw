package testframework.exception;

public class TestClassNotFoundException extends RuntimeException {

    private static final String message = "Тестовый класс %s не найден";

    public TestClassNotFoundException(String className) {
        super(String.format(message, className));
    }

}
