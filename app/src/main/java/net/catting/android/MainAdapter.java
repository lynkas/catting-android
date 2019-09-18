package net.catting.android;


import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.squareup.picasso.Picasso;

import net.catting.android.data.structs.Post;
import net.catting.android.data.structs.PostBrief;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private ArrayList<PostBrief> posts;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends ViewHolder {
        // each data item is just a string in this case
        private VideoView   videoView;
        private ImageView   imageView;
        private TextView    textViewTitle;
        private TextView    userView;
        private TextView    frontView;
        private TextView    behindView;
        private ImageView   profile;
        private ProgressBar progressBar;

        public MyViewHolder(View v) {
            super(v);
            this.imageView=v.findViewById(R.id.main_card_image);
            this.videoView=v.findViewById(R.id.main_card_video);
            this.textViewTitle=v.findViewById(R.id.main_card_title);
            this.userView=v.findViewById(R.id.nickname);
            this.frontView=v.findViewById(R.id.front);
            this.behindView=v.findViewById(R.id.behind);
            this.profile=v.findViewById(R.id.profile);
            this.progressBar=v.findViewById(R.id.main_card_progress);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainAdapter() {
        this.posts = new ArrayList<>();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_post_card, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        findViewById(R.id.main_card_title)
        if (position==posts.size()){

            holder.videoView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.textViewTitle.setVisibility(View.GONE);
            holder.frontView.setVisibility(View.GONE);
            holder.behindView.setVisibility(View.GONE);
            holder.profile.setVisibility(View.GONE);
            holder.userView.setVisibility(View.GONE);

            holder.progressBar.setVisibility(View.VISIBLE);

            return;
        }

        binder(posts.get(position),holder);

    }

    private void binder(PostBrief post, final MyViewHolder holder){
        holder.progressBar.setVisibility(View.GONE);

        if (post.media!=null&&post.media.isImage){
            notNullThenSetImageElsePlaceholder(holder.imageView,R.drawable.loading,post.media.imageUrl);
        }else {
            holder.imageView.setVisibility(View.GONE);
        }

        notNullThenSetImageElsePlaceholder(holder.profile,R.drawable.hairyfooddoe,post.postBy.user.profileUrl);
        notNullThenSetVisibleAndFillDataElseGone(holder.textViewTitle, Html.fromHtml(post.preview));
        notNullThenSetVisibleAndFillDataElseGone(holder.userView,post.postBy.user.nickName);
        notNullThenSetVisibleAndFillDataElseGone(holder.frontView,post.postBy.user.front);
        notNullThenSetVisibleAndFillDataElseGone(holder.behindView,post.postBy.user.behind);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size()+1;
    }

    public void addPosts(List<PostBrief> data) {
        this.posts.addAll(data);
        notifyDataSetChanged();
    }
    public void addPosts(List<PostBrief> data,boolean front) {
        if (front){
            data.addAll(this.posts);
            this.posts=new ArrayList<>(data);
            notifyDataSetChanged();
        }else {
            addPosts(data);
        }

    }

    private void notNullThenSetVisibleAndFillDataElseGone(TextView view,String data){
        if (data!=null&&!data.trim().equals("")){
            view.setVisibility(View.VISIBLE);
            view.setText(data);
        }else {
            view.setVisibility(View.GONE);
        }
    }
    private void notNullThenSetVisibleAndFillDataElseGone(TextView view, Spanned data){
        if (data!=null&&data.length()!=0){
            view.setVisibility(View.VISIBLE);
            view.setText(data);
        }else {
            view.setVisibility(View.GONE);
        }
    }

    private void notNullThenSetImageElsePlaceholder(ImageView view,int resId, String url){
        if(url==null){
            view.setVisibility(View.GONE);
            Log.d(TAG, "notNullThenSetImageElsePlaceholder: 不显示");
        }else {
            view.setVisibility(View.VISIBLE);
            Picasso.get().load(url).placeholder(resId).into(view);
            Log.d(TAG, "notNullThenSetImageElsePlaceholder: 显示");
        }
    }



}
