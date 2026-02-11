package sp.Units;

/**
 * responds an unit that has an AI.
 * Must be addable
 *
 * @see sp.Units.AddableUnit
 */
public abstract class AIUnit extends AddableUnit{

    protected AIUnit(int startX, int startY, long id, String image) {
        super(startX,startY,id,image);
    }
    protected AIUnit(){
        super();
    }

    /**
     * AI for Unit
     */
    public abstract void AI();
}