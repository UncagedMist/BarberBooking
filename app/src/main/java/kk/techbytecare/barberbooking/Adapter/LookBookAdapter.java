package kk.techbytecare.barberbooking.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kk.techbytecare.barberbooking.Model.Banner;
import kk.techbytecare.barberbooking.R;

public class LookBookAdapter extends RecyclerView.Adapter<LookBookAdapter.MyViewHolder> {

    Context context;
    List<Banner> lookBook;

    public LookBookAdapter(Context context, List<Banner> lookBook) {
        this.context = context;
        this.lookBook = lookBook;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_look_book,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Picasso.get()
                .load(lookBook.get(i).getImage())
                .into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return lookBook.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_look_book);
        }
    }
}
