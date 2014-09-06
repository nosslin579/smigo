package org.smigo.user;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller that handles user specific type of requests.
 *
 * @author Christian Nilsson
 */
@Controller
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserSession userSession;
    @Autowired
    private UserHandler userHandler;
    @Autowired
    private User user;

    public UserController() {
        log.debug("Creating new UserController");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Locale.class, new LocaleEditor());
    }

    @ModelAttribute("availableLocales")
    public Map<String, String> addAvailableLocales() {
        return Translation.getTransalationMap();
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
    public String getChangePasswordForm(ModelMap modelMap) {
        modelMap.addAttribute(new PasswordFormBean());
        modelMap.addAttribute("requireCurrentPassword", true);
        return "passwordform.jsp";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String handleChangePasswordForm(@Valid PasswordFormBean passwordFormBean, BindingResult result) {
        if (result.hasErrors())
            return "passwordform.jsp";
        userHandler.updatePassword(user.getUsername(), passwordFormBean.getNewPassword());
        return "redirect:/user/";
    }

    @RequestMapping(value = {"/cuuser", "/signup", "/edituser"}, method = RequestMethod.GET)
    public String getUserForm(ModelMap modelMap) {
        userSession.registerSignupStart();
        modelMap.addAttribute(new UserBean());
        return "userform.jsp";
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

    @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
    public String getUser(@PathVariable Integer userid, Model model, Principal principal) {
        UserBean u = new UserBean();
        u.setEmail("");
        u.setUsername("");
        model.addAttribute("showall", false);
        model.addAttribute("user", u);
        return "userinfo.jsp";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getUser(Model model, @AuthenticationPrincipal UserBean principal) {
        model.addAttribute("showall", true);
        model.addAttribute("user", principal);
        return "userinfo.jsp";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "loginform.jsp";
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.GET)
    public String getReset(Model model) {
        model.addAttribute(new ResetFormBean());
        return "resetpasswordform.jsp";
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public String handleReset(@Valid ResetFormBean resetFormBean, BindingResult result) {
        if (result.hasErrors()) {
            return "resetpasswordform.jsp";
        }
        userHandler.sendResetPasswordEmail(resetFormBean.getEmail());
        return "resetpasswordform.jsp";
    }

    @RequestMapping(value = "/login-reset/{loginKey}", method = RequestMethod.GET)
    public String handleReset(@PathVariable String loginKey, Model model) {
        userHandler.authenticateUser(loginKey);
        model.addAttribute(new PasswordFormBean());
        model.addAttribute("requireCurrentPassword", false);
        return "passwordform.jsp";
    }


}
