package sp.Units;

import NativeLibraries.SoundLibrary;
import sp.Mods.ClassMap;

import java.util.ArrayList;

import static sp.Panels.GamePanel.player;

/** Provides an Enemy unit that shoots in the player
 * @since build 4
 * @see sp.Units.Player
 */
public class Enemy extends AIUnit {

    private boolean avoidance = false;
    private int avoidanceCount = 0;
    public int id = 0;

    public Enemy(int startX, int startY, int id) {
            super(startX,startY,id,"enemy.png");



    }
    public Enemy() {super();}


    public void AI() {
        if (this.x == player.x && !avoidance) {
            shoot();
            avoidance = true;
        } else {
            if (avoidance) {
                moveLeft();
                moveDown();
                avoidanceCount++;
                if (avoidanceCount != 4) {
                    return;
                } else {
                    avoidanceCount = 0;
                    avoidance = false;
                }
            }
            if (this.x > player.x) {
                moveLeft();
            } else if (this.x < player.x) {
                moveRight();
            }
            moveDown();
        }
    }

    public static void add(int x, int y, ArrayList list) throws NullPointerException{
        if (list == null){
            throw new NullPointerException("ArrayList is null!");
        }
        if (list.isEmpty()){
            list.add(new Enemy(x,y,0));
        }
        else{
            AddableUnit LastAsteroid = (AddableUnit) list.get(list.size() - 1);
            list.add(new Enemy(x,y, (int) (LastAsteroid.id + 1)));
        }

    }
    private void shoot() {
        bullet.Shoot(this.x + 32,this.y, bullet.bulletConstants.DIRECTION_DOWN, ClassMap.GamePanel.bullets);
        SoundLibrary.playAudio("assets/SFX/shoot.wav");
    }
}