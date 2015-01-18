package org.smigo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Locale;

@Controller
public class AcceptTermsofServiceController {

    @Autowired
    private UserSession userSession;
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/accept-termsofservice", method = RequestMethod.GET)
    public String acceptTermsOfService(Model model) {
        model.addAttribute(new AcceptTermsOfService());
        return "accept-tos.jsp";
    }

    @RequestMapping(value = "/accept-termsofservice", method = RequestMethod.POST)
    public String acceptTermsOfService(@Valid AcceptTermsOfService acceptTermsOfService, BindingResult result,
                                       Locale locale, @AuthenticationPrincipal AuthenticatedUser user) {
        if (result.hasErrors()) {
            return "accept-tos.jsp";
        }

        final UserBean userBean = userSession.getUser();
        userBean.setTermsOfService(true);
        userBean.setLocale(locale);
        userDao.updateUser(user.getId(), userBean);

        return "redirect:/";
    }
}
