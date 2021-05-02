package com.example.todoapp;

// Java imports of pachkages
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Calendar;

public class UpdateTodo extends AppCompatActivity {

    // Create a text field for the title, desc
    EditText update_title, update_desc;
    TextView update_date;
    DatePicker update_datePicker;
    TimePicker timePicker2;
    CheckBox checkbox2;

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

        // This allows the user to click the save button to save updates to the todo
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String date = dateFormat();

                Todo todo = new Todo(update_title.getText().toString(), update_desc.getText().toString(), date);
                String title=todo.getTitle();
                String desc = todo.getDescription();

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
                        //update Reminder method
                        updateReminder(cUpdate,todo, checkbox2.isChecked());
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
            System.out.println(intent.getStringExtra("task_title"));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,todo.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        }
    }
}