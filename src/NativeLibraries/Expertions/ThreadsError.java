package NativeLibraries.Expertions;
import java.lang.Error;

/**
 * The <code>ThreadsError</code> thrown when the threads stop being deleted and the game starts using too many threads.
 * @see java.lang.Error
 */
public class ThreadsError extends Error{
    public ThreadsError(String message,Throwable cause){
        super(message, cause);
    }
    public ThreadsError(String message){
        super(message);
    }
}