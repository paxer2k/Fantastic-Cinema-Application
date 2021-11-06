package exception;

public class FieldsNotFilledException extends RuntimeException {
    public FieldsNotFilledException() {
        super("Please fill out the fields!");
    }
}
