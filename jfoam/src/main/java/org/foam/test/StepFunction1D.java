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
public class StepFunction1D implements IMCFunc {

    @Override
    public double getWeight(double[] par) {
        if(par[0]<0.2) return 4.0;
        if(par[0]>0.8) return 4.0;
        if(par[0]>0.4&&par[0]<0.6) return 2.0;
        return 0.4;
    }

    @Override
    public int getNDim() {
        return 1;
    }
    
    
}
