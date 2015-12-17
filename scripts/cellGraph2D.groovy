import org.foam.base.*;
import org.foam.test.*;
import org.root.histogram.*;
import org.root.pad.*;


GaussFunction2D gausFunc = new GaussFunction2D();
ExpFunction1D    expFunc = new ExpFunction1D();
MCFoam foam = new MCFoam();
foam.setMaxCells(300);
foam.setFunction(expFunc);

//GraphErrors  cellGraph = foam.getCellGraph(0,1);
//cellGraph.setMarkerSize(2);
//TGCanvas c1 = new TGCanvas("c1","",600,600,1,1);
//c1.cd(0);
//c1.draw(cellGraph);
