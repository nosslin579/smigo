package org.smigo.message;

import java.util.Date;

public class Message {
    private int id;
    private String text;
    private String submitter;
    private Date createdate;

    public Message() {
    }

    public Message(int id, String text, String submitter, Date createdate) {
        this.id = id;
        this.text = text;
        this.submitter = submitter;
        this.createdate = createdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}
