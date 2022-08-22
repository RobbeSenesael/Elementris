package utils;

public class ElementrisException extends RuntimeException {
    public ElementrisException(String msg, Exception ex) {
        super(msg, ex);
    }

    public ElementrisException(String msg) {
        super(msg, new RuntimeException());
    }
}
