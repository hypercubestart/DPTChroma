package com.andrewliu.gpuvideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ColorBlind> colorBlindTypes;
    private boolean isCollapsed;
    private int current;
    private RecyclerViewListener listener;
    public RecyclerViewAdapter(Context context, ArrayList<ColorBlind> colorBlindTypesList, RecyclerViewListener listener) {
        this.context = context;
        this.colorBlindTypes = colorBlindTypesList;
        this.listener = listener;
        isCollapsed = true;
        current = 0;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // infalte the item Layout
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setSelected(current == i);
        viewHolder.bind(this.colorBlindTypes.get(i));
    }

    @Override
    public int getItemCount() {
        return colorBlindTypes.size();
    }

    public void toggleVisiblity() {
        this.isCollapsed = !this.isCollapsed;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;// init the item view's
        ImageView imageView;
        MaterialCardView imageViewHolder;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image);
            imageViewHolder = itemView.findViewById(R.id.imageViewHolder);
        }

        public void bind(ColorBlind colorBlind) {
            if (current == getAdapterPosition()) {
                imageViewHolder.setStrokeColor(ContextCompat.getColor(context, R.color.selectedColor));
            } else {
                imageViewHolder.setStrokeColor(ContextCompat.getColor(context, R.color.transparent));
            }
            imageViewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ColorBlindTypes type = ColorBlindTypes.valueOf(colorBlindTypes.get(position).name);
                    listener.onItemClick(type);
                    current = position;
                    notifyDataSetChanged();
                }
            });
            name.setVisibility(!isCollapsed ? View.VISIBLE : View.GONE);
            imageViewHolder.setVisibility(!isCollapsed ? View.VISIBLE : View.GONE);
            name.setText(colorBlind.name);

            new ImageLoadTask(colorBlind.url, imageView).execute();
        }


    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL url = new URL(this.url);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}
