/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.grid.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jlab.groot.data.DataVector;
import org.jlab.hipo.data.HipoEvent;
import org.jlab.hipo.data.HipoNode;
import org.jlab.hipo.data.HipoNodeType;
import org.jlab.hipo.io.HipoWriter;

/**
 *
 * @author gavalian
 */
public class GridSerializer {
    
    public static Integer NumberOfBinsPerEvent = 100;
    
    public static void writeGrid(SparseVectorGrid grid, String filename){
        
        HipoWriter  writer = new HipoWriter();
        
        int[] index = grid.getIndexer().getBinsPerAxis();
        
        HipoNode  node = new HipoNode(200,1,HipoNodeType.INT,index.length);
        for(int i = 0; i < index.length; i++) node.setInt(i, index[i]);
        
        List<HipoNode>  headerNodes = new ArrayList<HipoNode>();
        
        HipoEvent event = new HipoEvent();
        event.addNodes(headerNodes);
        
        writer.writeEvent(event.getDataBuffer());
        
        int  gridSize = grid.getGrid().size();
        
        int counter = 0;
        
        HipoNode nodeINDEX  = new HipoNode(200,2,HipoNodeType.LONG,100);
        HipoNode nodeVECTOR = new HipoNode(200,3,HipoNodeType.LONG,100);
        
        for(Map.Entry<Long,DataVector> entry : grid.getGrid().entrySet()){
            //nodeINDEX.getL
        }
        
        writer.close();
    }
}
