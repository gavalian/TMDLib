/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.grid.matrix;

import java.util.HashMap;
import org.jlab.grid.utils.SparseIndexer;
import org.jlab.groot.data.DataVector;

/**
 *
 * @author gavalian
 */
public class SparseVectorGrid {
    
    SparseIndexer             indexer;
    HashMap<Long,DataVector>   binMap;
    
    public SparseVectorGrid(){
        
    }
    public SparseVectorGrid(int[] bins){
        this.indexer = new SparseIndexer(bins);
        this.binMap = new HashMap<Long,DataVector>();
    }
    
    public HashMap<Long,DataVector> getGrid(){ return binMap;}
    
    public void addBin(int[] index){
        Long key = indexer.getKey(index);
        binMap.put(key, new DataVector(indexer.getRank()));
    }
    
    public DataVector getBin(int[] index){
        Long key = indexer.getKey(index);
        if(binMap.containsKey(key)==true) return binMap.get(key);
        return null;
    }
    
    public SparseIndexer  getIndexer(){
        return indexer;
    }
        
}
