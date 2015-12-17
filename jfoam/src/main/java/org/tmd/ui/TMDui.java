/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmd.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.root.histogram.GraphErrors;
import org.root.histogram.H1D;
import org.root.pad.EmbeddedCanvas;
import org.root.pad.TGCanvas;
import org.tmd.base.IPhysicsProcess;
import org.tmd.base.PhaseSpace;
import org.tmd.base.TFoam;
import org.tmd.base.UserParamSet;
import org.tmd.fitter.PhysicsProcessFitter;


/**
 *
 * @author gavalian
 */
public class TMDui extends JFrame implements ActionListener {
    EmbeddedCanvas  canvas = new EmbeddedCanvas();
    IPhysicsProcess physProcess = null;
    UserParamSet    parameterSet = null;
    
    public TMDui(IPhysicsProcess proc){
        super();
        this.setLayout(new BorderLayout());
        this.add(this.canvas,BorderLayout.CENTER);
        JPanel  panel = new JPanel();
        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(this);
        
        JButton generateBtn = new JButton("Generate");
        generateBtn.addActionListener(this);
        
        panel.add(updateBtn);
        panel.add(generateBtn);
        this.add(panel,BorderLayout.PAGE_START);
        this.parameterSet = proc.getParamSet();
        JPanel  constPanel = this.parameterSet.createPanel();
        this.add(constPanel,BorderLayout.PAGE_END);
        this.pack();
        this.physProcess = proc;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Update")==0){
            GraphErrors  graph = new GraphErrors();
            graph.setTitle("#sigma = x^a (1-x)^b");
            graph.setYTitle("#sigma");
            PhaseSpace   space = this.physProcess.getPhaseSpace();
            for(double x = 0.01; x <1.0; x+= 0.005){
                space.getDimension("x").setValue(x);
                double w = this.physProcess.getWeight(space, parameterSet);
                graph.add(x, w);
                graph.setMarkerSize(2);
            }
            this.canvas.cd(0);
            this.canvas.draw(graph);
        }
        
        
        if(e.getActionCommand().compareTo("Generate")==0){
            System.out.println(" Generating ---> ");
            TFoam foam = new TFoam();
            H1D h1 = foam.generate(physProcess, parameterSet);
            
            UserParamSet set = PhysicsProcessFitter.modelFit(physProcess, h1);
            TGCanvas c1 = new TGCanvas("c1","",600,300,1,1);
            h1.setTitle(String.format("a = %.3f b = %.3f", set.getParam("a").getValue(),
                    set.getParam("b").getValue()));
            h1.setLineWidth(2);
            h1.setFillColor(34);
            c1.cd(0);
            c1.draw(h1);
            
        }
    }
    
    public static void main(String[] args){
        //TMDui ui = new TMDui(new InclusiveElectroProduction());
        //ui.setVisible(true);
    }

}
