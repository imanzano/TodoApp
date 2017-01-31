package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private String text;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        text = getIntent().getStringExtra("text");
        position = getIntent().getIntExtra("position",-1);
        final EditText editText = (EditText)findViewById(R.id.editText);
        if ( text != null ) {
            editText.setText(text);
            editText.setSelection(editText.getText().length());
        }
    }

    public void onSave(View view) {

        final Intent data = new Intent();

        final EditText editText = (EditText)findViewById(R.id.editText);
        data.putExtra("position", position);
        data.putExtra("text", editText.getText().toString()); // ints work too

        setResult(RESULT_OK, data);
        this.finish();
    }
}
