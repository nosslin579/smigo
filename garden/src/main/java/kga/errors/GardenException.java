package kga.errors;

/**
 * Garden exception
 *
 * @author Christian Nilsson
 */
public class GardenException extends RuntimeException {
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
