package service.exception;

public class InvalidServiceRequestException extends ServiceException {
    public InvalidServiceRequestException(String errorMessage) {
        super(errorMessage);
    }
}
