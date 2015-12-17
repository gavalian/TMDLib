/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmd.base;


import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gavalian
 */
public class UserParamSet {
    Map<String,UserParam>  userParams = new LinkedHashMap<String,UserParam>();
    JPanel  panel = null;
    
    public UserParamSet(){
        
    }
    
    public UserParamSet addParam(String name, double value){
        this.userParams.put(name,new UserParam(name,value));
        return this;
    }
    
    public UserParamSet addParam(String name, double value, double min, double max){     
        this.userParams.put(name,new UserParam(name,value,min,max));
        return this;
    }
    public UserParam getParam(String name){
        return this.userParams.get(name);
    }
    
    public int getCount(){ return this.userParams.size();}
    
    public List<UserParam>  getList(){
        List<UserParam>  plist = new ArrayList<UserParam>();
        for(Map.Entry<String,UserParam> item : this.userParams.entrySet()){
            plist.add(item.getValue());
        }
        return plist;
    }
    
    public void setParams(double[] par){
        int counter = 0;
        for(Map.Entry<String,UserParam> item : this.userParams.entrySet()){
            item.getValue().setValue(par[counter]);
            counter++;
        }
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,UserParam> item : this.userParams.entrySet()){
            str.append(item.getValue()); str.append("\n");
        }
        return str.toString();
    }
    public JPanel createPanel(){
        this.panel = new JPanel();
        this.panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
        for(Map.Entry<String,UserParam>  item : this.userParams.entrySet()){
            this.panel.add(item.getValue().getParamPanel());
        }
        return this.panel;
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        UserParamSet set = new UserParamSet();
        set.addParam("x", 0.1).addParam("y", 0.2).addParam("z", 0.6);
        frame.add(set.createPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
