package com.example.appnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class NotePage extends AppCompatActivity {
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);
        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);

        EditText note = findViewById(R.id.editTextTextMultiLine2);

        note.setText(MainActivity.title.get(index));


        //saving the notes as typing is finished
        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.title.set(index,charSequence.toString());
                MainActivity.arrayAdapter.notifyDataSetChanged();
                MainActivity.updateSharedPreference(MainActivity.sharedPreferences);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }
}