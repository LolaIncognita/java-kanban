package rent.exceptions;


public class ManagersSaveException extends RuntimeException {
    private String message;
    public ManagersSaveException (String message) {
        this.message = message;
      }

      public ManagersSaveException (String message, Throwable cause) {
        super(message, cause);
      }
}