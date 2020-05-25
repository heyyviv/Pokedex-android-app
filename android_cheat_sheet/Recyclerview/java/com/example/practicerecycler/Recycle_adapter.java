package com.example.practicerecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Recycle_adapter extends RecyclerView.Adapter<Recycle_adapter.myViewHolder> {
    Context this_context;

    int number_limit;
    Recycle_adapter(int number) {
        number_limit=number;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.contents,parent,false);
        //start
        this_context=context;
        return new myViewHolder(view);



    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.bind( position);


    }

    @Override
    public int getItemCount() {
        return number_limit;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        int curpos;
        TextView contents_tv;
        LinearLayout l_layout;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            contents_tv=(TextView) itemView.findViewById(R.id.numbers);
            l_layout=(LinearLayout)itemView.findViewById(R.id.l_layout);
            l_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(this_context,"position"+String.valueOf(curpos),Toast.LENGTH_SHORT).show();
                }
            });

        }
        public void bind(int index){
            curpos=index;
            contents_tv.setText(String.valueOf(index));
        }
    }
}

