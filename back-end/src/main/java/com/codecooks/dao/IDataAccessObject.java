package com.codecooks.dao;

/**
 * Template interface for classes that behave as DAOs.
 * @param <T> Associated domain class
 */
public interface IDataAccessObject<T> {

    void save(T object);
    void delete(T object);
}
