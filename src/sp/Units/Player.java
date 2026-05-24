package sp.Units;

import NativeLibraries.SoundLibrary;

/** provides a player unit
 * @apiNote in future builds id of this unit is player id that plays that unit*/
public class Player extends Unit{

    public Player(int startX, int startY,int id) {
        super(startX,startY,id,"player.png");
    }
    public Player(){}

    @Override
    public void setIsmodded() {
        ismodded = false;
    }

    /** shoot function
     * @since build 4
     * @see bullet
     */
    public void shoot(){
        bullet.Shoot(x + 32,y - 70, bullet.bulletConstants.DIRECTION_UP, this, sp.Mods.ClassMap.GamePanel.NSbullets);
        SoundLibrary.playAudio("assets/SFX/shoot.wav");
    }


}