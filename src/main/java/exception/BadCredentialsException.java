package exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Password did not match!");
    }
}
