/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foam.base;

import org.foam.test.StepFunction1D;
import org.foam.test.GaussFunction2D;
import org.foam.test.ExpFunction1D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.clas.tools.benchmark.BenchmarkTimer;
import org.root.data.DataVector;
import org.root.histogram.H1D;
import org.root.histogram.H2D;
import org.root.pad.TGCanvas;

/**
 *
 * @author gavalian
 */
public class MCFoam {
    
    MCellExplorer  explorer = new MCellExplorer();
    List<MCell>   cellStore = new ArrayList<MCell>();
    DataVector    cellVector = new DataVector();
    
    int           maxNumberOfCells = 25000;
    
    public MCFoam(){
        
    }
    
    
    public void setFunction(IMCFunc func){
        
        MCell cell = new MCell(func.getNDim());
        
        this.cellStore.add(cell);
        this.explorer.exploreCell(cell, func);
        
        BenchmarkTimer  timer = new BenchmarkTimer("CELL-DIVISION");
        while(this.cellStore.size()<this.maxNumberOfCells){

            timer.resume();
            int bestCandidateIndex = 0;
            int bestCandidateDim   = 0;
            double bestRLOSS       = this.cellStore.get(0).getRLoss(0);
            //
            // FIND BEST DIMENSION
            for(int loop = 1; loop < this.cellStore.size(); loop++){
                MCell mc = this.cellStore.get(loop);
                for(int dim = 0; dim < mc.getDim(); dim++){
                    if(mc.getRLoss(dim)<bestRLOSS){
                        bestRLOSS = mc.getRLoss(dim);
                        bestCandidateIndex = loop;
                        bestCandidateDim   = dim;
                    }
                }
            }
            
            MCell divCell = this.cellStore.get(bestCandidateDim);
            
            this.cellStore.remove(bestCandidateDim);
            
            MCell[]  dauCells = divCell.split(bestCandidateDim, divCell.getLambda(bestCandidateDim));
            //System.out.println(" SPLITTING CELL " + bestCandidateIndex);
            explorer.exploreCell(dauCells[0], func);
            explorer.exploreCell(dauCells[1], func);
            
            this.cellStore.add(dauCells[0]);
            this.cellStore.add(dauCells[1]);
            timer.pause();
        }
        double totalWeight = 0;
        for(MCell mc : this.cellStore){
            totalWeight += mc.getWeight();
            System.out.println(mc);
        }
        
        double totalIntegral = 0.0;
        for(int bin = 0; bin < this.cellStore.size();bin++){
            totalIntegral +=  this.cellStore.get(bin).getWeight()/totalWeight;
            this.cellVector.add(totalIntegral);
        }
        
        System.out.println(timer);
    }
    
    
    public double[]  getRandom(){
        double rand = Math.random();
        int bin = this.cellVector.findBin(rand);
        //System.out.println("rand " + rand + "  bin " + bin);
        return this.cellStore.get(bin).random();
    }
    public H1D  getCellSizes(){
        H1D h1 = new H1D("SIZES",this.cellStore.size(),0.0,1.0);
        for(int bin = 0; bin < this.cellStore.size();bin++){
            h1.setBinContent(bin, this.cellStore.get(bin).getSize());
        }
        return h1;
    }
    
    public H1D  getCellWeights(){
        H1D h1 = new H1D("SIZES",this.cellStore.size(),0.0,1.0);
        for(int bin = 0; bin < this.cellStore.size();bin++){
            h1.setBinContent(bin, this.cellStore.get(bin).getWeight());
        }
        return h1;
    }
    public static void main(String[] args){
        ExpFunction1D expFunc = new ExpFunction1D();
        StepFunction1D stpFunc = new StepFunction1D();
        GaussFunction2D gausFunc = new GaussFunction2D();
        
        MCFoam foam = new MCFoam();
        
        //foam.setFunction(expFunc);
        //foam.setFunction(stpFunc);
        foam.setFunction(gausFunc);
        
        TGCanvas c1 = new TGCanvas("c1","",900,800,2,2);
        c1.cd(0);
        H1D hs = foam.getCellSizes();
        hs.setFillColor(3);
        c1.draw(hs);
        c1.cd(1);
        H1D hw = foam.getCellWeights();
        hw.setFillColor(4);
        c1.draw(hw);
        
        H1D  hr  = new H1D("HR",200,0.0,1.0);
        H2D  hrg = new H2D("HR",60,0.0,1.0,60,0.0,1.0);
        
        for(int loop = 0; loop < 30000; loop++){
            double[] rand = foam.getRandom();
            hr.fill(rand[0]);  
            hrg.fill(rand[0], rand[1]);
        }
        c1.cd(2);
        c1.draw(hr);
        c1.cd(3);
        c1.draw(hrg);
    }
}
