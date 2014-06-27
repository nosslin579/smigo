package org.smigo;

import org.smigo.persitance.UserSession;
import kga.Garden;
import kga.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class ViewBean {
  @Autowired
  private UserSession userSession;

  public Collection<Square> getSquares(){
	return Collections.singletonList(new Square(2002,3,3,new Garden()));
//    return userSession.getGarden().getSquares().values();
  }
}
