package org.sourceforge.kga.errors;

/**
 * Garden exception
 *
 * @author Christian Nilsson
 */
public class GardenException extends Exception {
  private static final long serialVersionUID = 1L;

  private String messageKey;

  public GardenException(String message, String messageKey) {
    super(message);
    this.messageKey = messageKey;
  }

  public String getMessageKey() {
    return messageKey;
  }
}
