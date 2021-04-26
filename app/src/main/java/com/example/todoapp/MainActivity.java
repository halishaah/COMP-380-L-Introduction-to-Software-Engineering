package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    ArrayList<Todo> todos;
    RecyclerView recyclerView;
    TodoAdapter todoAdapter;
    TimePicker timePicker;


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
                CheckBox checkBox = viewInput.findViewById(R.id.checkBox);
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

//                                get date and time and set calendar object
                                timePicker = (TimePicker) viewInput.findViewById(R.id.timePicker1);
//                                int day = datePicker.getDayOfMonth();
//                                int month = datePicker.getMonth();
//                                int year = datePicker.getYear();

                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.MONTH,updateDate.getMonth());
                                c.set(Calendar.DAY_OF_MONTH,updateDate.getDayOfMonth());
                                c.set(Calendar.YEAR,updateDate.getYear());
                                c.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                                c.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                                c.set(Calendar.SECOND,0);
//                                end calendar set
//                                check if remind me box is checked
                                boolean remindMe = checkBox.isChecked();

                                Todo todo = new Todo(title1, desc1,date);
//                                checks title and desc
//                                need to get the id of row and create reminder
                                if ((todo.checkTitle(title1)==1) && (todo.checkDescription(desc1)==1)) {
                                    int todoID = new Todo_Util(MainActivity.this).createTodo(todo);
                                    boolean isInserted = todoID > 0;
                                    if (isInserted) {
//                                      Could also just pass the id and not set it yet.
                                        todo.setId(todoID);
//                                      ---------------------------------------------
                                        if(remindMe)
                                            setReminder(c, todo);
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
                //Cancel reminders for todo
                cancelReminder(todos.get(viewHolder.getAdapterPosition()).getId());
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

    //set reminder
    private void setReminder(Calendar c, Todo todo){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("id_num",todo.getId());
        intent.putExtra("task_title",todo.getTitle());
        intent.putExtra("task_message",todo.getDescription());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,todo.getId(), intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//            System.out.println(Build.VERSION.SDK_INT+">="+ Build.VERSION_CODES.O);
//            System.out.println(c.getTime().toString());
//            System.out.println(DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
        }
    }
    private void cancelReminder(int idNum){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,idNum, intent, 0);
        alarmManager.cancel(pendingIntent);

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



}







