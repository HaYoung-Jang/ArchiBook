package org.techtown.archivebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RecordInputSearchListAdapter extends BaseAdapter {

    private ArrayList<BookInfoDocsData> bookInfoList = new ArrayList<>();

    String imageUrl;
    RequestOptions options;

    RecordInputSearchListAdapter() {

    }

    RecordInputSearchListAdapter(ArrayList<BookInfoDocsData> bookList) {
        bookInfoList = bookList;
    }

    @Override
    public int getCount() {
        return bookInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookInfoList.get(position);
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
            convertView = inflater.inflate(R.layout.list_item_book_regis, parent, false);
        }

        TextView tv_search_result_title = (TextView) convertView.findViewById(R.id.tv_search_result_title);
        TextView tv_search_result_isbn = (TextView) convertView.findViewById(R.id.tv_search_result_isbn);
        TextView tv_search_result_author = (TextView) convertView.findViewById(R.id.tv_search_result_author);
        TextView tv_search_result_publisher = (TextView) convertView.findViewById(R.id.tv_search_result_publisher);
        TextView tv_search_result_date_published = (TextView) convertView.findViewById(R.id.tv_search_result_date_published);
        TextView tv_search_result_price_orgin = (TextView) convertView.findViewById(R.id.tv_search_result_price_orgin);
        ImageView iv_search_result_image = (ImageView) convertView.findViewById(R.id.iv_search_result_image);
        RelativeLayout rl_search_result_info = (RelativeLayout) convertView.findViewById(R.id.rl_search_result_info);

        BookInfoDocsData bookItem = bookInfoList.get(position);
        tv_search_result_title.setText(bookItem.getBookTitle());
        tv_search_result_isbn.setText(bookItem.getIsbn());
        tv_search_result_author.setText(bookItem.getBooKAuthor());
        tv_search_result_publisher.setText(bookItem.getBookPublisher());
        tv_search_result_date_published.setText(bookItem.getBookDate());
        tv_search_result_price_orgin.setText(Integer.toString(bookItem.getBookPrice()));

        imageUrl = bookItem.getBookImage();
        options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);

        if (!imageUrl.equals("")) {
            Glide.with(context).load(imageUrl).apply(options).into(iv_search_result_image);
        }
        else {
            iv_search_result_image.setImageResource(R.drawable.img_no_book_image);
        }

        rl_search_result_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecordInfo.class);
                intent.putExtra("account_id", bookItem.getUserNow());
                intent.putExtra("title", bookItem.getBookTitle());
                intent.putExtra("isbn", bookItem.getIsbn());
                intent.putExtra("author", bookItem.getBooKAuthor());
                intent.putExtra("publisher", bookItem.getBookPublisher());
                intent.putExtra("date_published", bookItem.getBookDate());
                intent.putExtra("price_origin", bookItem.getBookPrice());
                intent.putExtra("image_url", imageUrl);
                intent.putExtra("link", bookItem.getBookLink());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addItem(String title, String isbn, String author, String publisher, String datePublished, int priceOrigin, String imageUrl, String link, String userNow) {
        BookInfoDocsData bookItem = new BookInfoDocsData();

        bookItem.setBookTitle(title);
        bookItem.setIsbn(isbn);
        bookItem.setBooKAuthor(author);
        bookItem.setBookPublisher(publisher);
        bookItem.setBookDate(datePublished);
        bookItem.setBookPrice(priceOrigin);
        bookItem.setBookImage(imageUrl);
        bookItem.setBookLink(link);
        bookItem.setUserNow(userNow);

        bookInfoList.add(bookItem);

        notifyDataSetChanged();
    }
}
