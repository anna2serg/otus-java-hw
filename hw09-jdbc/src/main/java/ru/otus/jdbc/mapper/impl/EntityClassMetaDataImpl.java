package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.annotation.Id;
import ru.otus.jdbc.mapper.exception.EntityMetaDataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new EntityMetaDataException("Error retrieving class constructor", e);
        }
    }

    @Override
    public Field getIdField() {
        return getAllFields().stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .collect(toSingleton());
    }

    @Override
    public List<Field> getAllFields() {
        Field[] allFields = clazz.getDeclaredFields();
        return Arrays.asList(allFields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream()
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(toList());
    }

    private static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                toList(),
                list -> {
                    if (list.size() < 1) {
                        throw new EntityMetaDataException("No entity identifiers found");
                    }
                    if (list.size() > 1) {
                        throw new EntityMetaDataException("More than one entity identifiers found");
                    }
                    return list.get(0);
                }
        );
    }

}
