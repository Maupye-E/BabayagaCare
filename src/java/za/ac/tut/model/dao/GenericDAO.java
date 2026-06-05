/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.dao;

import java.util.List;

/**
 * Generic DAO interface with common CRUD operations
 * 
 * @author USER
 * @param <T> Entity type
 * @param <ID> Primary key type
 */
public interface GenericDAO<T, ID> {
    
    // Create / Update
    T save(T entity);
    T update(T entity);
    
    // Read
    T findById(ID id);
    List<T> findAll();
    
    // Delete
    void delete(T entity);
    void deleteById(ID id);
    
    // Check existence
    boolean existsById(ID id);
    long count();
}