package com.codecooks.dao;

import javax.jdo.*;

/**
 * Base class that implements most common db access operations.
 */
public abstract class DataAccessObjectBase {

    protected static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("RecipeMaster");

    public void saveObject(Object object) {

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.makePersistent(object);
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

    public void deleteObject(Object object) {

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();

        try {

            tx.begin();
            pm.deletePersistent(object);
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

    public abstract boolean exists(String condition);
}
