package pt.peralta.shareYourDemo.exceptions;

public class NoMorePublicationException extends Exception{
    public NoMorePublicationException() {
    }

    public NoMorePublicationException(String message) {
        super(message);
    }

    public NoMorePublicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMorePublicationException(Throwable cause) {
        super(cause);
    }
}
