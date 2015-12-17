/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmd.process;

import org.tmd.base.IPhysicsProcess;
import org.tmd.base.PhaseSpace;
import org.tmd.base.UserParamSet;

/**
 *
 * @author gavalian
 */
public class ElasticScattering implements IPhysicsProcess {

    public PhaseSpace getPhaseSpace() {
        PhaseSpace  space = new PhaseSpace();
        space.add("theta",0.1,0.5);
        return space;
    }

    public UserParamSet getParamSet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getWeight(PhaseSpace phaseSpace, UserParamSet params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
