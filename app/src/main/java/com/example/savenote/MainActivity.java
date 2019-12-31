package com.example.savenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DBHelper dbHelper;
    ArrayList<Notes> notesList;
    NotesRecyclerAdapter adapter;

    private boolean fetchFromTrash = false;

    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepareFloationgActionButton();

        prepareNavDrawer(toolbar);


    }


    @Override
    protected void onResume() {
        super.onResume();

        // Todo Avoid fetching always from db , instead maintain cache

        // Fetch the notes list
        fetchFileInfo();

        // Show the fetched list in RLV
        showNotesList();

    }

    public void prepareNavDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void createNewnote() {
        Intent newNoteIntent = new Intent(MainActivity.this, NewnoteActivity.class);
        startActivity(newNoteIntent);
    }

    public void prepareFloationgActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewnote();
            }
        });
    }


    public void showNotesList() {

        RecyclerView notesListRecyclerView = (RecyclerView) findViewById(R.id.notesRecyclerView);
        LinearLayout zero_note = (LinearLayout) findViewById(R.id.zero_note);


        if (notesList == null || notesList.size() == 0) {
            // No notes
            Log.d(TAG, "No Notes");
            notesListRecyclerView.setVisibility(View.GONE);
            zero_note.setVisibility(View.VISIBLE);

            if (!fetchFromTrash) {
                TextView infoTextZeroNote = (TextView) findViewById(R.id.infoTextZeroNote);
                infoTextZeroNote.setText("Oops ,No Notes");
                TextView textView1 = (TextView) findViewById(R.id.textView1);
                textView1.setVisibility(View.VISIBLE);
                textView1.setText("Start adding notes by clicking on the add button");
            } else {
                TextView infoTextZeroNote = (TextView) findViewById(R.id.infoTextZeroNote);
                infoTextZeroNote.setText("Nothing in trash");
                TextView textView1 = (TextView) findViewById(R.id.textView1);
                textView1.setVisibility(View.GONE);
            }
        } else {

            Log.d(TAG, "Notes size in show ui " + notesList.size());
            zero_note.setVisibility(View.GONE);
            notesListRecyclerView.setVisibility(View.VISIBLE);
            notesListRecyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            notesListRecyclerView.setLayoutManager(layoutManager);

            adapter = new NotesRecyclerAdapter(notesList, this);
            notesListRecyclerView.setAdapter(adapter);
        }


    }


    public void fetchFileInfo() {
        dbHelper = new DBHelper(this);
        notesList = new ArrayList<Notes>();

        if (fetchFromTrash) {
            getSupportActionBar().setTitle("Deleted Notes");
            notesList = dbHelper.getTrashNotes();
        } else {
            getSupportActionBar().setTitle("Active Notes");
            notesList = dbHelper.getActiveNotes();
        }

        Log.d(TAG, "Notes Size in Fetchfile  is " + notesList.size());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.newNoteNavbar:
                createNewnote();
                break;

            case R.id.viewTrashNavbar:
                fetchFromTrash = true;
                onResume();
                break;

            case R.id.viewActiveNotesNavbar:
                fetchFromTrash = false;
                onResume();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
