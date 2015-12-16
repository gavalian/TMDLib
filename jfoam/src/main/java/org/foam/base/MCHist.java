/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foam.base;

import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class MCHist {
    
    H1D profileHist     = null;
    H1D bestProfileHist = null;
    double   bestLambda = 0.0;
    double   bestRloss  = 1.0;
    int      bestBin    = 0;
    int      binProgressL = 0;
    int      binProgressH = 0;
    
    public MCHist(){
        
    }
    
    
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

    public void analyze(H1D h){
        int nbins = h.getXaxis().getNBins();
        this.bestRloss = 0.0;
        for(int loop = 1; loop < nbins-2; loop++){
            double rloss = this.buildProfile(h, loop);
            //System.out.println("    ************ RLOSS CHANGE " + rloss);
            if(rloss>this.bestRloss){
                //System.out.println("    ************ RLOSS CHANGE " + rloss);
                this.bestRloss = rloss;
                if(this.binProgressL==0){
                    this.bestLambda = h.getXaxis().getBinCenter(this.binProgressH);
                } else {
                    this.bestLambda = h.getXaxis().getBinCenter(this.binProgressL);
                }
            }
        }
        System.out.println(" ANALYSIS RESULT = " + this.bestLambda + "  " + this.bestRloss);
    }
    
    
    public double getLambda(){return this.bestLambda;}
    public double getRLoss() { return this.bestRloss;}
    
    public double buildProfile(H1D h, int hbin){
        int nbins = h.getXaxis().getNBins();
        this.profileHist = new H1D("profile",nbins,0.0,1.0);        
        int binL = this.getPrevBinShift(h, hbin);
        int binH = this.getNextBinShift(h, hbin);
        
        //System.out.println(" L/H = " + binL + " " + binH);
        for(int bin = 0; bin < nbins; bin++){
            if(bin>=binL&&bin<=binH){
                this.profileHist.setBinContent(bin, h.getBinContent(hbin));                
            } else {
                this.profileHist.setBinContent(bin, 1.0);
            }            
        }
        
        double bw = h.getXaxis().getBinWidth(0);
        double rlossp = 1.0-h.integral()*bw;
        double rloss  = (this.profileHist.integral() - h.integral())*bw;
        this.binProgressL = binL;
        this.binProgressH = binH;        
        //String titleString = String.format(" RLOSS PARENT = %12.5f RLOSS = %12.5f", rlossp,rloss);
        //this.profileHist.setTitle(String.format(" RLOSS PARENT = %12.5f RLOSS = %12.5f", rlossp,rloss));
        //System.out.println(titleString);
        //return (rlossp-rloss)/rlossp;
        return (rlossp-rloss)/rlossp;
    }
    
    public H1D  getProfile(){ return this.profileHist;}
}
