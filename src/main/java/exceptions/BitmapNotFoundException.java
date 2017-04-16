package exceptions;

/**
 * Created by amin on 2017-04-16.
 */
public class BitmapNotFoundException extends RuntimeException{
    public BitmapNotFoundException(String msg) {
        super(msg);
    }
}
