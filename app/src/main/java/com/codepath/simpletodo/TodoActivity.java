package com.codepath.simpletodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class TodoActivity extends AppCompatActivity {

    private ArrayList<TodoItem> items;
    private TodoItemAdapter itemsAdapter;
    private ListView lvItems;

    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        final TodoDatabaseHelper dbHelper = new TodoDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new TodoItemAdapter(this,items);
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
                i.putExtra("text", items.get(position).text);
                i.putExtra("position",position);
                startActivityForResult(i, 20);
            }
        });
    }

    public void onAddItem(View view) {
        final EditText editText = (EditText)findViewById(R.id.etNewItem);
        final String itemText = editText.getText().toString();
        TodoItem object = new TodoItem();
        object.text = itemText;
        itemsAdapter.add(object);
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
            TodoItem todoItem = new TodoItem();
            todoItem.text= newText;
            items.set(position,todoItem);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }


    private void readItems(){

        final Cursor cursor = cupboard().withDatabase(db).query(TodoItem.class).getCursor();
        QueryResultIterable<TodoItem> iterate = cupboard().withCursor(cursor).iterate(TodoItem.class);
        items = new ArrayList<>();
        for (TodoItem todoItem : iterate) {
            items.add(todoItem);
        }
    }

    private void writeItems(){
        cupboard().withDatabase(db).delete(TodoItem.class,"");

        for (final TodoItem item : items) {

            cupboard().withDatabase(db).put(item);
        }
    }
}
