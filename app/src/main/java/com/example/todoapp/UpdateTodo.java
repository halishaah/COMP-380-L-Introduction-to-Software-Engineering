package com.example.todoapp;

// Java imports of pachkages
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class UpdateTodo extends AppCompatActivity {

    // Create a text field for the title, desc
    EditText update_title, update_desc;
    TextView update_date;
    DatePicker update_datePicker;
    TimePicker timePicker2;
    CheckBox checkbox2;
    Button addGoogle;

    // Create the save and exit buttons
    Button btnExit, btnSave;

    LinearLayout linearLayout;

    // This is the method that updates the todo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);

        // Get the todo object inorder to display it in the update box
        Intent intent = getIntent();

        linearLayout = findViewById(R.id.btn_util);

        update_title = findViewById(R.id.update_up_title);
        update_desc = findViewById(R.id.update_up_desc);
        View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
        update_date = (TextView) inflatedView.findViewById(R.id.txt_todo_date);
        timePicker2 = (TimePicker) findViewById(R.id.timePicker2);
        checkbox2 = (CheckBox) findViewById(R.id.checkBox2);
        addGoogle = findViewById(R.id.addGoogle);
        //populates the spinner
        Spinner repeatSpinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapterRepeat = ArrayAdapter.createFromResource(this,R.array.repeat, android.R.layout.simple_spinner_item);
        adapterRepeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(adapterRepeat);


        //populates the spinner for reminder
        Spinner reminderSpinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapterReminder = ArrayAdapter.createFromResource(this,R.array.reminder, android.R.layout.simple_spinner_item);
        adapterReminder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderSpinner.setAdapter(adapterReminder);


        update_datePicker=findViewById(R.id.datePicker);
        btnExit = findViewById(R.id.btn_exit);
        btnSave = findViewById(R.id.btn_save);

        // This allows the user to press exit to discard the changes
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!update_title.getText().toString().isEmpty() && !update_desc.getText().toString().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    String calName = "vnd.android.cursor.item/event";
                    intent.setType(calName);

                    Calendar cal = Calendar.getInstance();
                    long startTime = cal.getTimeInMillis();
                    long endTime = cal.getTimeInMillis() + 60 * 60 * 1000;

                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);

                    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                    intent.putExtra(CalendarContract.Events.TITLE, update_title.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, update_desc.getText().toString());
                    intent.putExtra(Intent.EXTRA_EMAIL, "test@gmail.com, test2@gmail.com");

                    if (calName == "vnd.android.cursor.item/event") {
                        startActivity(intent);
                    } else {
                        Toast.makeText(UpdateTodo.this, "No calendar found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateTodo.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // This allows the user to click the save button to save updates to the todo
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String date = dateFormat();

                Todo todo = new Todo(update_title.getText().toString(), update_desc.getText().toString(), date);
                String title=todo.getTitle();
                String desc = todo.getDescription();
                String repeatSpinnerValue = repeatSpinner.getSelectedItem().toString();

                String reminderSpinnerValue = reminderSpinner.getSelectedItem().toString();

                //set calendar object to selected date and time
                Calendar cUpdate = Calendar.getInstance();
                cUpdate.set(Calendar.MONTH,update_datePicker.getMonth());
                cUpdate.set(Calendar.DAY_OF_MONTH,update_datePicker.getDayOfMonth());
                cUpdate.set(Calendar.YEAR,update_datePicker.getYear());
                cUpdate.set(Calendar.HOUR_OF_DAY,timePicker2.getCurrentHour());
                cUpdate.set(Calendar.MINUTE,timePicker2.getCurrentMinute());
                cUpdate.set(Calendar.SECOND,0);
                //Set same id as in databse for that todo
                // Put default as one for error handling in case id does not show up
                todo.setId(intent.getIntExtra("id", 1));

                // Check to see of todo was updated
                if ((todo.checkTitle(title)==1) && (todo.checkDescription(desc)==1)) {
                    if (new Todo_Util(UpdateTodo.this).updateTodo(todo)) {
                        Toast.makeText(UpdateTodo.this, "Todo Updated", Toast.LENGTH_SHORT).show();

                        //for 15 min, 30 min, and one day before
                        if(reminderSpinnerValue.equals("15 Minutes Before")){
                            cUpdate.add(Calendar.MINUTE,-15);
                        }
                        else if(reminderSpinnerValue.equals("30 Minutes Before")){
                            cUpdate.add(Calendar.MINUTE,-30);
                        }
                        else if(reminderSpinnerValue.equals("One Day Before")){
                            cUpdate.add(Calendar.DATE,-1);
                        }
                        else if(reminderSpinnerValue.equals("at time")){
                            updateReminder(cUpdate, todo, checkbox2.isChecked());
                        }

                        //update Reminder method
                        //check if repeating
                        if(repeatSpinnerValue.equals("Never")) {
                            updateReminder(cUpdate, todo, checkbox2.isChecked());
                        }
                        else{
                            updateReminderRecurring(cUpdate, todo, checkbox2.isChecked(),repeatSpinnerValue);
                        }
                    } else {
                        Toast.makeText(UpdateTodo.this, "Update Failed, Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (todo.checkTitle(title) == 0 || todo.checkDescription(desc) == 0) {
                        if (title.trim().length() == 0) {
                            Toast.makeText(UpdateTodo.this, "Error Title is Mandatory", Toast.LENGTH_SHORT).show();
                        }
                        if (desc.trim().length() == 0) {
                            Toast.makeText(UpdateTodo.this, "Error Description is Mandatory", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                // Simulate going back to homepage animation
                btnSave.setVisibility(View.GONE);
                btnExit.setVisibility(View.GONE);
                addGoogle.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(linearLayout);
                onBackPressed();
            }
        });

        update_title.setText(intent.getStringExtra("title"));
        update_desc.setText(intent.getStringExtra("description"));

      String s = intent.getStringExtra("date").toString();
           // update_datePicker.updateDate(intent.getIntExtra("month",));

        String[] m= s.split("/",2);

       int mm = Integer.parseInt(m[0])-1;
        String[] d = m[1].split("/",2);

      int dd = Integer.parseInt(d[0]);
        String y= d[1];
        int yy = Integer.parseInt(d[1]);
        update_datePicker.init(yy,mm,dd,DatePicker::updateDate);

    }

    // Animation to set button visibility to none after clicked. This is for a smoother trasnsition back to home page
    @Override
    public void onBackPressed() {
        btnSave.setVisibility(View.GONE);
        btnExit.setVisibility(View.GONE);
        addGoogle.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(linearLayout);
        super.onBackPressed();
    }
    public String dateFormat(){
        int day = update_datePicker.getDayOfMonth();
        String day1;
        if(day<10){
            day1="0"+day;
        }else{
            day1=String.valueOf(day);
        }
        int month = update_datePicker.getMonth()+1;
        String month1;
        if(month<10){
            month1="0"+month;
        }else{
            month1=String.valueOf(month);
        }
        int year = update_datePicker.getYear();
        String date = (month1+"/"+day1+"/"+year);
        return date;
    }
//    update the reminder when todo is updated.
    public void updateReminder(Calendar c, Todo todo, boolean newReminder){
        //cancel old reminder
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intentCancel = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this,todo.getId(), intentCancel, 0);
        alarmManager.cancel(pendingIntentCancel);
        pendingIntentCancel.cancel();
        //new reminder
        if(newReminder) {
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("id_num",todo.getId());
            intent.putExtra("task_title",todo.getTitle());
            intent.putExtra("task_message",todo.getDescription());
            intent.putExtra("frequency", "Never");
            System.out.println(intent.getStringExtra("task_title"));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,todo.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public void updateReminderRecurring(Calendar c, Todo todo, boolean newReminder, String period){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intentCancel = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this,todo.getId(), intentCancel, 0);
        alarmManager.cancel(pendingIntentCancel);
        pendingIntentCancel.cancel();
        //new reminder
        if(newReminder) {
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("id_num",todo.getId());
            intent.putExtra("task_title",todo.getTitle());
            intent.putExtra("task_message",todo.getDescription());
            intent.putExtra("frequency", period);
            System.out.println(intent.getStringExtra("task_title"));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,todo.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        }
    }
}