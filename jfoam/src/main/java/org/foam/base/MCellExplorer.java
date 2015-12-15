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
import org.root.histogram.GraphErrors;
import org.root.histogram.H1D;
import org.root.pad.TGCanvas;

/**
 *
 * @author gavalian
 */
public class MCellExplorer {
    
    int  nSamplesExplore = 4000;
    int  cellLambdaBins  = 80;
    List<H1D>  hLambda                = new ArrayList<H1D>();    
    List<H1D>  hLambdaReiman          = new ArrayList<H1D>();
    List<GraphErrors>  graphRLoss     = new ArrayList<GraphErrors>();
    List<Double>       derivedLambdas = new ArrayList<Double> ();
    List<Double>       derivedRLoss   = new ArrayList<Double> ();
    
    public MCellExplorer(){
        
    }
    
    
    public void integrate(){
        this.hLambdaReiman.clear();
        for(int loop = 0; loop < this.hLambda.size(); loop++){
            H1D h1L = this.hLambda.get(loop);
            H1D h1I = new H1D(h1L.getName(),h1L.getXaxis().getNBins(),0.0,1.0);
            double entries = h1L.integral();
            double summ = 0;
            for(int bin = 0; bin < h1I.getXaxis().getNBins(); bin++){
                summ += h1L.getBinContent(bin)/entries;
                h1I.setBinContent(bin, summ);
            }
            this.hLambdaReiman.add(h1I);
        }
    }
    
    
    private double getLoss(H1D h1, int b1, int b2){
        
        double bw  = h1.getXaxis().getBinWidth(0);
        double bh1 = h1.getBinContent(b1);
        double bh2 = h1.getBinContent(b2);
        double bx1 = h1.getXaxis().getBinCenter(b1);
        double bx2 = h1.getXaxis().getBinCenter(b2);
        double i1 = h1.integral(0, b1);
        double i2 = h1.integral(b1,b2);
        double i3 = h1.integral(b2, h1.getXaxis().getNBins()-1);
        double rlossparent = 1.0 - (i1+i2+i3)*bw;
        double rloss       = bx1*1.0 + (bx2-bx1)*bh1 + (1.0-bx2)*1.0;
        rloss = rloss - (i1+i2+i3)*bw;
        //System.out.println(String.format("%3d %3d  %12.5f %12.5f", 
        //        b1,b2,rlossparent,rloss));
        return rloss / rlossparent;
    }
    
    public void findLambdas(){
        
        for(int dim = 0; dim < this.hLambdaReiman.size(); dim++){
            GraphErrors  graph = new GraphErrors();
            graph.setMarkerSize(5);
            graph.setMarkerColor(1);
            graph.setFillColor(3);
            this.graphRLoss.add(graph);
            
            H1D h1R   = this.hLambdaReiman.get(dim);
            int nbins = h1R.getXaxis().getNBins();
            int best_bin = 0;
            double best_lambda = 0.0;
            
            double rloss = 100000.0;
            double totalintegral = h1R.integral()*h1R.getXaxis().getBinWidth(0);
            
            for(int bin = 0; bin < nbins; bin++){
                double height = h1R.getBinContent(bin);
                double lambda = h1R.getXaxis().getBinCenter(bin);
                double sqint  = height*lambda + (1.0-lambda)*1.0;
                double rlossB  =  (sqint-totalintegral)/totalintegral;
                graph.add(lambda, rlossB);
                if(rlossB<rloss){
                    rloss = rlossB;
                    best_bin = bin;
                    best_lambda = lambda;
                }
                System.out.println(
                        String.format("L = %12.5f RLOSS = %12.5f ",
                                lambda ,rlossB));
                //double in1    = h1R.integral(0, bin-1);
                //double in2    = h1R.integral(bin,nbins-1);
                
            }
            this.derivedLambdas.add(best_lambda);
            this.derivedRLoss.add(rloss);
        }
        
        for(int dim = 0; dim < this.derivedLambdas.size(); dim++){
            System.out.println(String.format("dim %3d : lambda = %12.7f %14.8f", 
                    dim,this.derivedLambdas.get(dim),this.derivedRLoss.get(dim)));
        }
    }
    
    public List<GraphErrors>  getGraphs(){
        return this.graphRLoss;
    }
    /**
     * Find the bin for lambda 2 for given lambda 1
     * @param h
     * @param start_bin
     * @return 
     */
    private int getNextBinShift(H1D h, int start_bin){
        double content = h.getBinContent(start_bin);
        
        for(int bin = start_bin+1; bin < h.getXaxis().getNBins();bin++){
            if(h.getBinContent(bin)>content) return (bin-1);
        }
        return h.getXaxis().getNBins()-1;
    }
    
    private int getPrevBinShift(H1D h, int start_bin){
        double content = h.getBinContent(start_bin);
        
        for(int bin = start_bin; bin > 0;bin--){
            if(h.getBinContent(bin)>content) return (bin+1);
        }
        return 0;
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
            
            H1D h1   = this.hLambda.get(dim);
            int bins = h1.getXaxis().getNBins();
            int bbL  = 0;
            int bbH  = 0;
            double brl = 1000.0;
            int start_bin = 1;
            for(int binS = start_bin; binS < bins-1; binS++){
                int binH = this.getNextBinShift(h1, binS);
                int binL = this.getPrevBinShift(h1, binS);
                double loss = getLoss(h1,binL,binH);
                System.out.println(binS + " " + binL + "   " + binH + "  loss = " + loss);

                if(loss<brl && loss > 0){
                    bbL = binL;
                    bbH = binH;
                    brl = loss;
                }

                    /*
                    System.out.println(" BEST = " + bbL + " ( " + 
                            h1.getXaxis().getBinCenter(bbL)
                            +
                            
                            ") " + bbH +  "   (" +
                            h1.getXaxis().getBinCenter(bbL) + ")  RL = " + brl);
                   */
            }
            
            cell.setRLoss(dim, brl);
            if(bbL==0||bbH==h1.getXaxis().getNBins()-1){
                cell.setRLoss(dim, 1.0);
                cell.setLambda(dim, 0.5);
            } else {
                if(bbL!=0){
                    cell.setLambda(dim, h1.getXaxis().getBinCenter(bbL)-
                            0.5*h1.getXaxis().getBinWidth(bbL));
                } else {
                    cell.setLambda(dim, h1.getXaxis().getBinCenter(bbH)+
                            0.5*h1.getXaxis().getBinWidth(bbH));
                }
            }
            
            /*
            System.out.println(" BEST = " + bbL + " ( " + 
                    h1.getXaxis().getBinCenter(bbL)
                    +
                    
                    ") " + bbH +  "   (" +
                    h1.getXaxis().getBinCenter(bbH) + ")  RL = " + brl);            
              */      
        }
        //this.integrate();
        //this.findLambdas();
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
        
        
        explorer.exploreCell(cell2d, gausFunc);
        System.out.println(cell2d);
        
        //explorer.exploreCell(cell, stpFunc);
        //explorer.exploreCell(cell, expFunc);
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
        TGCanvas c1 = new TGCanvas("c1","",900,600,1,2);
        c1.cd(0);
        c1.draw(explorer.getLambdaHistList().get(0));
        c1.cd(1);
        c1.draw(explorer.getLambdaHistList().get(1));
        //c1.cd(1);
        //c1.draw(explorer.getGraphs().get(0));
        
    }
}
