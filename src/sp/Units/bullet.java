package sp.Units;

import NativeLibraries.Expertions.UnvaliabeClassExpertion;

import java.awt.*;
import java.util.ArrayList;

public class bullet {
    private  int direction = bulletConstants.DIRECTION_NULL;
    public Unit shooter;
    public int x;
    public int y;
    public int id;
    private  Rectangle hitbox;

    public Rectangle getHitbox() {
        return hitbox;
    }

    public enum bulletConstants{;

        /**Direction constants<br>
         * Constant for "up" bullet direction
         */
        public static final int DIRECTION_UP = 0;
        /** Constant for "left" bullet direction
         */

        public static final int DIRECTION_LEFT = 1;
        /** Constant for "right" bullet direction
         */
        public static final int DIRECTION_RIGHT = 2;
        /** Constant for "down" bullet direction
         */
        public static final int DIRECTION_DOWN = 3;
        /** Null direction <br>
         * Used to initialise "direction" variable
         */
        public static final int DIRECTION_NULL = -1;
        /**  bullet color constant
         */
        public static final Color BULLET_COLOR = new Color(255, 183, 0);
        /**bullet width constant*/
        public static final int BULLET_WIDTH = 1;
        /**bullet height constant*/
        public static final int BULLET_HEIGHT = 96;
    }

    public bullet(int x, int y, int direction, int id, Unit shooter){
        this.shooter = shooter;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;
        //int Hix = x + 2;
        hitbox = new Rectangle(x, y, bulletConstants.BULLET_WIDTH, bulletConstants.BULLET_HEIGHT);
    }
    public bullet(){}
    public void draw(Graphics g){
        g.setColor(bulletConstants.BULLET_COLOR);
        g.drawRect(x,y,bulletConstants.BULLET_WIDTH,bulletConstants.BULLET_HEIGHT);

    }
    public void BAI(){
        switch (direction){
            case (bulletConstants.DIRECTION_UP):
                moveUp();
                break;
            case (bulletConstants.DIRECTION_LEFT):
                moveLeft();
                break;
            case (bulletConstants.DIRECTION_RIGHT):
                moveRight();
                break;
            case (bulletConstants.DIRECTION_DOWN):
                moveDown();
                break;
        }
    }
    public static void Shoot(int x, int y, int direction, Unit shooter, ArrayList list){

        if (list.isEmpty()) {
            list.add(new bullet(x, y, direction,0, shooter));
        } else {
            bullet LastBullet = (bullet) list.get(list.size() - 1);
            list.add(new bullet(x, y, direction, LastBullet.id + 1, shooter));
        }

    }
    public static void Shoot(int x, int y, int direction, ArrayList list)  {
        Shoot(x,y,direction,null,list);
    }
    public void moveLeft() {
        //System.out.println("Method Called!");
        x = x - 50;

        updateHitbox(); //  Обновляем позицию хитбокса
    }

    public void moveRight() {
        //System.out.println("Method Called!");
        x = x + 50;

        updateHitbox(); //  Обновляем позицию хитбокса
    }

    public void moveUp() {
        //System.out.println("Method Called!");
        y = y - 50;

        updateHitbox(); //  Обновляем позицию хитбокса
    }

    public void moveDown() {
        //System.out.println("Method Called!");
        y = y + 50;

        updateHitbox(); //  Обновляем позицию хитбокса
    }

    //  Метод для обновления позиции хитбокса
    private void updateHitbox() {
        hitbox.x = x;
        hitbox.y = y;
    }
}
