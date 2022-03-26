package com.codecooks.dao;

import com.codecooks.domain.User;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

/**
 * User DAO class.
 */
public class UserDAO extends DataAccessObjectBase implements IDataAccessObject<User> {

    @Override
    public void save(User object) {
        super.saveObject(object);
    }

    @Override
    public void delete(User object) {
        super.deleteObject(object);
    }

    public User getBy(String field, String value) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        User user = null;

        try {

            tx.begin();

            Extent<?> e = pm.getExtent(User.class, true);
            Query<?> query = pm.newQuery(e);
            query.setUnique(true);
            query.setFilter(field + " == '" + value + "'");

            user = (User) query.execute();

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

        return user;

    }

    public boolean exists(String condition) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        User user = null;

        try {

            tx.begin();

            Extent<?> e = pm.getExtent(User.class, true);
            Query<?> query = pm.newQuery(e);
            query.setUnique(true);
            query.setFilter(condition);

            user = (User) query.execute();

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

        return user != null;

    }

}
