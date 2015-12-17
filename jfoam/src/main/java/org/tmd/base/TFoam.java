/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmd.base;


import org.root.data.DataSetXY;
import org.root.func.RandomFunc;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class TFoam {
    public TFoam(){
        
    }
    
    public H1D generate(IPhysicsProcess  process, UserParamSet params){
        H1D  h1 = new H1D("h1",120,0.01,1.0);
        
        DataSetXY  dataset = new DataSetXY();
        PhaseSpace  space = process.getPhaseSpace();
        for(double x = 0.01; x < 1.0; x+=0.005){
            space.getDimension("x").setValue(x);
            double w = process.getWeight(space, params);
            dataset.add(x, w);
        }
        
        RandomFunc random = new RandomFunc(dataset);
        for(int loop = 0; loop < 44000; loop++){
            double num = random.random();
            //System.out.println(num);
            h1.fill(num);
        }
        return h1;
    }
}
