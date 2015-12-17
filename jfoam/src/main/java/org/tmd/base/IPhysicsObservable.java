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
public interface IPhysicsObservable {
    PhaseSpace          getPhaseSpace();
    double              getValue(PhaseSpace pSpace);
}
