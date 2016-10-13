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
public class GridPointInteger implements IGridContent<Integer>{

    Integer binContent; 
    
    public GridPointInteger(){
        binContent = 0; 
    }
    
//    public void GridPointInteger(Integer value){
//        binContent = value; 
//    }
    
    public GridPointInteger(int value) {
        binContent = (Integer) value; 
    }
    
    @Override
    public void setContent(Integer value) {
        binContent = value; 
    }

    @Override
    public Integer getContent() {
        return binContent; 
    }

    @Override
    public void add(Integer a) {
        binContent += a; 
    }

    @Override
    public void multiply(Integer a) {
        binContent *= a; 
    }

    @Override
    public void reset() {
        binContent = 0; 
    }
   
    @Override
    public Integer empty(){
        return new Integer(0);
    }
    
}


