package NativeLibraries.Expertions;


public class UnvaliabeClassExpertion extends Exception {
    Class<?> Cause = null;
    public UnvaliabeClassExpertion(String message, Class<?> cause){
        super(message);
        this.Cause = cause;
    }
    UnvaliabeClassExpertion(String message){
        this(message, null);
    }
    UnvaliabeClassExpertion(){
        this(null, null);
    }
    UnvaliabeClassExpertion(Class<?> cause){
       this(null,cause);
    }
}