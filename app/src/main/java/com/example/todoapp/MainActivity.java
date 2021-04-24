package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    ArrayList<Todo> todos;
    RecyclerView recyclerView;
    TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.create);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.create_todo, null, false);

                EditText updateTitle = viewInput.findViewById(R.id.update_title);
                EditText updateDesc = viewInput.findViewById(R.id.update_desc);
                DatePicker updateDate = viewInput.findViewById(R.id.datePicker);
                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)
                        .setTitle("Add A Todo")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title1 = updateTitle.getText().toString();
                                String desc1 = updateDesc.getText().toString();
                                //formats extra 0's
                                int day = updateDate.getDayOfMonth();
                                String day1;
                                if(day<10){
                                    day1="0"+day;
                                }else{
                                    day1=String.valueOf(day);
                                }
                                int month = updateDate.getMonth()+1;
                                String month1;
                                if(month<10){
                                    month1="0"+month;
                                }else{
                                    month1=String.valueOf(month);
                                }
                                int year = updateDate.getYear();
                                String date = (month1+"/"+day1+"/"+year);


                                Todo todo = new Todo(title1, desc1,date);

                                if ((todo.checkTitle(title1)==1) && (todo.checkDescription(desc1)==1)) {
                                    boolean isInserted = new Todo_Util(MainActivity.this).createTodo(todo);
                                    if (isInserted) {
                                        Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        loadTheTodos();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Error Saving", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.cancel();
                                }else{
                                        if (todo.checkTitle(title1) == 0 || todo.checkDescription(desc1) == 0) {
                                            if (title1.trim().length() == 0) {
                                                Toast.makeText(MainActivity.this, "Error Title is Mandatory", Toast.LENGTH_SHORT).show();
                                            }
                                            if (desc1.trim().length() == 0) {
                                                Toast.makeText(MainActivity.this, "Error Description is Mandatory", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                }
                            }
                        }).show();

            }
        });

        recyclerView = findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager((MainActivity.this)));

        // For swiping
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Call the delete function in util file and get the id
                new Todo_Util(MainActivity.this).deleteTodo(todos.get(viewHolder.getAdapterPosition()).getId());

                // Remove from array list
                todos.remove(viewHolder.getAdapterPosition());

                // Notify the adapter of the removal
                todoAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);


        // Attach to recyvler view
        itemTouchHelper.attachToRecyclerView((recyclerView));

        loadTheTodos();
    }

    public ArrayList<Todo> getTodos() {
        ArrayList<Todo> todos = new Todo_Util(this).getTodos();

        return todos;
    }

    // Load the todos
    public void loadTheTodos() {
        todos = getTodos();
        todoAdapter = new TodoAdapter(todos, this, new TodoAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                editTodo(todos.get(position).getId(), view);
            }
        });
        recyclerView.setAdapter(todoAdapter);
    }

    // Open the edittodo dialog
    private void editTodo(int todoId, View view) {
        Todo_Util todoUtil = new Todo_Util(this);
        Todo todo = todoUtil.getTodo(todoId);
        Intent intent = new Intent(this, UpdateTodo.class);
        intent.putExtra("title", todo.getTitle());
        intent.putExtra("description", todo.getDescription());
        intent.putExtra("date",todo.getDate());
        intent.putExtra("id", todo.getId());

        // For the update animation
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent, 1, optionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            loadTheTodos();
        }
    }



    //Search


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_main, menu);

        MenuItem  searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                todoAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}







