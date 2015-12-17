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
    
    double x1 = 0.6;
    double y1 = 0.6;
    double x2 = 0.3;
    double y2 = 0.3;
    
    @Override
    public double getWeight(double[] par) {
        if(par.length!=2) return 0.0;

        double x = par[0];
        double y = par[1];
        double r1 = (x-x1)*(x-x1) + (y-y1)*(y-y1);
        double r2 = (x-x2)*(x-x2) + (y-y2)*(y-y2);
        double gx = FunctionFactory.gauss(r1, 0.0, 0.025);
        double gy = FunctionFactory.gauss(r2, 0.0, 0.025);
        
        return 1.2 + 6.5*gy + 8.5*gx;
        //return  3.5*gx;
    }

    @Override
    public int getNDim() {
        return 2;
    }
    
    
    
}
