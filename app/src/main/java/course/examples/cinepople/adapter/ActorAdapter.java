//package course.examples.cinepople.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
////import com.bumptech.glide.load.resource.bitmap.CircleCrop;
////import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
////import com.firebase.ui.firestore.FirestoreRecyclerOptions;
////import com.yourpackage.R; // Thay đổi package của bạn
////import com.yourpackage.models.Actor; // Thay đổi package của bạn
//
//public class ActorAdapter extends FirestoreRecyclerAdapter<Actor, ActorAdapter.ActorViewHolder> {
//
////    public ActorAdapter(@NonNull FirestoreRecyclerOptions<Actor> options) {
////        super(options);
////    }
////
////    @Override
////    protected void onBindViewHolder(@NonNull ActorViewHolder holder, int position, @NonNull Actor model) {
////        holder.bind(model);
////    }
////
////    @NonNull
////    @Override
////    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actor, parent, false);
////        return new ActorViewHolder(view);
////    }
////
////    class ActorViewHolder extends RecyclerView.ViewHolder {
////        ImageView actorImage;
////        // TextView actorName; // Bỏ comment nếu bạn đã thêm tên diễn viên vào item_actor.xml
////
////        public ActorViewHolder(@NonNull View itemView) {
////            super(itemView);
////            actorImage = itemView.findViewById(R.id.image_actor_headshot);
////            // actorName = itemView.findViewById(R.id.text_actor_name);
////        }
////
////        public void bind(Actor actor) {
////            // Tải ảnh diễn viên và bo tròn
////            Glide.with(itemView.getContext())
////                    .load(actor.getHeadshotUrl())
////                    .transform(new CircleCrop())
////                    .placeholder(R.drawable.actor_placeholder) // Ảnh chờ
////                    .into(actorImage);
////
////            // if (actorName != null) {
////            //     actorName.setText(actor.getName());
////            // }
////        }
////    }
//}