package org.smigo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.species.SpeciesHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
    private UserHandler userHandler;
    @Autowired
    private UserAdaptiveMessageSource messageSource;
    @Autowired
    private SpeciesHandler speciesHandler;

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

    @RequestMapping(value = "/rest/user", method = RequestMethod.GET)
    @ResponseBody
    public UserBean getUser(@AuthenticationPrincipal AuthenticatedUser user) {
        return userHandler.getUser(user);
    }


    @RequestMapping(value = "/rest/user/{username}", method = RequestMethod.GET)
    @ResponseBody
    public PublicInfoUserBean getUser(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable String username) {
        return userHandler.getUserPublicInfo(username);
    }

    @RequestMapping(value = "/rest/user", method = RequestMethod.POST)
    @ResponseBody
    public List<ObjectError> addUser(@RequestBody @Valid RegisterFormBean user, BindingResult result, HttpServletResponse response, Locale locale) {
        log.info("Create Update user: " + user);
        if (result.hasErrors()) {
            log.warn("Create user failed. Username:" + user.getUsername() + " Errors:" + StringUtils.arrayToDelimitedString(result.getAllErrors().toArray(), ", "));
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return result.getAllErrors();
        }
        userHandler.createUser(user, locale);
        return Collections.emptyList();
    }

    @RequestMapping(value = "/rest/user", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ObjectError> updateUser(@RequestBody @Valid UserBean userBean, BindingResult result,
                                        @AuthenticationPrincipal AuthenticatedUser user,
                                        HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("Update user failed. Username:" + user.getUsername() + " Errors:" + StringUtils.arrayToDelimitedString(result.getAllErrors().toArray(), ", "));
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return result.getAllErrors();
        }
        log.info("Updating user: " + userBean.toString());
        userHandler.updateUser(userBean, user);
        return Collections.emptyList();
    }

    @RequestMapping(value = "/request-password-link", method = RequestMethod.POST)
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
        return "redirect:/login";
    }


    @RequestMapping(value = "/locales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public java.util.Map<String, String> getLocales() {
        return Language.getTransalationMap();
    }

    @RequestMapping(value = {"/rest/translation"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public java.util.Map<Object, Object> getDefaultTranslation(Locale locale) {
        final Map<String, String> speciesTranslation = speciesHandler.getSpeciesTranslation(locale);
        final Map<Object, Object> allMessages = messageSource.getAllMessages(locale);
        allMessages.putAll(speciesTranslation);
        return allMessages;
    }


    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object ping(final Principal principal) {
        log.debug("Ping" + principal);
        return principal == null ? null : new Principal() {
            @Override
            public String getName() {
                return principal.getName();
            }
        };
    }

}
