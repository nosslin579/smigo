package org.smigo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that handles species specific type of requests.
 *
 * @author Christian Nilsson
 */
@Controller
public class TestController {
  @RequestMapping(value = "/test", method = RequestMethod.GET)
  public String handleDeleteYearForm() {
    return "test.jsp";
  }
}