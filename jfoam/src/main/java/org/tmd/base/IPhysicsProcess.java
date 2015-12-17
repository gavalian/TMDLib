/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmd.base;

import java.util.Map;

/**
 *
 * @author gavalian
 */
public interface IPhysicsProcess {
    PhaseSpace          getPhaseSpace();
    UserParamSet        getParamSet();
    double              getWeight(PhaseSpace phaseSpace, UserParamSet params);
}
