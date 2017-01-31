package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
                i.putExtra("text", items.get(position));
                i.putExtra("position",position);
                startActivityForResult(i, 20);
            }
        });
    }

    public void onAddItem(View view) {
        final EditText editText = (EditText)findViewById(R.id.etNewItem);
        final String itemText = editText.getText().toString();
        itemsAdapter.add(itemText);
        editText.setText("");
        writeItems();
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == 20) {
            // Extract name value from result extras
            String newText = data.getExtras().getString("text");
            int position = data.getExtras().getInt("position");

            items.set(position,newText);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }


    private void readItems(){
        final File filesDir = getFilesDir();
        final File todoFile = new File(filesDir,"todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (final IOException e)
        {
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        final File filesDir = getFilesDir();
        final File todoFile = new File(filesDir,"todo.txt");
        try {
            FileUtils.writeLines(todoFile,items);
        } catch (final IOException e)
        {
            e.printStackTrace();
        }
    }
}
