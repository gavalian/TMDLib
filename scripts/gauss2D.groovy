import org.foam.base.*;
import org.foam.test.*;
import org.root.histogram.*;
import org.root.pad.*;


GaussFunction2D   gausFunc = new GaussFunction2D();
CosineFunction2D  cosFunc  = new CosineFunction2D();
MCFoam foam = new MCFoam();
foam.setMaxCells(25000);

//foam.setFunction(gausFunc);
foam.setFunction(cosFunc);

H2D  hrg  = new H2D("HR" ,80,0.0,1.0,80,0.0,1.0);
H1D  hrgx = new H1D("HRx",120,0.0,1.0);
H1D  hrgy = new H1D("HRy",120,0.0,1.0);

for(int loop = 0; loop < 300000; loop++){
   double[] rand = foam.getRandom();
   hrg.fill( rand[0], rand[1]);
   hrgx.fill(rand[0]);
   hrgy.fill(rand[1]);
}

TGCanvas c1 = new TGCanvas("c1","",600,900,1,3);
c1.cd(0);
//c1.setLogZ(true);
c1.draw(hrg);

c1.cd(1);
c1.draw(hrgx);

c1.cd(2);
c1.draw(hrgy);
