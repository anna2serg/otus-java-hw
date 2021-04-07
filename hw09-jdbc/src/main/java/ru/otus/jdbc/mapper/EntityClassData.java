package ru.otus.jdbc.mapper;

import java.sql.ResultSet;
import java.util.List;

public interface EntityClassData<T> {

    Object getIdFieldValue(T object);

    List<Object> getFieldValuesWithoutId(T object);

    T getObjectFrom(ResultSet resultSet);

}
