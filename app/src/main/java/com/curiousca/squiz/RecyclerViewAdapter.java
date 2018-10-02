package com.curiousca.squiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sanjay on 1/1/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private QuizClickInterface mClickListener;

    /* public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images) {
         mImageNames = imageNames;
         mImages = images;
         mContext = context;
     }
 */
    public RecyclerViewAdapter(QuizClickInterface mClickListener, ArrayList<String> imageNames) {
        this.mImageNames = imageNames;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

      /*  Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);
*/
        holder.tvImageName.setText(mImageNames.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onRecyclerItemClick(holder.getAdapterPosition(),mImageNames.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        //        CircleImageView image;
        TextView tvImageName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
//            image = itemView.findViewById(R.id.image);
            tvImageName = itemView.findViewById(R.id.tv_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}















