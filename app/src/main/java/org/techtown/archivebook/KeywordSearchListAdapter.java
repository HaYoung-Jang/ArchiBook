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

public class KeywordSearchListAdapter extends BaseAdapter {

    private ArrayList<KeywordData> keywordList = new ArrayList<>();

    KeywordSearchListAdapter() {

    }

    RequestOptions options;
    String image_url;

    @Override
    public int getCount() {
        return keywordList.size();
    }

    @Override
    public Object getItem(int position) {
        return keywordList.get(position);
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
            convertView = inflater.inflate(R.layout.list_item_keyword_search, parent, false);
        }

        ImageView iv_book_image = (ImageView) convertView.findViewById(R.id.iv_book_image);
        TextView tv_book_title = (TextView) convertView.findViewById(R.id.tv_book_title);
        TextView tv_book_author = (TextView) convertView.findViewById(R.id.tv_book_author);
        TextView tv_book_publisher = (TextView) convertView.findViewById(R.id.tv_book_publisher);
        TextView tv_book_date_published = (TextView) convertView.findViewById(R.id.tv_book_date_published);
        TextView tv_book_isbn = (TextView) convertView.findViewById(R.id.tv_book_isbn);

        KeywordData keywordItem = keywordList.get(position);

        // 도서 표지 이미지 띄우기
        image_url = keywordItem.getImage_url();
        options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);

        if (image_url != null) {
            Glide.with(context).load(image_url).apply(options).into(iv_book_image);
        }
        else {
            iv_book_image.setImageResource(R.drawable.img_no_book_image);
        }

        tv_book_title.setText(keywordItem.getTitle());
        tv_book_author.setText(keywordItem.getAuthor());
        tv_book_publisher.setText(keywordItem.getPublisher());
        tv_book_date_published.setText(keywordItem.getDate_published());
        tv_book_isbn.setText(keywordItem.getIsbn());

        return convertView;
    }

    public void addItem(String keyword, String isbn, String imageUrl, String title, String author, String publisher, String date_published) {
        KeywordData keywordItem = new KeywordData();

        keywordItem.setKeyword(keyword);
        keywordItem.setIsbn(isbn);
        keywordItem.setImage_url(imageUrl);
        keywordItem.setTitle(title);
        keywordItem.setAuthor(author);
        keywordItem.setPublisher(publisher);
        keywordItem.setDate_published(date_published);

        keywordList.add(keywordItem);

        notifyDataSetChanged();
    }

    public void resetItem() {
        keywordList.clear();
    }
}
