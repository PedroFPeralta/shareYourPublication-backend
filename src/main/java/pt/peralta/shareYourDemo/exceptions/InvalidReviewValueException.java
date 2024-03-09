package pt.peralta.shareYourDemo.exceptions;

public class InvalidReviewValueException extends RuntimeException{
    public InvalidReviewValueException() {
    }

    public InvalidReviewValueException(String message) {
        super(message);
    }

    public InvalidReviewValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReviewValueException(Throwable cause) {
        super(cause);
    }
}
