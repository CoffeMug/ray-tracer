package exceptions;

/**
 * Created by amin on 2017-04-16.
 */
public class BitmapReadErrorException extends RuntimeException {
    public BitmapReadErrorException(String msg) {
        super(msg);
    }
}
