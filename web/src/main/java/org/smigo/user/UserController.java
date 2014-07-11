package org.smigo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.persitance.DatabaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Locale;
import java.util.Map;

/**
 * Controller that handles user specific type of requests.
 *
 * @author Christian Nilsson
 */
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private DatabaseResource databaseresource;
    @Autowired
    private UserSession userSession;
    @Autowired
    private UserHandler userHandler;
    @Autowired
    private CurrentUser currentUser;

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
        userHandler.updatePassword(currentUser.getUser().getUsername(), passwordFormBean.getNewPassword());
        return "redirect:/user/";
    }

    @RequestMapping(value = {"/cuuser", "/signup", "/edituser"}, method = RequestMethod.GET)
    public String getUserForm(ModelMap modelMap) {
        userSession.registerSignupStart();
        modelMap.addAttribute("user", new User());
        return "userform.jsp";
    }

    @RequestMapping(value = "/cuuser", method = RequestMethod.POST)
    public String handleUserForm(@Valid User user, BindingResult result) {
        log.info("Create Update user: " + user);
        if (result.hasErrors()) {
            log.warn("Create user failed. Username:" + user.getUsername());
            return "userform.jsp";
        }
        // create user
        if (user.getId() == 0) {
            final String password = user.getPassword();
            userHandler.createUser(user);
            userHandler.authenticateUser(user.getUsername(), password);
            return "redirect:/garden";
        } else {// update user
            userHandler.updateUser(user);
            return "redirect:user/";
        }
    }

    @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
    public String getUser(@PathVariable Integer userid, Model model, Principal principal) {
        User u = new User();
        u.setEmail("");
        u.setUsername("");
        model.addAttribute("showall", false);
        model.addAttribute("user", u);
        return "userinfo.jsp";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getUser(Model model, Principal principal) {
        User u = currentUser.getUser();
        model.addAttribute("showall", true);
        model.addAttribute("user", u);
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
