/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foam.test;

import org.foam.base.IMCFunc;
import org.jlab.data.func.FunctionFactory;

/**
 *
 * @author gavalian
 */
public class GaussFunction2D implements IMCFunc {

    @Override
    public double getWeight(double[] par) {
        if(par.length!=2) return 0.0;
        double x = par[0];
        double y = par[1];
        double gx = FunctionFactory.gauss(x, 0.6, 0.1);
        double gy = FunctionFactory.gauss(y, 0.4, 0.1);
        
        return 2.5*gy + 3.5*gx;
        //return  3.5*gx;
    }

    @Override
    public int getNDim() {
        return 2;
    }
    
    
    
}
