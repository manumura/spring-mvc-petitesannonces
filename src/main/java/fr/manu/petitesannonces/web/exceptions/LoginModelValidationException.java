package fr.manu.petitesannonces.web.exceptions;

import java.util.List;

import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;

import fr.manu.petitesannonces.web.model.LoginModel;

/**
 * @author EmmanuelM
 *
 */
public class LoginModelValidationException extends AuthenticationException {

    private static final long serialVersionUID = 5427816579407213002L;

    private final transient BindingResult bindingResult;

    private final LoginModel loginModel;

    private final List<String> errorMessages;
    
    public LoginModelValidationException(final String msg, final BindingResult bindingResult,
            final LoginModel loginModel, final List<String> errorMessages) {
        super(msg);
        this.bindingResult = bindingResult;
        this.loginModel = loginModel;
        this.errorMessages = errorMessages;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

}
