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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.root.histogram.GraphErrors;
import org.root.histogram.H1D;
import org.root.pad.TGCanvas;

/**
 *
 * @author gavalian
 */
public class MCellExplorer {
    
    int  nSamplesExplore = 1000;
    int  cellLambdaBins  = 40;
    List<H1D>  hLambda                = new ArrayList<H1D>();    
    List<H1D>  hLambdaReiman          = new ArrayList<H1D>();
    List<GraphErrors>  graphRLoss     = new ArrayList<GraphErrors>();
    List<Double>       derivedLambdas = new ArrayList<Double> ();
    List<Double>       derivedRLoss   = new ArrayList<Double> ();
    MCHist             mcHist         = new MCHist();
    
    public MCellExplorer(){
        
    }
    
    public void setNSample(int sample){
        this.nSamplesExplore = sample;
    }
    
    public void setHbins(int bins){
        this.cellLambdaBins = bins;
    }
    
    public void exploreCell(MCell cell, IMCFunc func){
        
        double weight = 0.0;

        int ndim = cell.getDim();
        hLambda.clear();
        
        for(int loop = 0; loop < ndim; loop++){
            hLambda.add(new H1D("DIM_L_"+loop,this.cellLambdaBins,0.0,1.0));
        }
        
        for(int loop = 0; loop < this.nSamplesExplore; loop++){
            double[] par  = cell.random();
            double result = func.getWeight(par);
            weight += result/this.nSamplesExplore;
            for(int dim = 0; dim < par.length; dim++){
                double lambda = cell.getLambda(dim, par[dim]);
                hLambda.get(dim).fill(lambda, result);
            }
        }
        
        cell.setWeight(weight*cell.getSize());
    
        
        for(int loop = 0; loop < this.hLambda.size(); loop++){
            H1D h1  = this.hLambda.get(loop);
            int bin = h1.getMaximumBin();
            double factor = h1.getBinContent(bin);
            h1.divide(factor);
        }
        
        
        for(int dim = 0; dim < this.hLambda.size();dim++){
            H1D h = this.hLambda.get(dim);
            mcHist.analyze(h);
            cell.setRLoss(dim, mcHist.getRLoss());
            cell.setLambda(dim, mcHist.getLambda());            
        }
    }
    
    public List<H1D>  getLambdaHistList(){
        return this.hLambda;
    }
    
    public List<H1D>  getLambdaHistListNorm(){
        return this.hLambdaReiman;
    }
    
    public static void main(String[] args){
        
        ExpFunction1D expFunc = new ExpFunction1D();
        GaussFunction2D gausFunc = new GaussFunction2D();
        StepFunction1D stpFunc = new StepFunction1D();
        MCellExplorer explorer = new MCellExplorer();
        MCell         cell     = new MCell(1);
        
        MCell         cell2d     = new MCell(2);
        
        
        //explorer.exploreCell(cell2d, gausFunc);
        System.out.println(cell2d);
        
        //explorer.exploreCell(cell, stpFunc);
        explorer.exploreCell(cell, expFunc);
        //explorer.exploreCell(cells[0], expFunc);

        /*
        MCell[]      cells   = cell.split(0, cell.getLambda(0));
        System.out.println(cell);

        explorer.exploreCell(cells[0], stpFunc);
        explorer.exploreCell(cells[1], stpFunc);
         System.out.println(cells[0]);
         System.out.println(cells[1]);
        //System.out.println(cells[0]);
        */
        /*
        TGCanvas c1 = new TGCanvas("c1","",900,600,1,2);
        c1.cd(0);
        c1.draw(explorer.getLambdaHistList().get(0));
        c1.cd(1);
        c1.draw(explorer.getLambdaHistList().get(1));
                */
        //c1.cd(1);
        //c1.draw(explorer.getGraphs().get(0));
        
        
        TGCanvas c2 = new TGCanvas("c2","",600,600,1,2);
        MCHist mcH = new MCHist();
        GraphErrors graph = new GraphErrors();
        
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
        mcH.analyze(explorer.getLambdaHistList().get(0));
    }
}
