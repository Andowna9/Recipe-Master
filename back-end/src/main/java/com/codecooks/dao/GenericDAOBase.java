package com.codecooks.dao;

import javax.jdo.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

    public boolean exists(String condition) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        T t = null;

        try {

            tx.begin();

            Extent<?> e = pm.getExtent(clazz, true);
            Query<?> query = pm.newQuery(e);
            query.setUnique(true);
            query.setFilter(condition);

            t = (T) query.execute();

            tx.commit();


        }

        catch (Exception e) {

            System.err.println("! Error obteniendo usuario por email");
            e.printStackTrace();
        }

        finally {

            if (tx != null && tx.isActive()) {

                tx.rollback();
            }

            pm.close();
        }

        return t != null;

    }

    public T findBy(String field, String value) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        T t = null;

        try {

            tx.begin();

            Extent<?> e = pm.getExtent(clazz, true);
            Query<?> query = pm.newQuery(e);
            query.setUnique(true);
            query.setFilter(field + " == '" + value + "'");

            t = (T) query.execute();

            tx.commit();


        }

        catch (Exception e) {

            System.err.println("! Error obteniendo usuario por email");
            e.printStackTrace();
        }

        finally {

            if (tx != null && tx.isActive()) {

                tx.rollback();
            }

            pm.close();
        }

        return t;

    }
}
