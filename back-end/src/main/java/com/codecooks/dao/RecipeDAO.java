package com.codecooks.dao;

import com.codecooks.domain.Recipe;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.List;

/**
 * Recipe DAO class.
 */
public class RecipeDAO extends GenericDAOBase<Recipe> {

    /**
     * Finds recipes that have been added as favourites by
     * the greatest amount of users.
     * @param limit max number of recipes
     * @return most popular recipes
     */
    public List<Recipe> findMostPopular(long limit) {

        PersistenceManager pm = pmf.getPersistenceManager();

        Transaction tx = pm.currentTransaction();
        List<Recipe> results = new ArrayList<>();

        try {

            tx.begin();

            Extent<?> e = pm.getExtent(Recipe.class, true);
            Query<?> query = pm.newQuery(e);
            query.addExtension("datanucleus.query.evaluateInMemory","true"); // TODO Find why it only accepts in-memory evaluation
            query.setOrdering("usersLinkedToFav.size() descending");
            query.setRange(0, limit);

            results.addAll((List<Recipe>) query.execute());

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
