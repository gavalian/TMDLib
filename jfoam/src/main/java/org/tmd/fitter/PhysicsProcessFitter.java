/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmd.fitter;

import java.util.List;
import org.freehep.math.minuit.FCNBase;
import org.freehep.math.minuit.FunctionMinimum;
import org.freehep.math.minuit.MnMigrad;
import org.freehep.math.minuit.MnScan;
import org.freehep.math.minuit.MnUserParameters;
import org.root.base.IDataSet;
import org.root.func.RealParameter;
import org.tmd.base.IPhysicsProcess;
import org.tmd.base.PhaseSpace;
import org.tmd.base.UserParam;
import org.tmd.base.UserParamSet;

/**
 *
 * @author gavalian
 */
public class PhysicsProcessFitter implements FCNBase {

    IPhysicsProcess  physicsProcess = null;
    IDataSet         physicsDataSet = null;
    
    public PhysicsProcessFitter(IPhysicsProcess  proc, IDataSet ds){
        this.physicsProcess = proc;
        this.physicsDataSet = ds;
    }
    
    public IPhysicsProcess  getProcess(){
        return this.physicsProcess;
    }
    
    @Override
    public double valueOf(double[] doubles) {
        double chi2 = 0;
        double[] pr = new double[doubles.length-1];
        for(int loop = 0; loop < pr.length; loop++) pr[loop] = doubles[loop+1];
        UserParamSet set   = this.physicsProcess.getParamSet();
        PhaseSpace   space = this.physicsProcess.getPhaseSpace();
        set.setParams(pr);

        for(int loop = 0; loop < this.physicsDataSet.getDataSize(); loop++){
            double x = this.physicsDataSet.getDataX(loop);
            double y = this.physicsDataSet.getDataY(loop);
            space.getDimension("x").setValue(x);
            double w = doubles[0]*this.physicsProcess.getWeight(space, set);
            //System.out.println(" w = " + w + " x = " + x + " y = " + y);
            //System.out.println(space);
            if(y!=0){
                chi2 += (y-w)*(y-w);
            }
        }
        /*
        System.out.println(set);
        System.out.println("chi2 = " + doubles[0] + " " + chi2);
        */
        return chi2;
    }
    
    public static UserParamSet  modelFit(IPhysicsProcess process, IDataSet ds){
        PhysicsProcessFitter  fitter = new PhysicsProcessFitter(process,ds);
        UserParamSet  pset = fitter.getProcess().getParamSet();
        int npars = pset.getCount();
        
        
        
        MnUserParameters upar = new MnUserParameters();
        upar.add("amp",20,0.00000001);
        upar.setLimits("amp", 0, 10000000);
        List<UserParam>  upList = pset.getList();
        System.out.println("*****************************");
        System.out.println(pset);

        for(UserParam p : upList){
            upar.add(p.getName(), p.getValue(),0.001);
            //upar.setLimits(p.getName(),p.getMin(),p.getMax());
            upar.setLimits(p.getName(),0.0,10.0);
        }
        System.out.println(upar);        
        MnScan  scanner = new MnScan(fitter,upar);
        FunctionMinimum scanmin = scanner.minimize(); 
        System.out.println(" SCANNER = ");
        System.out.println(scanmin);
        System.out.println(" SCANNER =  END");
        
        MnMigrad migrad = new MnMigrad(fitter, upar);
        FunctionMinimum min = migrad.minimize();
        
        MnUserParameters userpar = min.userParameters();
        
        //System.out.println(upar);
        //System.out.println(min);
        //System.out.println(userpar);
        UserParamSet  paramSet = new UserParamSet();
        
        for(UserParam p : upList){            
             System.out.println(p.getName() + " = " + userpar.value(p.getName()));
             paramSet.addParam(p.getName(), userpar.value(p.getName()));
        }
        return paramSet;
    }
}
