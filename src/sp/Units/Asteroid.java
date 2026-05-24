package sp.Units;

import java.util.ArrayList;
import java.util.Random;


import static sp.Panels.GamePanel.Asteroids;
import static sp.ResonantDebris.frame;

public class Asteroid extends AIUnit {

    public Asteroid(int startX, int startY, long id) {
        super(startX,startY,id,"Asteroid.png");

    }
    public Asteroid(){super();}



    public static void add(int x, int y, ArrayList list){
        if (list.isEmpty()){
            list.add(new Asteroid(x,y,0));
        }
        else{
            Asteroid LastAsteroid = (Asteroid) list.get(list.size() - 1);
            list.add(new Asteroid(x,y, LastAsteroid.id + 1));
        }

    }

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
            for (int asi = 0; asi < Asteroids.size(); asi++) {
                if (Asteroids.get(asi).id == this.id){
                    //System.out.println("Asteroid Deleted!!");
                    Asteroids.remove(asi);
                    break; //  Важно! Выходим из цикла после удаления элемента, чтобы избежать IndexOutOfBoundsException

                }
            }
        }
    }

}