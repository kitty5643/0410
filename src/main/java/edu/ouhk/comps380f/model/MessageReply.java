package edu.ouhk.comps380f.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class MessageReply {

    private long id;
    private long id_topic;
    private String customerName;
    private String body;
    private Map<String, Attachment> attachments = new LinkedHashMap<>();
    Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_topic() {
        return id_topic;
    }

    public void setId_topic(long id_topic) {
        this.id_topic = id_topic;
    }

    public String getCustomerName() {
        return customerName;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Attachment getAttachment(String name) {
        return this.attachments.get(name);
    }

    public Collection<Attachment> getAttachments() {
        return this.attachments.values();
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.put(attachment.getName(), attachment);
    }

    public int getNumberOfAttachments() {
        return this.attachments.size();
    }

    public boolean hasAttachment(String name) {
        return this.attachments.containsKey(name);
    }

    public Attachment deleteAttachment(String name) {
        return this.attachments.remove(name);
    }

}
