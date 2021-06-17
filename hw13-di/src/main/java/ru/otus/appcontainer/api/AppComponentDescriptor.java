package ru.otus.appcontainer.api;
import java.lang.reflect.Method;

public class AppComponentDescriptor {

    private String name;
    private int order;
    private AppComponentDescriptor factoryComponent;
    private Method factoryMethod;
    private Class<?> clazz;

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public AppComponentDescriptor getFactoryComponent() {
        return factoryComponent;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setFactoryComponent(AppComponentDescriptor factoryComponent) {
        this.factoryComponent = factoryComponent;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

}
