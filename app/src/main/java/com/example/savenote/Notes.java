package com.example.savenote;

/**
 * Created by Swathi on 9/28/16.
 */

public class Notes {

    public Notes(String title, String content, String docId, int isActive) {
        this.title = title;
        this.content = content;
        this.docId = docId;
        this.isActive = isActive;
    }

    public String getDocId() {
        return docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public int getIsActive() {
        return isActive;
    }


    private String title;
    private String content;
    private String docId;
    private int isActive;

}
