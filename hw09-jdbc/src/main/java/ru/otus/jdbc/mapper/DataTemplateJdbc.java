package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.impl.EntityClassDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySQLMetaDataImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassData<T> entityClassData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = new EntitySQLMetaDataImpl<>(this.entityClassMetaData);
        this.entityClassData = new EntityClassDataImpl<>(this.entityClassMetaData);
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String selectByIdSql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, selectByIdSql, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return entityClassData.getObjectFrom(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        String selectAllSql = entitySQLMetaData.getSelectAllSql();
        return dbExecutor.executeSelect(connection, selectAllSql, Collections.emptyList(), rs -> {
            var objectList = new ArrayList<T>();
            try {
                if (rs.next()) {
                    T object = entityClassData.getObjectFrom(rs);
                    objectList.add(object);
                }
                return objectList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        String insertSql = entitySQLMetaData.getInsertSql();
        try {
            List<Object> values = entityClassData.getFieldValuesWithoutId(object);
            return dbExecutor.executeStatement(connection, insertSql, values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        String updateSql = entitySQLMetaData.getUpdateSql();
        try {
            List<Object> values = entityClassData.getFieldValuesWithoutId(object);
            Object idValue = entityClassData.getIdFieldValue(object);
            values.add(idValue);
            dbExecutor.executeStatement(connection, updateSql, values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

}
