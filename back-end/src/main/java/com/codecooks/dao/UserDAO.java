package com.codecooks.dao;

import com.codecooks.domain.User;

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

}
