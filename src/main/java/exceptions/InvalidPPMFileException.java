package exceptions;

/**
 * Created by amin on 2017-03-11.
 */
public class InvalidPPMFileException extends RuntimeException{
    public InvalidPPMFileException(String msg) {
        super(msg);
    }
}