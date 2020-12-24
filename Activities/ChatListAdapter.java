package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import raymondhernandez.pocketuniv.Model.ChatList;
import raymondhernandez.pocketuniv.Model.ChatMessage;
import raymondhernandez.pocketuniv.R;

/**
 * Created by Chachi on 4/19/2016.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<ChatList> chatLists;
    Context context;
    String studentName;

    public ChatListAdapter(Context mContext,String studentName, ArrayList<ChatList> chatLists) {
        this.chatLists = chatLists;
        this.studentName = studentName;
        this.context = mContext;
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder{

        public TextView titleTextview;
        public TextView creatorTextView;
        public ChatListViewHolder(View itemView) {
            super(itemView);
            titleTextview = (TextView)itemView.findViewById(R.id.textview_chatlist_topic);
            creatorTextView = (TextView)itemView.findViewById(R.id.textview_createdBy);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.chat_list, parent, false);
        return new ChatListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatListViewHolder chatListViewHolder = (ChatListViewHolder) holder;
        configureChatListViewHolder(chatListViewHolder,position);
    }


    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void configureChatListViewHolder(final ChatListViewHolder holder,int pos){
        final ChatList chatList = chatLists.get(pos);
        holder.titleTextview.setText(chatList.getTitle());
        holder.creatorTextView.setText(context.getString(R.string.chat_list_createdby,chatList.getCreatedBy()));
    }
}
