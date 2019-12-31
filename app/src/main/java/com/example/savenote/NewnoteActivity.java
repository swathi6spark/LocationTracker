package com.example.savenote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewnoteActivity extends AppCompatActivity {

    String title;
    String content = ""; // content is the content of note , after every Db update 'content' and 'noteEditText' should be in sync
    String docId;
    Bundle extras; // Extras Bundle differentiates b/w old and new notes
    DBHelper dbHelper;

    EditText noteEditText;
    KeyListener editTextListener;
    Button footerActionButton;

    DialogFragment titleDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);

        getInfoFromLaunchingActivity();

        customizeActionBar();

        setContentView(R.layout.activity_new_note);

        prepareUI();

    }

    public void customizeActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void getInfoFromLaunchingActivity() {
        extras = getIntent().getExtras();
        if (extras != null) {
            // Opening old noteEditText
            docId = extras.getString(AppConstants.getDocId());
            title = extras.getString(AppConstants.getTitle());
            content = extras.getString(AppConstants.getContent());
        } else {
            // Creating new noteEditText
            docId = Utils.getCurrentTimeStamp();
        }
    }


    public void prepareUI() {

        prepareEditor();


        prepareFooterButton();


    }


    public void prepareEditor() {
        noteEditText = (EditText) findViewById(R.id.note);
        noteEditText.setText(content);
        editTextListener = noteEditText.getKeyListener();
        // Disable edit incase of opening an old note
        if (!TextUtils.isEmpty(content)) {
            noteEditText.setKeyListener(null);
        }
    }


    public void updateUIPostDBUpdate() {
        if (noteEditText != null) {
            noteEditText.clearFocus();
        }
        if (footerActionButton != null) {
            footerActionButton.setText(AppConstants.getActionEdit());
        }

    }

    public void saveDocToDB() {
        if (extras != null) {
            // Update the note
            if (!TextUtils.equals(content, noteEditText.getText().toString())) {
                dbHelper.deleteNote(docId); // Todo check return value
                docId = Utils.getCurrentTimeStamp(); // update docId to make it more recent
                dbHelper.insertNote(docId, title, noteEditText.getText().toString(), 1);

                content = noteEditText.getText().toString();
                Toast.makeText(TodoNotes.getContext(), AppConstants.getNoteUpdateSuccess(), Toast.LENGTH_LONG).show();
            } else {
                // No change in  note content , no need to update
            }
            updateUIPostDBUpdate();

        } else {
            // New note
            titleDialog = new MyAlertDialogFragment().newInstance(AppConstants.getPurposeTitleDialog());
            titleDialog.show(getSupportFragmentManager(), null);
        }
    }

    public void setNoteEditable(boolean isEditable) {
        if (isEditable) {
            noteEditText.setKeyListener(editTextListener);
            noteEditText.requestFocus();
            noteEditText.setSelection(noteEditText.getText().length());
        } else {
            noteEditText.setKeyListener(null);
        }
    }

    public void prepareFooterButton() {

        footerActionButton = (Button) findViewById(R.id.action);

        if (extras != null) {
            // Old note
            footerActionButton.setText(AppConstants.getActionEdit());
            setNoteEditable(false);
        } else {
            // New note
            footerActionButton.setText(AppConstants.getActionDone());
            setNoteEditable(true);

        }

        footerActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (footerActionButton.getText().equals(AppConstants.getActionEdit())) {
                    // Start Editing and change the text of footer button
                    setNoteEditable(true);
                    footerActionButton.setText(AppConstants.getActionDone());
                } else {
                    // Editing done , apply changes to DB
                    setNoteEditable(false);
                    saveDocToDB();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (!noteEditText.getText().toString().equals(content)) {
            MyAlertDialogFragment alertDialogFragment = MyAlertDialogFragment.newInstance(AppConstants.getPurposeAlertUserDialog());
            alertDialogFragment.show(getSupportFragmentManager(), "");
        } else {
            super.onBackPressed();
        }

//        if (footerActionButton != null && footerActionButton.getText().equals(AppConstants.getActionDone())) {
//            if (extras == null) {
//                // In New Doc
//                if (TextUtils.isEmpty(noteEditText.getText().toString())) {
//                    // User not missing anything
//                    super.onBackPressed();
//                } else {
//                    // Probably user misses something
//                    MyAlertDialogFragment alertDialogFragment = MyAlertDialogFragment.newInstance(AppConstants.getPurposeAlertUserDialog());
//                    alertDialogFragment.show(getSupportFragmentManager(), "");
//                }
//            } else {
//                // user edited existing doc
//                MyAlertDialogFragment alertDialogFragment = MyAlertDialogFragment.newInstance(AppConstants.getPurposeDeleteDialog());
//                alertDialogFragment.show(getSupportFragmentManager(), "");
//            }
//        } else {
//            super.onBackPressed();
//        }


    }


    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int purpose) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("purpose", purpose);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int purpose = getArguments().getInt("purpose");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


            switch (purpose) {

                case AppConstants.PURPOSE_ALERT_USER_DIALOG:

                    // Show alert Dialog
                    builder.setTitle("Alert")
                            .setMessage("Are you sure you want to close without saving the current note ?")
                            .setPositiveButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    }
                            )
                            .setNegativeButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            getActivity().finish();
                                        }
                                    }
                            );
                    break;


                case AppConstants.PURPOSE_TITLE_DIALOG:

                    // Show Title Dialog
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View view = inflater.inflate(R.layout.title_dialog, null);
                    builder.setView(view);

                    final EditText newNoteTitleET = (EditText) view.findViewById(R.id.newNoteTitle);


                    builder.setTitle("Title your note")
                            .setPositiveButton("Done",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            // Todo enable this button only if the title is non empty
                                            ((NewnoteActivity) getActivity()).saveNewnoteToDB(newNoteTitleET.getText().toString());
                                        }
                                    }
                            )
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            ((NewnoteActivity) getActivity()).onUserCancelClick();
                                        }
                                    }
                            );
                    break;

                case AppConstants.PURPOSE_DELETE_DIALOG:
                    // Show Delete Dialog
                    builder.setTitle("Delete")
                            .setMessage("This action will delete the current note")
                            .setPositiveButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            // Todo enable this button only if the title is non empty
                                        }
                                    }
                            )
                            .setNegativeButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            ((NewnoteActivity) getActivity()).deleteCurrentNote();
                                        }
                                    }
                            );
                    break;

            }


            return builder.create();
        }
    }

    public void saveNewnoteToDB(String title) {
        Log.d("MainActivity", "Inserting " + docId + "-" + title + "-" + noteEditText.getText().toString());

        dbHelper.insertNote(docId, title, noteEditText.getText().toString(), 1);
        content = noteEditText.getText().toString();

        Toast.makeText(TodoNotes.getContext(), AppConstants.getNoteInsertSuccess(), Toast.LENGTH_LONG).show();

        extras = new Bundle(); // Since the note needs to behave as old once it is saved in db
        updateUIPostDBUpdate();

    }

    public void deleteCurrentNote() {

        // Todo Should not be able to delete  unsaved new note

        Log.d("MainActivity", "Deleting " + docId + "-" + title + "-" + noteEditText.getText().toString());
        boolean isDeleted = dbHelper.updateNote(docId, title, noteEditText.getText().toString(), 0);
        if (isDeleted) {
            Toast.makeText(TodoNotes.getContext(), AppConstants.getMoveTrashSuccess(), Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(TodoNotes.getContext(), AppConstants.getErrorDeletingNote(), Toast.LENGTH_LONG).show();
        }
    }

    public void onUserCancelClick() {
        Toast.makeText(TodoNotes.getContext(), AppConstants.getTitleError(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_menu, menu);
        if (TextUtils.isEmpty(content)) {
            MenuItem delete = (MenuItem) menu.findItem(R.id.deleteNote);
            delete.setVisible(false);
            invalidateOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteNote) {
            MyAlertDialogFragment alertDialogFragment = MyAlertDialogFragment.newInstance(AppConstants.getPurposeDeleteDialog());
            alertDialogFragment.show(getSupportFragmentManager(), "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}