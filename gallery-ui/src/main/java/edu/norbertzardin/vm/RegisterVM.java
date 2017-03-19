package edu.norbertzardin.vm;

import edu.norbertzardin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;


public class RegisterVM {


    @WireVariable
    private UserService userService;

    private String username;
    private String password;
    private String password_r;

    private String captcha;
    private String generated_captcha;

    private final String USER_ALREADY_EXISTS = "User <b>" + username + "<b> already exists.";
    private final String REGISTRATION_FAILED = "Could not create user.";
    private static final String SUCCESS = "User has been successfully registered.";

    private String error;
    private String message;

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

    }



    @Command
    @NotifyChange({"error", "message"})
    public void register() {
        if (!password.equals(password_r)) {
            error = "Password don't match.";
            return;
        }
        Integer result = userService.register(username, password);
        switch (result) {
            case 100:
                error = USER_ALREADY_EXISTS;
                break;
            case 101:
                error = REGISTRATION_FAILED;
                break;
            case 200:
                message = SUCCESS;
            default:
                break;

        }


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword_r(String password_r) {
        this.password_r = password_r;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getGenerated_captcha() {
        return generated_captcha;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
