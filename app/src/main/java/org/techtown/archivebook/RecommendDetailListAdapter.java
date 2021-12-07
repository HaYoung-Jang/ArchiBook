package org.techtown.archivebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RecommendDetailListAdapter extends BaseAdapter {

    ArrayList<RecommendData> recommendList = new ArrayList<>();

    RecommendDetailListAdapter() {

    }

    RequestOptions options;
    String image_url;

    @Override
    public int getCount() {
        return recommendList.size();
    }

    @Override
    public Object getItem(int position) {
        return recommendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_recommend_detail, parent, false);
        }

        ImageView iv_book_image = (ImageView) convertView.findViewById(R.id.iv_book_image);
        TextView tv_book_title = (TextView) convertView.findViewById(R.id.tv_book_title);
        TextView tv_book_author = (TextView) convertView.findViewById(R.id.tv_book_author);
        TextView tv_book_publisher = (TextView) convertView.findViewById(R.id.tv_book_publisher);
        TextView tv_book_date_published = (TextView) convertView.findViewById(R.id.tv_book_date_published);
        TextView tv_book_isbn = (TextView) convertView.findViewById(R.id.tv_book_isbn);

        RecommendData recommendItem = recommendList.get(position);

        // 도서 표지 이미지 띄우기
        image_url = recommendItem.getImage_url();
        options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);

        if (image_url != null) {
            Glide.with(context).load(image_url).apply(options).into(iv_book_image);
        }
        else {
            iv_book_image.setImageResource(R.drawable.img_no_book_image);
        }

        tv_book_title.setText(recommendItem.getTitle());
        tv_book_author.setText(recommendItem.getAuthor());
        tv_book_publisher.setText(recommendItem.getPublisher());
        tv_book_date_published.setText(recommendItem.getDate_published());
        tv_book_isbn.setText(recommendItem.getIsbn());

        return convertView;
    }

    public void addItem(String isbn, String accountId, String imageUrl, String title, String author, String publisher, String date_published){
        RecommendData recommendItem = new RecommendData();

        recommendItem.setIsbn(isbn);
        recommendItem.setAccount_id(accountId);
        recommendItem.setImage_url(imageUrl);
        recommendItem.setTitle(title);
        recommendItem.setAuthor(author);
        recommendItem.setPublisher(publisher);
        recommendItem.setDate_published(date_published);

        recommendList.add(recommendItem);

        notifyDataSetChanged();
    }
}
