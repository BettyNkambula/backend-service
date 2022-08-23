package za.co.vending.machine.backendservice.exceptions;

public class ErrorHandler extends Exception {
    private static final long serialVersionUID = -272160890920002615L;

    public ErrorHandler(String errorMsg) {
        super(errorMsg);
    }
    public ErrorHandler(String errorMsg, Throwable err) {
        super(errorMsg, err);
    }
}
