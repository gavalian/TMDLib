/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.grid.utils;

/**
 *
 * @author dmriser
 * @param <T>
 */
public interface IGridContent<T> {
    void setContent(T content);
    T getContent();
    T empty();
    void add(T a); 
    void multiply(T a);
    void reset();
}
