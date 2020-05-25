package edu.harvard.cs50.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {
    private EditText editText;
    Button delete_note;
    int id;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


         intent = getIntent();
         id = intent.getIntExtra("id", 0);
        editText = findViewById(R.id.note_edit_text);
        delete_note=findViewById(R.id.delete);
        editText.setText(intent.getStringExtra("content"));
        delete_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.database.noteDao().delete(id);
                onDestroy();
            }
        });
    }

    @Override
    protected void onPause() {
        MainActivity.database.noteDao().save(editText.getText().toString(),id);
        super.onPause();



    }
}
