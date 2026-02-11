package NativeLibraries.MathFunctions;

/**
 * The <code>MFunctions</code> has many math functions which you can use
 * @see java.lang.Math
 * @see java.lang.StrictMath
 */
public interface MFunctions {

    /**
     * The <code>ConsExponentFunction</code> function uncreases input constant on exponent function.
     * @param Const function's multiple constant.
     * @param rate exponent rate parameter.
     *
     *
     * **/
    static int ConsExponentFunction(final int Const, int rate){
           int Expont = rate * 2;
           return Const * Expont;
       }

    /**
     * The <code>ConsExponentDivideFunction</code> is the same as <code>ConsExponentFunction</code> but <code>Const</code> parameter is divide constant now
     */

    static int ConsExponentDivideFunction(final int Const, int rate){
        int Expont = rate * 2;
        if (Expont == 2){
            return Const;
        }else {
            return Const / Expont;
        }
    }


}