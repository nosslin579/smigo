package org.smigo.entities;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import java.util.Date;

public class Message {
  public static final String SMIGO_WALL = "smigowall";
  public static final int HELP = 1;
  public static final int GENERAL = 2;

  @SafeHtml(whitelistType = WhiteListType.NONE)
  private String message;
  private String column;
  private Integer location;
  private int poster;
  private Date postdate;

  public Message() {
  }

  public Message(String message, String column, Integer location, int poster, Date postdate) {
    this.poster = poster;
    this.message = message;
    this.location = location;
    this.column = column;
    this.postdate = postdate;
  }

  @Override
  public String toString() {
    return "org.smigo.entities.Messages poster:" + poster + ", location:" + location
             + ", column:" + column + ", message:" + message + ", postdate:" + postdate;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Return s the column, which can be user, species, rule or smigowall
   *
   * @return the column
   */
  public String getColumn() {
    return column;
  }

  /**
   * @param column the column to set
   */
  public void setColumn(String column) {
    this.column = column;
  }

  /**
   * Returns the location. Location can be a species address, user id or rule
   * id or wall id.
   *
   * @return the location
   */
  public Integer getLocation() {
    return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(Integer location) {
    this.location = location;
  }

  /**
   * @return the poster
   */
  public int getPoster() {
    return poster;
  }

  /**
   * @param poster the poster to set
   */
  public void setPoster(int poster) {
    this.poster = poster;
  }

  /**
   * Returns the time of posting
   *
   * @return the postdate
   */
  public Date getPostdate() {
    return postdate;
  }

  /**
   * @param postdate the postdate to set
   */
  public void setPostdate(Date postdate) {
    this.postdate = postdate;
  }
}
