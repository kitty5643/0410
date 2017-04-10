package edu.ouhk.comps380f.model;

public class Attachment {

    private String name;
    private String mimeContentType;
    private byte[] contents;
    private Message message;
    private Long messageId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeContentType() {
        return mimeContentType;
    }

    public void setMimeContentType(String mimeContentType) {
        this.mimeContentType = mimeContentType;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
