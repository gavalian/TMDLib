import org.foam.base.*;
import org.foam.test.*;
import org.root.histogram.*;
import org.root.pad.*;


GaussFunction2D gausFunc = new GaussFunction2D();        
MCFoam foam = new MCFoam();

foam.setFunction(gausFunc);

H2D  hrg = new H2D("HR",120,0.0,1.0,120,0.0,1.0);

for(int loop = 0; loop < 300000; loop++){
   double[] rand = foam.getRandom();
   hrg.fill(rand[0], rand[1]);
}

TGCanvas c1 = new TGCanvas("c1","",600,600,1,1);
c1.cd(0);
c1.draw(hrg);
