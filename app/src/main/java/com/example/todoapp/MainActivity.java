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

import android.annotation.SuppressLint;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


import java.util.ArrayList;
import java.util.Calendar;

import static com.example.todoapp.R.layout.create_todo;

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
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
               YoYo.with(Techniques.Tada).duration(400).repeat(2).playOn(imageButton);
             //  randomAnimation(imageButton);
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(create_todo, null, false);

                EditText updateTitle = viewInput.findViewById(R.id.update_title);
                EditText updateDesc = viewInput.findViewById(R.id.update_desc);
                DatePicker updateDate = viewInput.findViewById(R.id.datePicker);
                CheckBox checkBox = viewInput.findViewById(R.id.checkBox);

                //repeat
                Spinner repeatSpinner = viewInput.findViewById(R.id.recurring_spinner);
                ArrayAdapter<CharSequence>adapterRepeat = ArrayAdapter.createFromResource(inflater.getContext(),R.array.repeat, android.R.layout.simple_spinner_item);
                adapterRepeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                repeatSpinner.setAdapter(adapterRepeat);

                //reminder
                Spinner reminderSpinner = viewInput.findViewById(R.id.reminder_spinner);
                ArrayAdapter<CharSequence>adapterReminder = ArrayAdapter.createFromResource(inflater.getContext(),R.array.reminder, android.R.layout.simple_spinner_item);
                adapterReminder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reminderSpinner.setAdapter(adapterReminder);



                new AlertDialog.Builder(MainActivity.this,R.style.AlertDialogStyle)

                        .setView(viewInput)
                        .setTitle("Add A Todo")
                        .setPositiveButton( "Add",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                YoYo.with(Techniques.Tada).duration(400).repeat(2).playOn(viewInput);
                                String title1 = updateTitle.getText().toString();
                                String desc1 = updateDesc.getText().toString();
                                String repeatSpinnerValue = repeatSpinner.getSelectedItem().toString();
                                String reminderSpinnerValue = reminderSpinner.getSelectedItem().toString();

                                //formats extra 0's
                                int day = updateDate.getDayOfMonth();
                                String day1;
                                if (day < 10) {
                                    day1 = "0" + day;
                                } else {
                                    day1 = String.valueOf(day);
                                }
                                int month = updateDate.getMonth() + 1;
                                String month1;
                                if (month < 10) {
                                    month1 = "0" + month;
                                } else {
                                    month1 = String.valueOf(month);
                                }
                                int year = updateDate.getYear();
                                String date = (month1 + "/" + day1 + "/" + year);

//                                get date and time and set calendar object
                                timePicker = (TimePicker) viewInput.findViewById(R.id.timePicker1);
//                                int day = datePicker.getDayOfMonth();
//                                int month = datePicker.getMonth();
//                                int year = datePicker.getYear();

                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.MONTH, updateDate.getMonth());
                                c.set(Calendar.DAY_OF_MONTH, updateDate.getDayOfMonth());
                                c.set(Calendar.YEAR, updateDate.getYear());
                                c.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                                c.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                                c.set(Calendar.SECOND, 0);
//                                end calendar set
//                                check if remind me box is checked
                                boolean remindMe = checkBox.isChecked();

                                Todo todo = new Todo(title1, desc1, date);
//                                checks title and desc
//                                need to get the id of row and create reminder
                                if ((todo.checkTitle(title1) == 1) && (todo.checkDescription(desc1) == 1)) {
                                    int todoID = new Todo_Util(MainActivity.this).createTodo(todo);
                                    boolean isInserted = todoID > 0;
                                    if (isInserted) {
                                        todo.setId(todoID);

                                        if(reminderSpinnerValue.equals("15 Minutes Before")){
                                            c.add(Calendar.MINUTE,-15);
                                        }
                                        else if(reminderSpinnerValue.equals("30 Minutes Before")){
                                            c.add(Calendar.MINUTE,-30);
                                        }
                                        else if(reminderSpinnerValue.equals("One Day Before")){
                                            c.add(Calendar.DATE,-1);
                                        }
                                        else if(reminderSpinnerValue.equals("at time")){
                                            setReminder(c, todo);
                                        }
                                          
                                        if(remindMe) {
                                            //check if no repeating
                                            if(repeatSpinnerValue.equals("Never")) {
                                                setReminder(c, todo);
                                                System.out.println("Never");
                                            }
                                            else{
                                                setRecurringRemind(c,todo, repeatSpinnerValue);
                                                System.out.println("recurring");
                                            }
                                          
                                        }
                                        Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        loadTheTodos();
             
                                    } else {
                                        Toast.makeText(MainActivity.this, "Error Saving", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.cancel();
                                } else {
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
                        }).setIcon(getResources().getDrawable(android.R.drawable.checkbox_on_background))
            .create().show();


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

                //fixes the search by reloading the todos array
                loadTheTodos();


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
                YoYo.with(Techniques.Tada).duration(600).repeat(2).playOn(view);
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
        intent.putExtra("frequency","Never");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,todo.getId(), intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }
    private void setRecurringRemind(Calendar c, Todo todo, String period){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("id_num",todo.getId());
        intent.putExtra("task_title",todo.getTitle());
        intent.putExtra("task_message",todo.getDescription());
        intent.putExtra("frequency",period);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,todo.getId(), intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
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







