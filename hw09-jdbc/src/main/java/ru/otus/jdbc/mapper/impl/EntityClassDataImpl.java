package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassData;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.exception.EntityDataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class EntityClassDataImpl<T> implements EntityClassData<T> {

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntityClassDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Object getIdFieldValue(T object) {
        Field idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        try {
            return idField.get(object);
        } catch (IllegalAccessException e) {
            throw new EntityDataException("Error while retrieving id value", e);
        }
    }

    @Override
    public List<Object> getFieldValuesWithoutId(T object) {
        return entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(object);
                    } catch (IllegalAccessException e) {
                        throw new EntityDataException("Error while retrieving field value", e);
                    }
                })
                .collect(toList());
    }

    @Override
    public T getObjectFrom(ResultSet resultSet) {
        Constructor<T> defaultConstructor = entityClassMetaData.getConstructor();
        T object;
        try {
            object = defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new EntityDataException("Error while creating object", e);
        }
        List<Field> allFields = entityClassMetaData.getAllFields();
        int i = 1;
        for (Field field : allFields) {
            try {
                setFieldValue(object, field, resultSet.getObject(i));
            } catch (SQLException e) {
                throw new EntityDataException("Error while retrieving object from ResultSet", e);
            }
            i++;
        }
        return object;
    }

    private static void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new EntityDataException("Error while setting field value", e);
        }
    }

}
