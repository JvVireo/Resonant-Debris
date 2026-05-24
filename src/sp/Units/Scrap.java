package sp.Units;


import java.util.ArrayList;
import java.util.Random;

import static sp.Panels.GamePanel.scrap;
import static sp.ResonantDebris.frame;

public class Scrap extends AIUnit {


    public Scrap(int startX, int startY, long id) {
       super(startX,startY,id,"Scrap.png");
    }

    public Scrap() {}

    public Scrap(int startX, int startY, long id, String ModdedImage){ super(startX,startY,id,ModdedImage);}
    public static void add(int x, int y, ArrayList list){
        if (list.isEmpty()){
            list.add(new Scrap(x,y,0));
        }
        else{
            Scrap LastAsteroid = (Scrap) list.get(list.size() - 1);
            list.add(new Scrap(x,y, LastAsteroid.id + 1));
        }

    }
    @Override
    public void AI() {
        Random r = new Random();
        int IC = r.nextInt(2);
        switch (IC) {
            case 0:
                this.moveRight();
                break;
            case 1:
                this.moveLeft();
                break;
            default:
        }
        this.moveDown();
        if (x >= frame.getWidth() + 10 || y >= frame.getHeight() + 10){
            for (int asi = 0; asi < scrap.size(); asi++) {
                if (scrap.get(asi).id == this.id){
                    scrap.remove(asi);
                    break;

                }
            }
        }
    }

    public long getId() {
        return id;
    }

}