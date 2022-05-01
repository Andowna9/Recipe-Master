package com.codecooks.dao;

import javax.jdo.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class that implements most common db access operations.
 */
public abstract class GenericDAOBase<T> {

    protected static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("RecipeMaster");
    private Class<T> clazz;

    public GenericDAOBase() {

        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        clazz = (Class<T>) pt.getActualTypeArguments()[0];
    }

    public void save(T t) {

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.makePersistent(t);
            tx.commit();
        }

        catch (Exception e) {

            e.printStackTrace();
        }

        finally {

            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }
    }

    public void delete(T t) {

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {

            tx.begin();
            pm.deletePersistent(t);
            tx.commit();
        }

        catch (Exception e) {

            e.printStackTrace();
        }

        finally {

            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    public void deleteAll() {

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {

            tx.begin();
            Query<?> query = pm.newQuery(clazz);
            query.deletePersistentAll();
            tx.commit();
        }

        catch (Exception e) {

            e.printStackTrace();
        }

        finally {

            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }

    }

    public boolean exists(String condition) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        T result = null;

        try {

            tx.begin();

            Extent<?> e = pm.getExtent(clazz, true);
            Query<?> query = pm.newQuery(e);
            query.setUnique(true);
            query.setFilter(condition);

            result = (T) query.execute();

            tx.commit();


        }

        catch (Exception e) {

            e.printStackTrace();
        }

        finally {

            if (tx != null && tx.isActive()) {

                tx.rollback();
            }

            pm.close();
        }

        return result != null;

    }

    public T findBy(String field, Object value) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        T result = null;

        try {

            tx.begin();

            Extent<?> e = pm.getExtent(clazz, true);
            Query<?> query = pm.newQuery(e);
            query.setUnique(true);
            query.setFilter(field + " == :value");

            result = (T) query.execute(value);

            tx.commit();

        }

        catch (Exception e) {

            e.printStackTrace();
        }

        finally {

            if (tx != null && tx.isActive()) {

                tx.rollback();
            }

            pm.close();
        }

        return result;

    }

    public List<T> searchByText(String field, String text, long limit) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        List<T> results = new ArrayList<>();

        try {

            tx.begin();

            Extent<?> e = pm.getExtent(clazz, true);
            Query<?> query = pm.newQuery(e);
            query.setFilter(field + ".startsWith(:value)");
            query.setRange(0, limit);

            results.addAll((List<T>) query.execute(text));

            tx.commit();

        }

        catch (Exception e) {

            e.printStackTrace();
        }

        finally {

            if (tx != null && tx.isActive()) {

                tx.rollback();
            }

            pm.close();
        }

        return results;

    }
}
