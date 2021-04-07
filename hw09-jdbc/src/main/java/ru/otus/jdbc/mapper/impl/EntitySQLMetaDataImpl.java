package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        StringBuilder resultSql = new StringBuilder();
        resultSql.append("select ");
        String fieldsByCommas = entityClassMetaData.getAllFields()
                .stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        resultSql.append(fieldsByCommas)
                .append(" from ")
                .append(entityClassMetaData.getName());
        return resultSql.toString();
    }

    @Override
    public String getSelectByIdSql() {
        Field idField = entityClassMetaData.getIdField();
        return getSelectAllSql() +
                " where " +
                idField.getName() +
                " = ? ";
    }

    @Override
    public String getInsertSql() {
        StringBuilder resultSql = new StringBuilder();
        resultSql.append("insert into ")
                .append(entityClassMetaData.getName())
                .append("(");
        String fieldsWithoutIdByCommas = entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        resultSql.append(fieldsWithoutIdByCommas)
                .append(") values (");
        String paramsWithoutIdByCommas = entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));
        resultSql.append(paramsWithoutIdByCommas)
                .append(")");
        return resultSql.toString();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder resultSql = new StringBuilder();
        resultSql.append("update ")
                .append(entityClassMetaData.getName())
                .append(" set ");
        String fieldsWithoutIdByCommas = entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
        resultSql.append(fieldsWithoutIdByCommas)
                .append(" where ");
        Field idField = entityClassMetaData.getIdField();
        resultSql.append(idField.getName())
                .append(" = ?");
        return resultSql.toString();
    }

}
