/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.grid.utils;

import java.util.HashMap;
import java.lang.Exception;

/**
 *
 * @author dmriser
 */
public class SparseGrid<T extends IGridContent> {    
    SparseIndexer indexer;
    HashMap<Long,T> binMap; 
    
    public SparseGrid(int[] binsPerAxis){
        this.indexer = new SparseIndexer(binsPerAxis);
        this.binMap = new HashMap<Long,T>();
    }

    public void setBinContent(int[] bin, T value){
        long key = indexer.getKeyFast(bin);
        binMap.put(key, value);
    }
    
    public void setBinContentByKey(Long key, T value){
        binMap.put(key,value);
    }
    
    // Needs to use getOrDefault(key, defaultValue) to 
    // return empty bins properly 
    public T getBinContent(int[] bin){
        long key = indexer.getKeyFast(bin);
        return binMap.get(key); 
    }
    
    public T getBinContentByKey(Long key){
        return binMap.get(key);
    }
    
    public void removeAxis(int axisToBeRemoved) throws Exception {                
        if (indexer.rank == 1){
            throw new Exception("SparseGrid cannot be reduced from rank=1");
        } else if (axisToBeRemoved > indexer.rank-1){
            throw new Exception("Trying to remove axis number larger than rank of grid.");
        }
        
        int[] oldBinsPerAxis = indexer.getBinsPerAxis();
        int[] newBinsPerAxis = removeElement(oldBinsPerAxis, axisToBeRemoved);
        
        SparseIndexer newIndexer = new SparseIndexer(newBinsPerAxis); 
       
        HashMap<Long,T> newBinMap = generateReducedBinMap(axisToBeRemoved, newIndexer);
        setSparseIndexer(newIndexer);
        setBinMap(newBinMap);
    }
    
//    public void projectOutAxis(int axisToBeRemoved){
//        int[] bins = new int[getRank()];
//        int[] targetBin = new int[getRank()];
//        
//        for(HashMap.Entry<Long,T> entry : binMap.entrySet()){            
//            Long key = entry.getKey();
//            T value = entry.getValue();
//            
//            indexer.getIndex(key, bins);
//            if(bins[axisToBeRemoved] != 0){
//                bins[axisToBeRemoved] = 0; 
//                Long targetKey = indexer.getKey(bins);
//                T binContent = getBinContentByKey(targetKey);
//                binContent.add(value.getContent());
//                setBinContentByKey(targetKey,binContent);
//            }
//        }
//      
//        try{
//        removeAxis(axisToBeRemoved);  
//        } catch(Exception e) {
//            System.out.println("Error in projection, can't remove axis.");
//        }
//    }
//    
    private HashMap<Long,T> generateReducedBinMap(int axisToBeRemoved, SparseIndexer newIndexer) {
        int[] bin = new int[indexer.rank];
        int[] newBin = new int[indexer.rank-1];
        
        HashMap<Long,T> newBinMap = new HashMap<Long,T>();
        
        for (HashMap.Entry<Long,T> entry : binMap.entrySet()){
            indexer.getIndex(entry.getKey(),bin);
            newBin = removeElement(bin,axisToBeRemoved);
            Long newKey = newIndexer.getKeyFast(newBin);
            newBinMap.put(newKey,entry.getValue());
        }
        return newBinMap; 
    }

    private int[] removeElement(int[] oldArray, int elementToBeRemoved) {
        int[] newArray = new int[oldArray.length-1]; 
        
        int index = 0;
        for (int i=0; i<oldArray.length; i++){
            if(i == elementToBeRemoved){
                i += 1;
            }
            newArray[index] = oldArray[i];
            index++;
        }
        return newArray;
    }
    
    private int[] appendElement(int[] oldArray, int valueToAppend){
        int[] newArray = new int[oldArray.length+1];
        for (int i=0; i<oldArray.length; i++){
            newArray[i] = oldArray[i]; 
        }
        newArray[newArray.length-1] = valueToAppend;
        return newArray; 
    }
    
    public int getNumberOfFilledBins(){
        return binMap.size(); 
    }
    
    public int getRank(){
        return indexer.getRank();
    }
    
    private void setSparseIndexer(SparseIndexer newIndexer){
        this.indexer = newIndexer;
    }
    
    private void setBinMap(HashMap<Long,T> newBinMap){
        this.binMap = newBinMap; 
    }
    
    public void clearBinMap(){
        this.binMap.clear();
    }
}
