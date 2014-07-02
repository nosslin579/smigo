package org.smigo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.CurrentUser;
import org.smigo.entities.User;
import org.smigo.formbean.PasswordFormBean;
import org.smigo.handler.UserHandler;
import org.smigo.i18n.Translation;
import org.smigo.persitance.DatabaseResource;
import org.smigo.persitance.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
        modelMap.addAttribute("passwordFormBean", new PasswordFormBean());
        return "passwordform.jsp";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String handleChangePasswordForm(@Valid PasswordFormBean passwordFormBean, BindingResult result) {
        if (!databaseresource.validatePassword(currentUser.getUser(), passwordFormBean.getOldPassword()))
            result.addError(new FieldError("passwordFormBean", "oldPassword", "invalid"));
        if (result.hasErrors())
            return "passwordform.jsp";
        databaseresource.updatePassword(currentUser.getId(), passwordFormBean.getNewPassword());
        return "redirect:/user/";
    }

    @RequestMapping(value = {"/cuuser", "/signup", "/edituser"}, method = RequestMethod.GET)
    public String getUserForm(ModelMap modelMap) {
        userSession.registerSignupStart();
        modelMap.addAttribute("user", new User());
        return "userform.jsp";
    }

    @RequestMapping(value = "/cuuser", method = RequestMethod.POST)
    public String handleUserForm(@Valid User user, BindingResult result, HttpSession session) {
        log.info("Create Update user: " + user);
        if (result.hasErrors()) {
            log.warn("Create user failed. Username:" + user.getUsername());
            return "userform.jsp";
        }
        // create user
        if (user.getId() == 0) {
            long decideTime = System.currentTimeMillis() - session.getCreationTime();
            userHandler.createUser(user,decideTime);
            return "redirect:login";
        } else {// update user
            userHandler.updateUser(user);
            return "redirect:user/";
        }
    }

    /**
     * Returns user details. Almost like facebooks my wall.
     */
    @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
    public String getUser(@PathVariable Integer userid, Model model, Principal principal) {
        User u = null;
        u = databaseresource.getUser(userid);
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

    /**
     * Returns login form.
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "loginform.jsp";
    }

    /**
     * Authenticate automatically. Not implemented yet
     */
    // private void authenticateUserAndSetSession(User user, HttpServletRequest
    // request)
    // {
    // UsernamePasswordAuthenticationToken token = new
    // UsernamePasswordAuthenticationToken(
    // user.getUsername(), user.getPassword());
    //
    // // generate session if one doesn't exist
    // request.getSession();
    //
    // token.setDetails(new WebAuthenticationDetails(request));
    // Authentication authenticatedUser =
    // authenticationManager.authenticate(token);
    // SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    // }
    //
    // protected void autoLogin(HttpServletRequest request, User user){
    // UsernamePasswordAuthenticationToken authentication = new
    // UsernamePasswordAuthenticationToken(user.getUsername(),
    // user.getPassword(), user.getAuthorities());
    // SecurityContextHolder.getContext().setAuthentication(authentication);
    // }

}
