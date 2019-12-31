package com.example.savenote;


/**
 * Created by Swathi on 9/28/16.
 */

public class AppConstants {

    public static String getTitle() {
        return TITLE;
    }

    public static String getContent() {
        return CONTENT;
    }

    public static String getActionEdit() {
        return ACTION_EDIT;
    }

    public static String getActionDone() {
        return ACTION_DONE;
    }

    public static String getDocId() {
        return DOC_ID;
    }

    public static String getNoteUpdateSuccess() {
        return NOTE_UPDATE_SUCCESS;
    }

    public static String getNoteInsertSuccess() {
        return NOTE_INSERT_SUCCESS;
    }

    public static String getTitleError() {
        return TITLE_ERROR;
    }

    public static String getMoveTrashSuccess() {
        return MOVE_TRASH_SUCCESS;
    }

    public static int getPurposeTitleDialog() {
        return PURPOSE_TITLE_DIALOG;
    }

    public static int getPurposeDeleteDialog() {
        return PURPOSE_DELETE_DIALOG;
    }

    public static int getPurposeAlertUserDialog() {
        return PURPOSE_ALERT_USER_DIALOG;
    }


    public static String getErrorDeletingNote() {
        return ERROR_DELETING_NOTE;
    }


    private static String TITLE = "title";
    private static String CONTENT = "content";
    private static String ACTION_EDIT = "EDIT";
    private static String DOC_ID = "docId";
    private static String ACTION_DONE = "DONE";
    private static String NOTE_UPDATE_SUCCESS = "Note updated successfully";
    private static String NOTE_INSERT_SUCCESS = "Note added successfully";
    private static String TITLE_ERROR = "Sorry , note cannot be saved without title";


    private static String MOVE_TRASH_SUCCESS = "Note moved to trash successfully ";


    private static String ERROR_DELETING_NOTE = "Error deleting note ";
    public static final int PURPOSE_TITLE_DIALOG = 0;
    public static final int PURPOSE_DELETE_DIALOG = 1;
    public static final int PURPOSE_ALERT_USER_DIALOG = 2;


}
