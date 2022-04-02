package com.codecooks.dao;

import com.codecooks.domain.Recipe;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

/**
 * Recipe DAO class.
 */
public class RecipeDAO extends DataAccessObjectBase implements IDataAccessObject<Recipe> {

    @Override
    public void save(Recipe object) {
        super.saveObject(object);
    }

    @Override
    public void delete(Recipe object) {
        super.deleteObject(object);
    }

    public Recipe getBy(String field, String value) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        Recipe recipe = null;

        try {
            tx.begin();
            Extent<?> e = pm.getExtent(Recipe.class, true);
            Query<?> query = pm.newQuery(e);
            query.setUnique(true);
            query.setFilter(field + " == '" + value + "'"); //Que exista el nombre de la receta TODO

            recipe = (Recipe) query.execute();

            tx.commit();
            return recipe;

        }

        catch (Exception e) {
            System.err.println("! Error obteniendo receta");
            e.printStackTrace();
            return recipe;
        }

        finally {

            if (tx != null && tx.isActive()) {

                tx.rollback();
            }
            pm.close();
        }


    }

    @Override
    public boolean exists(String condition) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        Recipe recipe = null;

        try {
            tx.begin();

            Extent<?> e = pm.getExtent(Recipe.class, true);
            Query<?> query = pm.newQuery(e);
            query.setUnique(true);
            query.setFilter(condition);

            recipe = (Recipe) query.execute();

            tx.commit();
        }

        catch (Exception e) {
            System.err.println("! Error obteniendo receta");
            e.printStackTrace();
        }

        finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }

        return recipe != null;
    }
}
