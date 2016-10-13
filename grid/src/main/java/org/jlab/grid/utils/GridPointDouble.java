/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base;

/**
 *
 * @author dmriser
 */
public class GridPointDouble implements IGridContent<Double>{
    
    Double binContent; 
    
    public GridPointDouble(){
        binContent = 0.0; 
    }
//    
//    public void GridPointDouble(double value){
//        binContent = value; 
//    }

    @Override
    public void setContent(Double value) {
        binContent = value; 
    }

    @Override
    public Double getContent() {
        return binContent; 
    }

    @Override
    public void add(Double a) {
        binContent += a; 
    }

    @Override
    public void multiply(Double a) {
        binContent *= a; 
    }

    @Override
    public void reset() {
        binContent = 0.0;
    }
    
    @Override
    public Double empty(){
        return new Double(0.0);
    }
    
}
