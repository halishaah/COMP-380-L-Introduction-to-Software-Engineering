package com.example.todoapp;

// Java imports of pachkages
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateTodo extends AppCompatActivity {

    // Create a text field for the title, desc
    EditText update_title, update_desc;
    TextView update_date;
    DatePicker update_datePicker;

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

                Todo todo = new Todo(update_title.getText().toString(), update_desc.getText().toString(), date);
                System.out.println(todo.getDate());
                String title=todo.getTitle();
                String desc = todo.getDescription();


                //Set same id as in databse for that todo
                // Put default as one for error handling in case id does not show up
                todo.setId(intent.getIntExtra("id", 1));

                // Check to see of todo was updated
                if ((todo.checkTitle(title)==1) && (todo.checkDescription(desc)==1)) {
                    if (new Todo_Util(UpdateTodo.this).updateTodo(todo)) {
                        Toast.makeText(UpdateTodo.this, "Todo Updated", Toast.LENGTH_SHORT).show();

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
}