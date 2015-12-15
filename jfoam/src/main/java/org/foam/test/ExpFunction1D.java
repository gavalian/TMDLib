/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foam.test;

import org.foam.base.IMCFunc;

/**
 *
 * @author gavalian
 */
public class ExpFunction1D implements IMCFunc {

    @Override
    public double getWeight(double[] par) {
        return Math.pow(par[0], 1.3)*Math.pow((1-par[0]), 2.5);
    }

    @Override
    public int getNDim() {
        return 1;
    }
    
}
