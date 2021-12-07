package org.techtown.archivebook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RecommendListAdapter extends RecyclerView.Adapter<Holder> {

    ArrayList<RecommendData> recommendList;

    RecommendListAdapter (ArrayList<RecommendData> recommendList) {
        this.recommendList = recommendList;
    }

    RequestOptions options;
    String image_url;

    Context context;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_recommend_book, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // 도서 표지 이미지 띄우기
        image_url = recommendList.get(position).getImage_url();
        options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);

        if (image_url != null) {
            Glide.with(context).load(image_url).apply(options).into(holder.iv_book_recommend_image);
        }
        else {
            holder.iv_book_recommend_image.setImageResource(R.drawable.img_no_book_image);
        }
    }

    @Override
    public int getItemCount() {
        return recommendList.size();
    }
}

class Holder extends RecyclerView.ViewHolder {
    LinearLayout ll_book_recommend_list;
    ImageView iv_book_recommend_image;

    public Holder(@NonNull View itemView) {
        super(itemView);
        ll_book_recommend_list = itemView.findViewById(R.id.ll_book_recommend_list);
        iv_book_recommend_image = itemView.findViewById(R.id.iv_book_recommend_image);
    }

}
