package net.catting.android;


import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
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

    ArrayList<PostBrief> posts;
    private boolean     lastErr=false;
    private boolean     loading=false;
    private iBus bus;
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
        private Button      reload;

        MyViewHolder(View v) {
            super(v);
            this.imageView=v.findViewById(R.id.main_card_image);
            this.videoView=v.findViewById(R.id.main_card_video);
            this.textViewTitle=v.findViewById(R.id.main_card_title);
            this.userView=v.findViewById(R.id.nickname);
            this.frontView=v.findViewById(R.id.front);
            this.behindView=v.findViewById(R.id.behind);
            this.profile=v.findViewById(R.id.profile);
            this.progressBar=v.findViewById(R.id.main_card_progress);
            this.reload=v.findViewById(R.id.main_card_reload_button);
        }

    }

    void setLoading(){
        loading=true;
        notifyDataSetChanged();

    }

    void unsetLoading(){
        loading=false;
        notifyDataSetChanged();

    }

    void setLastErr(){
        this.lastErr=true;
        notifyDataSetChanged();

    }

    void unsetLastErr(){
        this.lastErr=false;
        notifyDataSetChanged();

    }

    boolean getLoadingState(){
        return loading;
    }

    boolean getLastErrState(){
        return lastErr;

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    MainAdapter(iBus bus) {
        posts = new ArrayList<>();
        this.bus=bus;

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
        holder.setIsRecyclable(false);
        if (position==posts.size()){


            holder.videoView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.textViewTitle.setVisibility(View.GONE);
            holder.frontView.setVisibility(View.GONE);
            holder.behindView.setVisibility(View.GONE);
            holder.profile.setVisibility(View.GONE);
            holder.userView.setVisibility(View.GONE);

            if (lastErr){
                holder.reload.setVisibility(View.VISIBLE);
                holder.reload.setOnClickListener((view)->bus.onClickReloadButton(view));
            }else {
                holder.progressBar.setVisibility(View.VISIBLE);
            }
            return;
        }

        binder(posts.get(position),holder);

    }

    private void binder(PostBrief post, final MyViewHolder holder){
        holder.progressBar.setVisibility(View.GONE);

        if (post.media!=null&&post.media.isImage){
            notNullThenSetImageElsePlaceholder(holder.imageView,R.drawable.loading,post.media.imageUrl,1024,0);
        }else {
            holder.imageView.setVisibility(View.GONE);
        }

        notNullThenSetImageElsePlaceholder(holder.profile,R.drawable.hairyfooddoe,post.postBy.user.profileUrl);
        notNullThenSetVisibleAndFillDataElseGone(holder.textViewTitle, Html.fromHtml(post.preview,Html.FROM_HTML_MODE_LEGACY));
        notNullThenSetVisibleAndFillDataElseGone(holder.userView,post.postBy.user.nickName);
        notNullThenSetVisibleAndFillDataElseGone(holder.frontView,post.postBy.user.front);
        notNullThenSetVisibleAndFillDataElseGone(holder.behindView,post.postBy.user.behind);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() { return posts.size()+1; }

    void addPosts(List<PostBrief> data) {
        posts.addAll(data);
        notifyDataSetChanged();
    }
    void addPosts(List<PostBrief> data,boolean front) {
        if (front){
            data.addAll(posts);
            posts=new ArrayList<>(data);
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
        }else {
            view.setVisibility(View.VISIBLE);
            Picasso.get().load(url).placeholder(resId).into(view);
        }
    }
    private void notNullThenSetImageElsePlaceholder(ImageView view,int resId, String url,int width,int height){
        if(url==null){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
            Picasso.get().load(url).placeholder(resId).resize(width,height).onlyScaleDown().into(view);
        }
    }

    void setPosts(ArrayList<PostBrief> posts){
        this.posts=posts;
        notifyDataSetChanged();
    }




}

interface iBus{
    void onClickReloadButton(View v);
}


