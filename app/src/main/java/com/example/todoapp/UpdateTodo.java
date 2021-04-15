package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class UpdateTodo extends AppCompatActivity {

    EditText update_title, update_desc;
    Button btnExit, btnSave;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);

        // Get the todo object inorder to display it in the update box
        Intent intent = getIntent();

        linearLayout = findViewById(R.id.btn_util);
        update_title = findViewById(R.id.update_up_title);
        update_desc = findViewById(R.id.update_up_desc);
        btnExit = findViewById(R.id.btn_exit);
        btnSave = findViewById(R.id.btn_save);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo todo = new Todo(update_title.getText().toString(), update_desc.getText().toString());

                //Set same id as in databse for that todo
                // Put default as one for error handling in case id does not show up
                todo.setId(intent.getIntExtra("id", 1));
                if ((todo.checkTitle(todo.getTitle()) == 1) && (todo.checkDescription(todo.getDescription()) == 1)) {
                    if (new Todo_Util(UpdateTodo.this).updateTodo(todo)) {
                        Toast.makeText(UpdateTodo.this, "Todo Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateTodo.this, "Update Failed, Please try again!", Toast.LENGTH_SHORT).show();
                    }

                    // Simulate going back to homepage animation
                    btnSave.setVisibility(View.GONE);
                    btnExit.setVisibility(View.GONE);
                    TransitionManager.beginDelayedTransition(linearLayout);
                    onBackPressed();
                    update_title.setText(intent.getStringExtra("title"));
                    update_desc.setText(intent.getStringExtra("description"));
                }else{
                    if (todo.checkTitle(todo.getTitle()) == 0 || todo.checkDescription(todo.getDescription()) == 0) {
                        if (todo.getTitle().trim().length() == 0) {
                            Toast.makeText(UpdateTodo.this, "Error Title is Mandatory", Toast.LENGTH_SHORT).show();

                        }
                        if (todo.getDescription().trim().length() == 0) {
                            Toast.makeText(UpdateTodo.this, "Error Description is Mandatory", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
    }




    // Animation to set button visibility to none after clicked
    @Override
    public void onBackPressed() {
        btnSave.setVisibility(View.GONE);
        btnExit.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(linearLayout);
        super.onBackPressed();
    }
}
