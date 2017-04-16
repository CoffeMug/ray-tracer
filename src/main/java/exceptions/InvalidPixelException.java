package exceptions;

/**
 * Created by amin on 2017-03-11.
 */
public class InvalidPixelException extends RuntimeException{
    public InvalidPixelException(String msg) {
        super(msg);
    }
}