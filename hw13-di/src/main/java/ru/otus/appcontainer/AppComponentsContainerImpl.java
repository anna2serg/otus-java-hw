package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.appcontainer.api.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final List<AppComponentDescriptor> appComponentDescriptors = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final static int APP_COMPONENT_ORDER_SHIFT = 100;

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner());
        Set<Class<?>> initialConfigClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true);
        processConfig(initialConfigClasses.toArray(new Class[]{}));
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        processConfig(initialConfigClasses);
    }

    private void processConfig(Class<?>[] configClasses) {
        for (Class<?> configClass : configClasses) {
            checkConfigClass(configClass);
            loadAppComponentDescriptors(configClass);
        }
        instantiateAppComponents();
    }


    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private void loadAppComponentDescriptors(Class<?> configClass) {
        List<AppComponentDescriptor> appComponentDescriptors = new ArrayList<>();
        AppComponentDescriptor factoryComponentDescriptor = new AppComponentDescriptor();
        AppComponentsContainerConfig appComponentsContainerConfig = configClass.getAnnotation(AppComponentsContainerConfig.class);
        int factoryComponentOrder = appComponentsContainerConfig.order();
        factoryComponentDescriptor.setOrder(factoryComponentOrder);
        factoryComponentDescriptor.setName(configClass.getSimpleName());
        factoryComponentDescriptor.setClazz(configClass);
        appComponentDescriptors.add(factoryComponentDescriptor);

        Method[] factoryMethods = configClass.getMethods();
        for (Method factoryMethod : factoryMethods) {
            if (factoryMethod.isAnnotationPresent(AppComponent.class)) {
                AppComponent appComponent = factoryMethod.getAnnotation(AppComponent.class);
                AppComponentDescriptor componentDescriptor = new AppComponentDescriptor();
                componentDescriptor.setName(appComponent.name());
                componentDescriptor.setOrder(factoryComponentOrder + APP_COMPONENT_ORDER_SHIFT + appComponent.order());
                componentDescriptor.setFactoryMethod(factoryMethod);
                componentDescriptor.setFactoryComponent(factoryComponentDescriptor);
                componentDescriptor.setClazz(factoryMethod.getReturnType());
                appComponentDescriptors.add(componentDescriptor);
            }
        }

        this.appComponentDescriptors.addAll(appComponentDescriptors.stream()
                .sorted(Comparator.comparing(AppComponentDescriptor::getOrder))
                .collect(Collectors.toList())
        );
    }

    private void instantiateAppComponents() {
        this.appComponentDescriptors.forEach(appComponentDescriptor -> {
            Object appComponent;
            if (Objects.nonNull(appComponentDescriptor.getFactoryComponent())
                    && Objects.nonNull(appComponentDescriptor.getFactoryMethod())) {
                appComponent = instantiateUsingFactoryMethod(appComponentDescriptor);
            } else {
                appComponent = instantiateByNoArgsConstructor(appComponentDescriptor);
            }
            appComponents.add(appComponent);
            appComponentsByName.put(appComponentDescriptor.getName(), appComponent);
        });
    }

    private Object instantiateByNoArgsConstructor(AppComponentDescriptor appComponentDescriptor) {
        Class<?> clazz = appComponentDescriptor.getClazz();
        Constructor<?>[] constructors = clazz.getConstructors();
        try {
            return constructors[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new AppComponentsContainerException(String.format("Component %s instantiation error: %s",
                    clazz.getName(), e.getMessage()), e);
        }
    }

    private Object instantiateUsingFactoryMethod(AppComponentDescriptor appComponentDescriptor) {
        Method factoryMethod = appComponentDescriptor.getFactoryMethod();
        AppComponentDescriptor factoryComponentDescriptor = appComponentDescriptor.getFactoryComponent();
        Class<?> factoryComponentClazz = factoryComponentDescriptor.getClazz();
        Object factoryComponent = getAppComponent(factoryComponentClazz);
        Object[] arguments = Arrays.stream(factoryMethod.getParameterTypes()).map(this::getAppComponent).toArray();
        try {
            return factoryMethod.invoke(factoryComponent, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AppComponentsContainerException(String.format("Component %s instantiation error: %s",
                    factoryComponentClazz.getName(), e.getMessage()), e);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Object appComponent = appComponents.stream().filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .findFirst().orElseThrow(() -> new AppComponentNotFoundException(componentClass));
        return (C) appComponent;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object appComponent = appComponentsByName.get(componentName);
        if (Objects.nonNull(appComponent)) {
            return (C) appComponent;
        }
        throw new AppComponentNotFoundException(componentName);
    }

}
