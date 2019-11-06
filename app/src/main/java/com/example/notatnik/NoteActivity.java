package com.example.notatnik;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class NoteActivity extends AppCompatActivity {

    private Toolbar toolbarView;
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.add_note){
            Intent intent = new Intent(getApplicationContext(), EditorActivity.class);

            startActivity(intent);

            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        toolbarView = (Toolbar) findViewById(R.id.toolbarView);
        setSupportActionBar(toolbarView);

        ListView listView = (ListView) findViewById(R.id.listView);

        //sprawdzenie zapisanych
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notatnik", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notatki", null);

        //przywracanie listy z zapisanych
        if(set == null) {
            notes.add("Przykładowa notatka");
        }
        else {
            notes = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(NoteActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Uwaga")
                        .setMessage("Czy na pewno chcesz usunąć notatke?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                //zapisywanie na stałe
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notatnik", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet(NoteActivity.notes);
                                sharedPreferences.edit().putStringSet("notatki", set).apply();
                            }
                        })
                        .setNegativeButton("Nie", null)
                        .show();

                return true;
            }
        });
    }
}
