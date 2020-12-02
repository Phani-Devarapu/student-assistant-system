package com.adrinofast.sa4;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class WishListAdapter extends  RecyclerView.Adapter<WishListAdapter.ViewHolder>{

    public static final String TAG = "WishListAdapter";

    Context context;
    private WishListAdapter.OnItemClickListener mListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    List<Program> mPrograms;

    public WishListAdapter(List<Program> programs){
        for(Program p :programs)
        {
            Log.i("in the wishlist adapter", p.toString());
        }
        mPrograms = programs;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListe(final WishListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView programDeptTextView;
        public TextView gradtextView;
        public ImageView college_image;


        ConstraintLayout relay_lay;
        MaterialCardView matcard;


        public Button IdButton;

        public ViewHolder(View itemView,final WishListAdapter.OnItemClickListener listener) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.college_name);
            gradtextView = (TextView)itemView.findViewById(R.id.message_text);
            programDeptTextView = (TextView)itemView.findViewById(R.id.program_dept);
            college_image = itemView.findViewById(R.id.college_image);
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
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View colleegView = inflater.inflate(R.layout.rv_each_row, parent, false);



        // Return a new holder instance
        WishListAdapter.ViewHolder viewHolder = new WishListAdapter.ViewHolder(colleegView,mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WishListAdapter.ViewHolder holder, int position) {

        final Program program = mPrograms.get(position);



        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(program.getEngineering());

        TextView textView2 = holder.programDeptTextView;
        textView2.setText(program.getCollegeName()+ " "+program.getTypeCollegeUni());

        TextView textview3 = holder.gradtextView;
        textview3.setText(program.getLevel());

        final ImageView image_college = holder.college_image;

        Log.i("the program deatils are", program.toString());

        StorageReference storageRef = storage.getReferenceFromUrl(program.getImageURL());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(220, 220).transform(new CropCircleTransformation()).into(holder.college_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPrograms.size();
    }
}
