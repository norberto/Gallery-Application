package edu.norbertzardin.vm;

import edu.norbertzardin.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;


public class RegisterVM {

    private static final String SUCCESS = "User has been successfully registered.";
    private final String REGISTRATION_FAILED = "Could not create user.";
    @WireVariable
    private UserService userService;
    private String username;
    private final String USER_ALREADY_EXISTS = "User <b>" + username + "<b> already exists.";
    private String password;
    private String password_r;
    private String captcha;
    private String generated_captcha;
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
        if (result == 200) {
            message = SUCCESS;
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
