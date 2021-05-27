package ru.otus.db.repository;

import org.hibernate.Session;
import ru.otus.db.repository.DataTemplate;

import java.util.List;
import java.util.Optional;

public class DataTemplateHibernate<T> implements DataTemplate<T> {

    private final Class<T> clazz;

    public DataTemplateHibernate(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findById(Session session, long id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<T> findAll(Session session) {
        return session.createQuery(String.format("from %s", clazz.getSimpleName()), clazz).getResultList();
    }

    @Override
    public void insert(Session session, T object) {
        session.persist(object);
    }

    @Override
    public void update(Session session, T object) {
        session.merge(object);
    }

    @Override
    public Optional<T> findByField(Session session, String fieldName, String fieldValue) {
        return session.createQuery(String.format("select c from %s c where c.%s = :fieldValue", clazz.getSimpleName(), fieldName), clazz)
                .setParameter("fieldValue", fieldValue)
                .uniqueResultOptional();
    }
}
