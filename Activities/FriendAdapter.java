package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

import raymondhernandez.pocketuniv.Model.ChatList;
import raymondhernandez.pocketuniv.Model.ChatMessage;
import raymondhernandez.pocketuniv.Model.Friend;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

/**
 * Created by Chachi on 4/19/2016.
 */
public class FriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Friend> friends;
    Context context;
    String StudentName;

    public FriendAdapter(Context mContext,String studentName,ArrayList<Friend> friends ) {
        this.friends = friends;
        this.context = mContext;
        this.StudentName = studentName;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder{

        public TextView NameTextview;
        public TextView emailTextView;
        public FriendViewHolder(View itemView) {
            super(itemView);
            NameTextview = (TextView)itemView.findViewById(R.id.textview_friend_name);
            emailTextView = (TextView)itemView.findViewById(R.id.textview_friend_email);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.friend_list, parent, false);
        return new FriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FriendViewHolder friendViewHolder = (FriendViewHolder) holder;
        configureChatListViewHolder(friendViewHolder,position);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void configureChatListViewHolder(final FriendViewHolder holder,int pos){
        final Friend friend = friends.get(pos);
        holder.NameTextview.setText(friend.getName());
        holder.emailTextView.setText(friend.getEmail());
    }
}
