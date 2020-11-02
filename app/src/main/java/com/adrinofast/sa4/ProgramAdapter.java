package com.adrinofast.sa4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;


public class ProgramAdapter extends  RecyclerView.Adapter<ProgramAdapter.ViewHolder>  {
    Context context;
    private OnItemClickListener mListener;
     List<Program> mPrograms;



    public ProgramAdapter(List<Program> programs ) {

        for(Program p :programs)
        {
             Log.i("in the adapter", p.toString());
        }
        mPrograms = programs;

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListe(final OnItemClickListener listener) {
        mListener = listener;
    }




   public  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView programDeptTextView;
        public TextView gradtextView;
        ConstraintLayout relay_lay;
        MaterialCardView matcard;


        public Button IdButton;

  public ViewHolder(View itemView,final OnItemClickListener listener) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.college_name);
            gradtextView = (TextView)itemView.findViewById(R.id.message_text);
            programDeptTextView = (TextView)itemView.findViewById(R.id.program_dept);
      matcard = itemView.findViewById(R.id.con_lay_eachitem);


      matcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Log.i("item clicked","plese chekc");
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }


    }

    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View colleegView = inflater.inflate(R.layout.rv_each_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(colleegView,mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramAdapter.ViewHolder holder, final int position) {



        final Program program = mPrograms.get(position);



        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(program.getEngineering());

        TextView textView2 = holder.programDeptTextView;
        textView2.setText(program.getCollegeName());

        TextView textview3 = holder.gradtextView;
        textview3.setText(program.getLevel());



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(1);
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mPrograms.size();
    }


}

