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

    /**
     * Saves a single object.
     * @param t object to save
     */
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

    /**
     * Deletes a single object.
     * @param t object to delete
     */
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

    /**
     * Deletes all objects belonging to the class handled by the DAO.
     */
    public void deleteAll() {

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {

            tx.begin();
            Extent<?> extent = pm.getExtent(clazz, true);
            Query<?> query = pm.newQuery(extent);
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

    /**
     * Checks whether an object was previously saved.
     * @param condition JDOQL filter to check
     * @return true if the associated object was found, false otherwise
     */
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

    /**
     * Finds all saved objects belonging to the class handled by the DAO.
     * @return list of objects
     */
    public List<T> findAll() {

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        List<T> results = new ArrayList<>();

        try {

            tx.begin();
            Extent<?> extent = pm.getExtent(clazz, true);
            Query<?> query = pm.newQuery(extent);
            results.addAll((List<T>) query.execute());
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

    /**
     * Finds an object by means of a single attribute.
     * @param field object attribute
     * @param value value to check against
     * @return object if it was found, null otherwise
     */
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

    /**
     * Finds all objects that match the starting characters of
     * a text comparing it with a particular string attribute.
     * @param field object string attribute
     * @param text string to compare
     * @param limit max number of objects
     * @return list of objects
     */
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
