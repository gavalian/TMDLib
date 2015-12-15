/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foam.base;

/**
 *
 * @author gavalian
 */
public interface IMCFunc {
    int    getNDim();
    double getWeight(double[] par);
}
