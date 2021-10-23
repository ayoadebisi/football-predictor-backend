package service.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"cause", "stackTrace", "message", "suppressed", "localizedMessage"})
public class ServiceException extends Exception {

    private String errorMessage;

    public ServiceException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
