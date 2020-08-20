package com.example.appnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> title;
    static ArrayAdapter<String> arrayAdapter;
    ListView noteList;
    static SharedPreferences sharedPreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.appnotes", Context.MODE_PRIVATE);
        noteList = findViewById(R.id.notes);
        try {
            title = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("noteString",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (title.size() == 0) {
            title.add("Example Notes");
        }
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,title);
        noteList.setAdapter(arrayAdapter);
        intent = new Intent(getApplicationContext(), NotePage.class);

        //access to the note page
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent.putExtra("index",i);
                startActivity(intent);
            }
        });

        //long click the icon to delete it
        noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deleting the notes")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                title.remove(i);
                                arrayAdapter.notifyDataSetChanged();
                                updateSharedPreference(sharedPreferences);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.newnotes:
                title.add("");
                intent.putExtra("index",title.size()-1);
                startActivity(intent);
                return true;
            default:
                return false;

        }
    }

    //update the array to the shared preference
    public static void updateSharedPreference(SharedPreferences sharedPreferences) {
        try {
            sharedPreferences.edit().putString("noteString",ObjectSerializer.serialize(title)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}