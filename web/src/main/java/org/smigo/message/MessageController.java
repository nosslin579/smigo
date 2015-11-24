package org.smigo.message;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.AuthenticatedUser;
import org.smigo.user.MailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

@Controller
public class MessageController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private MailHandler mailHandler;

    @RequestMapping(value = "/rest/message", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Message> getMessages(@RequestParam Integer page, @RequestParam Integer size) {
        return messageHandler.getMessages(page, size);
    }

    @RequestMapping(value = "/rest/message", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public int addMessage(@RequestBody Message message, @AuthenticationPrincipal AuthenticatedUser user, HttpServletResponse response) {
        mailHandler.sendAdminNotification("message added to forum", message);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return 0;
        }
        return messageHandler.addMessage(message, user);
    }
}
