package com.louis.sortlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Lists> list = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Lists> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //Returns a viewholder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_list_layout,parent,false);
        return new ViewHolder(view);
    }

    //Updates the values of the different components in the view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listIdTextView.setText(list.get(position).getListId());
        holder.idTextView.setText(list.get(position).getId());
        holder.nameTextView.setText(list.get(position).getName());
    }

    //returns size of list
    @Override
    public int getItemCount() {
        return list.size();
    }

    //Creating ViewHolder Class for the list view
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameTextView;
        private final TextView listIdTextView;
        private final TextView idTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextViewId);
            listIdTextView = itemView.findViewById(R.id.listIdTextViewId);
            idTextView = itemView.findViewById(R.id.idTextViewId);
        }
    }

}
