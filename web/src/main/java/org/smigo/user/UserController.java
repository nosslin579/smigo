package org.smigo.user;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Controller that handles user specific type of requests.
 *
 * @author Christian Nilsson
 */
@Controller
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserHandler userHandler;
    @Autowired
    private UserAdaptiveMessageSource messageSource;

    public UserController() {
        log.debug("Creating new UserController");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Locale.class, new LocaleEditor());
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @ResponseBody
    public List<ObjectError> changePassword(@RequestBody @Valid PasswordFormBean passwordFormBean, BindingResult result,
                                            @AuthenticationPrincipal AuthenticatedUser user, HttpServletResponse response) {
        if (result.hasErrors()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return result.getAllErrors();
        }
        userHandler.updatePassword(user, passwordFormBean.getNewPassword());
        return Collections.emptyList();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public List<ObjectError> register(@Valid RegisterFormBean user, BindingResult result, HttpServletResponse response) {
        log.info("Create Update user: " + user);
        if (result.hasErrors()) {
            log.warn("Create user failed. Username:" + user.getUsername() + " Errors:" + Joiner.on(", ").join(result.getAllErrors()));
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return result.getAllErrors();
        }
        userHandler.createUser(user);
        return Collections.emptyList();
    }

    @RequestMapping(value = "/rest/user", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ObjectError> updateUser(@RequestBody @Valid UserBean userBean, BindingResult result,
                                        @AuthenticationPrincipal AuthenticatedUser user,
                                        HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("Update user failed. Username:" + user.getUsername() + " Errors:" + Joiner.on(", ").join(result.getAllErrors()));
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return result.getAllErrors();
        }
        log.info("Updating user: " + userBean.toString());
        userHandler.updateUser(userBean, user);
        return Collections.emptyList();
    }

    @RequestMapping(value = "request-password-link", method = RequestMethod.POST)
    @ResponseBody
    public void requestPasswordLink(@RequestBody RequestPasswordLinkFormBean bean) {
        userHandler.sendResetPasswordEmail(bean.getEmail());
    }

    @RequestMapping(value = "/login-reset/{resetKey}", method = RequestMethod.GET)
    public String getResetForm(@PathVariable String resetKey, Model model) {
        model.addAttribute(new ResetKeyPasswordFormBean(resetKey));
        return "resetpasswordform.jsp";
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public String setPassword(@Valid ResetKeyPasswordFormBean resetFormBean, BindingResult result) {
        if (result.hasErrors()) {
            return "resetpasswordform.jsp";
        }
        boolean success = userHandler.setPassword(resetFormBean);
        if (!success) {
            result.addError(new ObjectError("resetPassword", "Update password failed"));
            return "resetpasswordform.jsp";
        }
        return "redirect:/#login";
    }


    @RequestMapping(value = "locales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public java.util.Map<String, String> getLocales() {
        return Language.getTransalationMap();
    }

    @RequestMapping(value = "rest/translation/{locale}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public java.util.Map<Object, Object> getTranslation(@PathVariable Locale locale) {
        return messageSource.getAllMessages(locale);
    }


}
