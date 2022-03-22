package com.codecooks.dao;

import com.codecooks.domain.Recipe;

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
}
