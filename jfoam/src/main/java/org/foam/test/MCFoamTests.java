/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foam.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foam.base.MCHist;
import org.foam.base.MCell;
import org.foam.base.MCellExplorer;
import org.root.histogram.GraphErrors;
import org.root.histogram.H1D;
import org.root.pad.TGCanvas;

/**
 *
 * @author gavalian
 */
public class MCFoamTests {
    
    public static void explorerTest(){
        ExpFunction1D expFunc = new ExpFunction1D();
        MCell         cell     = new MCell(1);
        MCellExplorer explorer = new MCellExplorer();
        
        explorer.exploreCell(cell, expFunc);
        System.out.println(cell);
        
        TGCanvas c2 = new TGCanvas("c2","",600,600,1,2);
        MCHist mcH = new MCHist();
        GraphErrors graph = new GraphErrors();
        MCell[]  cells = cell.split(0, cell.getLambda(0));
        
        explorer.exploreCell(cells[1], expFunc);
        for(int loop = 0; loop < 78; loop++){
            H1D    h   = explorer.getLambdaHistList().get(0);
            double lambda = h.getXaxis().getBinCenter(loop);            
            double rloss  = mcH.buildProfile(h, loop+1);
            graph.add(lambda, rloss);
            c2.cd(0);
            
            H1D hp = mcH.getProfile();            
            hp.setLineColor(2);
            c2.draw(hp);
            c2.draw(h,"same");                        
            c2.cd(1);
            c2.draw(graph);
            c2.update();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MCellExplorer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public static void main(String[] args){
        MCFoamTests.explorerTest();
    }
}
