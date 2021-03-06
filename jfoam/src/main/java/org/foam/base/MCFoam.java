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
import org.root.histogram.DataBox;
import org.root.histogram.GraphErrors;
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
    
    int           maxNumberOfCells      = 25000;
    int           exploreMaxSample      = 1000;
    int           exploreHistogramBins  = 80;
    double        mcTotalWeight         = 0.0;
    
    
    public MCFoam(){
        
    }
    
    public void setNSample(int sample){
        this.exploreMaxSample = sample;
        this.explorer.setNSample(sample);
    }
    
    
    public void setMaxCells(int mcells){
        this.maxNumberOfCells = mcells;
    }
    
    public void setLambdaBins(int bins){
        this.exploreHistogramBins = bins;
        this.explorer.setHbins(bins);
    }
    
    public GraphErrors  getCellGraph(int dim1, int dim2){
        GraphErrors graph = new GraphErrors();
        for(MCell cell : this.cellStore){
            double x = cell.cellQ[dim1] + 0.5*cell.cellH[dim1];
            double y = cell.cellQ[dim2] + 0.5*cell.cellH[dim2];
            graph.add(x, y);
        }
        return graph;
    }
    
    public GraphErrors  getCellGraph(int dim1){
        GraphErrors graph = new GraphErrors();
        for(MCell cell : this.cellStore){
            double x = cell.cellQ[dim1] + 0.5*cell.cellH[dim1];
            double y = 0.5;
            graph.add(x, y);
        }
        return graph;
    }
    
    public List<DataBox> getCellBoxes(int d1, int d2){
        List<DataBox>  boxes = new ArrayList<DataBox>();
        for(MCell cell : this.cellStore){
            boxes.add(new DataBox(cell.cellQ[d1],cell.cellQ[d2],cell.cellH[d1],cell.cellH[d2]));
        }
        return boxes;
    }
    
    public void setFunction(IMCFunc func){
        
        MCell cell = new MCell(func.getNDim());
        
        this.cellStore.add(cell);
        this.explorer.exploreCell(cell, func);
        
        BenchmarkTimer  timer = new BenchmarkTimer("CELL-DIVISION");
        int  iDivisionCounter    = 0;
        int  numberOfBoxesToShow = 0;
        while(this.cellStore.size()<this.maxNumberOfCells){

            iDivisionCounter++;
            if(iDivisionCounter%500==0){
                numberOfBoxesToShow = iDivisionCounter/500;

                for(int loop = 0; loop < numberOfBoxesToShow; loop++) System.out.print("-");
                System.out.print("> " + iDivisionCounter);
                if(numberOfBoxesToShow%10==0) System.out.println();
                System.out.print("\r");
            }
            
            timer.resume();
            int bestCandidateIndex = 0;
            int bestCandidateDim   = 0;
            //double bestRLOSS       = this.cellStore.get(0).getRLoss(0);
            double bestWEIGHT      = this.cellStore.get(0).getWeight();
            //
            // FIND BEST DIMENSION
            for(int loop = 0; loop < this.cellStore.size(); loop++){
                MCell mc = this.cellStore.get(loop);
                if(mc.getWeight()>bestWEIGHT){
                        //if(mc.getRLoss(dim)>bestRLOSS&&mc.getSize()>0.0001){                        
                        bestWEIGHT = mc.getWeight();
                        bestCandidateIndex = loop;
                }
            }
            double bestRLOSS       = this.cellStore.get(bestCandidateIndex).getRLoss(0);
            MCell  sCell = this.cellStore.get(bestCandidateIndex);
            for(int dim = 0; dim < sCell.getDim(); dim++){
                if(sCell.getRLoss(dim)>bestRLOSS){
                    bestRLOSS = sCell.getRLoss(dim);
                    bestCandidateDim = dim;
                }
            }
            
            for(MCell c2 : this.cellStore){
                //System.out.println(c2);
            }
            //System.out.println("[DIVISION] --->  INDEX = " + bestCandidateIndex 
            //        + "  DIM = " + bestCandidateDim + "  RLOSS = " + bestRLOSS);
            
            MCell divCell = this.cellStore.get(bestCandidateIndex);            
            this.cellStore.remove(bestCandidateIndex);
            
            
            
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
            //System.out.println(mc);
        }
        
        this.mcTotalWeight = totalWeight;
        
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
