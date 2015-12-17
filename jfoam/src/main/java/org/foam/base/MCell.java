/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foam.base;

/**
 *
 * @author gavalian
 */
public class MCell {
    
    double[] cellQ = null;
    double[] cellH = null;
    double[] rloss = null;
    double[] lambda = null;
    double   cellRloss = 0.0;
    double   cellW     = 0.0;
    
    public   MCell(double[] cq, double[] ch){
        this.cellQ = cq;
        this.cellH = ch;
        this.rloss = new double[ch.length];
        this.lambda = new double[ch.length];
        for(int loop = 0; loop < this.rloss.length;loop++){
            this.rloss[loop] = 1.0;
        }
    }
    
    public MCell(int dim){
        
        this.cellQ = new double[dim];
        this.cellH = new double[dim];
        this.rloss = new double[dim];
        this.lambda = new double[dim];
        
        for(int bin = 0; bin < dim;bin++){
            this.cellQ[bin] = 0.0;
            this.cellH[bin] = 1.0;
            this.rloss[bin] = 1.0;
        }
    }
    
    public double[] random(){
        double[]  values = new double[this.cellQ.length];
        this.random(values);
        return values;
    }
    
    public void random(double[] values){
        for(int bin = 0; bin < values.length; bin++){
            double r = Math.random();
            values[bin] = this.cellQ[bin] + r*(this.cellH[bin]);
        }
    }
    
    public int    getDim(){
        return this.cellQ.length;
    }
    
    public void setLambda(int dim, double lam){
        this.lambda[dim] = lam;
    }
    
    public double getLambda(int dim){
        return this.lambda[dim];
    }
    
    public void setRLoss(int dim, double value){
        this.rloss[dim] = value;
    }
    
    public double getRLoss(int dim){
        return this.rloss[dim];
    }
    
    public int getMinRLossDim(){
        int mb = 0;
        for(int loop = 0; loop < this.rloss.length;loop++){
            if(this.rloss[loop]<this.rloss[mb]){
                mb = loop;
            }
        }
        return mb;
    }
    
    public double getSize(){
        double hs = 1.0;
        for(int bin = 0; bin < this.cellH.length; bin++){
            hs = hs*this.cellH[bin];
        }
        return hs;
    }
    
    public double getRLoss(){
        double rl = 0.0;
        for(double v : this.rloss) rl += v;
        return rl;
    }
    
    public double getWeight(){
        return this.cellW;
    }
    
    public void setWeight(double w){
        this.cellW = w;
    }
    
    public void setRLoss(double rl){
        this.cellRloss = rl;
    }
    
    public MCell[]  split(int dim, double lambda){
        double[] cell_1_q = new double[this.cellQ.length];
        double[] cell_1_h = new double[this.cellH.length];
        
        double[] cell_2_q = new double[this.cellQ.length];
        double[] cell_2_h = new double[this.cellH.length];
        
        for(int bin = 0; bin < this.cellH.length; bin++){
            if(bin!=dim){
                cell_1_q[bin] = this.cellQ[bin];
                cell_2_q[bin] = this.cellQ[bin];
                cell_1_h[bin] = this.cellH[bin];
                cell_2_h[bin] = this.cellH[bin];
            } else {
                double fraction = this.cellH[bin]*lambda;
                cell_1_q[bin] = this.cellQ[bin];
                cell_1_h[bin] = fraction;
                cell_2_q[bin] = this.cellQ[bin] + fraction;
                cell_2_h[bin] = this.cellH[bin] - fraction;
            }
        }
        return new MCell[]{new MCell(cell_1_q,cell_1_h), new MCell(cell_2_q,cell_2_h)};
    }
    
    public double getLambda(int dim, double num){
        double lambda = (num-this.cellQ[dim])/this.cellH[dim];
        return lambda;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(
                String.format("[MCELL] DIM = %d WEIGHT = %12.5f RLOSS = %12.5f\n",
                this.cellQ.length,this.cellW, this.getRLoss()));
        for(int bin = 0; bin < this.getDim(); bin++){
            str.append(String.format("\t q/h : %12.5f %12.5f  rloss = %12.6f lambda = %12.5f\n", 
                    this.cellQ[bin],this.cellH[bin],this.rloss[bin],this.lambda[bin]));
        }
        return str.toString();
    }
    /**
     * Main method for running tests on MCell class
     * @param args 
     */
    public static void main(String[] args){
        MCell cell = new MCell(1);
        
        System.out.println(cell);
        
        System.out.println(" ********************************************* ");
        System.out.println(" AFTER SPLITING \n\n");
        
        MCell[]  splitCells = cell.split(0, 0.2);
        for(int loop = 0; loop < splitCells.length; loop++){
            System.out.println(splitCells[loop]);
        }
        /*
        for(int loop = 0; loop < 100; loop++){
            double[] r = splitCells[1].random();
            System.out.println(r[0]);
        }*/
        
        MCell[]  secondFirst  = splitCells[0].split(0, 0.2);
        MCell[]  secondSecond = splitCells[1].split(0, 0.8);
        
        System.out.println(" ********************************************* ");
        System.out.println(" AFTER SPLITING \n\n");
        
        System.out.println(secondFirst[0]);
        System.out.println(secondFirst[1]);
        
        
        System.out.println(secondSecond[0]);
        System.out.println(secondSecond[1]);

        
    }
}
