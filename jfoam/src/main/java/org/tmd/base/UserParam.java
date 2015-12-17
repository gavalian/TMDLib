/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmd.base;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author gavalian
 */
public class UserParam implements  ChangeListener {
    double pMin = Double.NEGATIVE_INFINITY;
    double pMax = Double.POSITIVE_INFINITY;
    double pValue = 0.0;
    boolean isFixed = false;
    boolean isLimited = false;
    String pName = "unknown";
    JPanel paramPanel = null;
    
    public UserParam(){
        
    }
    
    public UserParam(String name){
        this.pName = name;
    }
    
     public UserParam(String name, double value){
        this.pName = name;
        this.pValue = value;
    }
     
     public UserParam(String name, double value, double min, double max){
        this.pName = name;       
        this.setLimits(min, max);
        this.setValue(value);
        //this.pValue = pMin + 0.5*(pMax-pMin);
    }
    public UserParam(String name, double min, double max){
        this.pName = name;
        this.setLimits(min, max);
        this.pValue = pMin + 0.5*(pMax-pMin);
    }
    
    public String getName(){
        return this.pName;
    }
    
    public double getMin(){ return this.pMin;}
    public double getMax(){ return this.pMax;}
    public double getValue() { return this.pValue;}
    
    public final UserParam setLimits(double min, double max){
        this.pMin = min;
        this.pMax = max;
        this.isLimited = true;
        return this;
    }    
    
    public final void setValue(double value){
        if(this.isLimited==true){
            if(value>=this.pMin&&value<=this.pMax){
                this.pValue = value;
            } else {
                this.pValue = this.pMin;
            }
        } else {
            this.pValue = value;
        }
    }
    
    
    public JPanel getParamPanel(){
        this.paramPanel = new JPanel();
        this.paramPanel.setLayout(new FlowLayout());
        this.paramPanel.add(new JLabel(String.format("%-12s", this.pName)));
        SpinnerModel model1 = new SpinnerNumberModel(0.0, 0.0, 999.0,
                0.1);
        JSpinner spinner = new JSpinner(model1);
        spinner.setValue(this.pValue);
        spinner.setPreferredSize(new Dimension(100,25));
        spinner.addChangeListener(this);
        this.paramPanel.add(spinner);
        return paramPanel;
    }
    
    
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("%-12s : %12.6f %12.6f %12.6f",this.getName(),
                this.getValue(),this.getMin(),this.getMax()));
        return str.toString();
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner) e.getSource();
        Object   value = spinner.getValue();
        System.out.println(" VALUE " + this.getName() + " CHANGED TO " + value);
        this.pValue = (( Number) value).doubleValue();
    }
}
