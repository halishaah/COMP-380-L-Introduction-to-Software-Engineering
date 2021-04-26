package com.example.todoapp;

import android.text.Layout;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class  TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> implements Filterable {
    ArrayList<Todo> todos;
    Context context;
    ItemClicked itemClicked;
    ViewGroup parent;

    //make a copy of todos list
    ArrayList<Todo> todosFull;

    // To communicate between adapter and main activity
    public TodoAdapter(ArrayList<Todo> arrayList, Context context, ItemClicked itemClicked) {
        todos = arrayList;
        this.context = context;
        this.itemClicked = itemClicked;

        todosFull = new ArrayList<>(todos);
    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_list, parent, false);
        this.parent = parent;

        // Call the TodoHolder method and pass in the view created
        return new TodoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
        // Set the data to its specific element
        holder.title.setText(todos.get(position).getTitle());
        holder.description.setText(todos.get(position).getDescription());
        holder.date.setText(todos.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    // Instance of the todoholder class
    class TodoHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        ImageView editImage;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_todo_name);
            description = itemView.findViewById(R.id.txt_todo_description);
            date=itemView.findViewById(R.id.txt_todo_date);
            editImage = itemView.findViewById(R.id.img_edit);

            // Expand the description box by clicking
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (description.getMaxLines() == 1) {
                        description.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        description.setMaxLines(1);
                    }
                    TransitionManager.beginDelayedTransition(parent);
                }
            });

            // Set on click listener for editing note
            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onClick((getAdapterPosition()), itemView);
                }
            });
        }
    }

    interface ItemClicked {
        void onClick(int position, View view);
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Todo> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(todosFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Todo item : todosFull){
                    if(item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    if(item.getDescription().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    if(item.getDate().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            todos.clear();
            todos.addAll((ArrayList) results.values);
            notifyDataSetChanged();

        }
    };
}