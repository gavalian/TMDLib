import org.foam.base.*;
import org.foam.test.*;
import org.root.histogram.*;
import org.root.pad.*;


GaussFunction2D   gausFunc = new GaussFunction2D();
CosineFunction2D  cosFunc  = new CosineFunction2D();
MCFoam foam = new MCFoam();
foam.setMaxCells(2500);

foam.setFunction(gausFunc);
//foam.setFunction(cosFunc);


List<DataBox>  boxes = foam.getCellBoxes(0,1);

System.out.println("Number of BOXES = " + boxes.size());
TGCanvas c1 = new TGCanvas("c1","",800,800,1,1);

c1.cd(0);
c1.draw(boxes.get(0));
for(DataBox box : boxes){
  c1.draw(box,"same");
}


