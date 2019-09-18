//package net.catting.android;
//
//import android.text.Html;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.VideoView;
//
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView.ViewHolder;
//
//import com.squareup.picasso.Picasso;
//
//import net.catting.android.data.structs.Post;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
//
//    private ArrayList<Post> posts;
//    // Provide a reference to the views for each data item
//    // Complex data items may need more than one view per item, and
//    // you provide access to all the views for a data item in a view holder
//    public static class MyViewHolder extends ViewHolder {
//        // each data item is just a string in this case
//        private VideoView videoView;
//        private ImageView imageView;
//        private TextView textViewTitle;
//        private TextView userView;
//        private TextView frontView;
//        private TextView behindView;
//        private ImageView profile;
//        private ViewTreeObserver.OnDrawListener oDL;
//
//        public MyViewHolder(View v) {
//            super(v);
//            this.imageView=v.findViewById(R.id.main_card_image);
//            this.videoView=v.findViewById(R.id.main_card_video);
//            this.textViewTitle=v.findViewById(R.id.main_card_title);
//            this.userView=v.findViewById(R.id.nickname);
//            this.frontView=v.findViewById(R.id.front);
//            this.behindView=v.findViewById(R.id.behind);
//            this.profile=v.findViewById(R.id.profile);
//        }
//
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public MainAdapter() {
//        this.posts = new ArrayList<>();
//
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public MainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
//                                                       int viewType) {
//        // create a new view
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.main_post_card, parent, false);
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
////        findViewById(R.id.main_card_title)
//        binder(posts.get(position),holder);
//    }
//
//    private void binder(Post post, final MyViewHolder holder){
//        holder.textViewTitle.setText(Html.fromHtml(post.content));
//        final Post fPost = post;
//        holder.imageView.getViewTreeObserver().removeOnDrawListener(holder.oDL);
//        holder.oDL = new ViewTreeObserver.OnDrawListener() {
//            @Override
//            public void onDraw() {
//                android.util.Log.d("PAOLE", "PAOLE");
//
//                Picasso.get().load(fPost.media.imageUrl)
//                        .resize(holder.imageView.getWidth(),0)
//                        .into(holder.imageView);
//
//            }
//        };
//
//        /*
//         * fit image, full width and auto height!!!!!!!!!
//         */
//        if (post.media!=null && post.media.isImage){
//            holder.imageView.setVisibility(View.VISIBLE);
//            holder.imageView.getViewTreeObserver()
//                    .addOnDrawListener(holder.oDL);
//        }else {
//            holder.imageView.setVisibility(View.GONE);
//        }
//        if (post.postBy.user.profileUrl!=null)Picasso.get().load(fPost.postBy.user.profileUrl).into(holder.profile);
//        if (post.postBy.user.nickName!=null)holder.userView.setText(post.postBy.user.nickName);
//        if (post.postBy.user.front!=null){
//            holder.frontView.setText(post.postBy.user.front);
//            if (post.postBy.user.front.equals(""))holder.frontView.setVisibility(View.GONE);
//        }
//
//        if (post.postBy.user.behind!=null){
//            holder.behindView.setText(post.postBy.user.behind);
//            if (post.postBy.user.behind.equals(""))holder.behindView.setVisibility(View.GONE);
//        }
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return posts.size();
//    }
//
//    public void addPosts(List<Post> data) {
//        this.posts.addAll(data);
//        notifyDataSetChanged();
//    }
//}
