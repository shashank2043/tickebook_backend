package org.team11.tickebook.auth_service.client;

import java.io.Serializable;

public class Mail implements Serializable {
    String tomail;
    String sub;
    String content;

    public Mail(String tomail, String sub, String content) {
        this.tomail = tomail;
        this.sub = sub;
        this.content = content;
    }

    public Mail() {
    }

    public String getTomail() {
        return tomail;
    }

    public void setTomail(String tomail) {
        this.tomail = tomail;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
