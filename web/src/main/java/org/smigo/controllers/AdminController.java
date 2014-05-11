package org.smigo.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.persitance.DatabaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
  private static final Logger log = LoggerFactory.getLogger(AdminController.class);

  private DatabaseResource databaseresource;

  @Autowired
  public AdminController(DatabaseResource databaseresource) {
    log.debug("Creating new AdminController");
    this.databaseresource = databaseresource;
  }

  @RequestMapping(value = "/korv")
  public void korv() {
    databaseresource.updateIconFileName();
  }
}
