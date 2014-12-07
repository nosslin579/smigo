package org.smigo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.user.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@Controller
public class MessageController implements Serializable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageHandler messageHandler;

    @RequestMapping(value = "/rest/message", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public List<Message> getMessages(@RequestParam Integer page, @RequestParam Integer size) {
        return messageHandler.getMessages(page, size);
    }

    @RequestMapping(value = "/rest/message", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public int addMessage(@RequestBody Message message, @AuthenticationPrincipal AuthenticatedUser user) {
        return messageHandler.addMessage(message, user);
    }
}