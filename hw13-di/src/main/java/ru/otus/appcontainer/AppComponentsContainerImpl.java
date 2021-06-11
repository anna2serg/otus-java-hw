package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import ru.otus.appcontainer.api.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<AppComponentDescriptor> appComponentDescriptors = new ArrayList<>();
    private final Map<String, Object> appComponents = new HashMap<>();
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
            if (Objects.nonNull(appComponentDescriptor.getFactoryComponent())
                    && Objects.nonNull(appComponentDescriptor.getFactoryMethod())) {
                instantiateUsingFactoryMethod(appComponentDescriptor);
            } else {
                instantiateByNoArgsConstructor(appComponentDescriptor);
            }
        });
    }

    private void instantiateByNoArgsConstructor(AppComponentDescriptor appComponentDescriptor) {
        Class<?> clazz = appComponentDescriptor.getClazz();
        Constructor<?>[] constructors = clazz.getConstructors();
        try {
            Object appComponent = constructors[0].newInstance();
            appComponents.put(appComponentDescriptor.getName(), appComponent);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void instantiateUsingFactoryMethod(AppComponentDescriptor appComponentDescriptor) {
        Object[] arguments = null;
        Method factoryMethod = appComponentDescriptor.getFactoryMethod();
        AppComponentDescriptor factoryComponentDescriptor = appComponentDescriptor.getFactoryComponent();
        Object factoryComponent = getAppComponent(factoryComponentDescriptor.getClazz());
        if (factoryMethod.getParameterCount()>0) {
            arguments = new Object[factoryMethod.getParameterCount()];
            Parameter[] appComponentParams = factoryMethod.getParameters();
            for (int i = 0; i < factoryMethod.getParameterCount(); i++) {
                Parameter param = appComponentParams[i];
                Class<?> paramClass = param.getType();
                arguments[i] = getAppComponent(paramClass);
            }
        }
        try {
            Object appComponent = factoryMethod.invoke(factoryComponent, arguments);
            appComponents.put(appComponentDescriptor.getName(), appComponent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Map.Entry<String, Object> entry : appComponents.entrySet()) {
            Object appComponent = entry.getValue();
            if (componentClass.isAssignableFrom(appComponent.getClass())) {
                return (C) entry.getValue();
            }
        }
        throw new AppComponentNotFound(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        for (String key : appComponents.keySet()) {
            if (componentName.equals(key)) {
                return (C) appComponents.get(key);
            }
        }
        throw new AppComponentNotFound(componentName);
    }

}
