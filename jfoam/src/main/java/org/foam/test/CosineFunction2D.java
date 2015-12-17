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
public class CosineFunction2D implements IMCFunc {
    
    double x1 = 0.0;
    double y1 = 0.6;
    double x2 = 0.3;
    double y2 = 0.3;
    
    public int getNDim() {
        return 2;
    }

    public double getWeight(double[] par) {
        double x = par[0];
        double y = par[1];
        double r1 = (x-x1)*(x-x1) + (y-y1)*(y-y1);
        double func = 2.2 + Math.sin(r1*20);
        return func;
    }
    
}
