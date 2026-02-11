package sp.Units;

import java.util.ArrayList;

import static sp.Panels.GamePanel.scrap;
import static sp.ResonantDebris.frame;

/**
 * responds an unit that has id and method for correct addition to <code>ArrayList</code>
 * @since build 4
 * @see sp.Units.Unit
 *  @see ArrayList
 */
public class AddableUnit extends Unit {

    protected AddableUnit(int startX, int startY, long id, String imageFile) {
        super(startX, startY, id, imageFile);
    }
    protected AddableUnit(){
        super();
    }
    public static void add(int x, int y, ArrayList list) throws NullPointerException{
        if (list == null){
            throw new NullPointerException("ArrayList is null!");
        }
        if (list.isEmpty()){
            list.add(new AddableUnit(x,y,0,""));
        }
        else{
            AddableUnit LastAsteroid = (AddableUnit) list.get(list.size() - 1);
            list.add(new AddableUnit(x,y, LastAsteroid.id + 1,""));
        }

    }
}