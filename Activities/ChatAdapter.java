package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import raymondhernandez.pocketuniv.Model.ChatMessage;
import raymondhernandez.pocketuniv.R;

/**
 * Created by Chachi on 4/18/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<ChatMessage> chats;
    private String userId;

    public ChatAdapter(ArrayList<ChatMessage> chatList, String userId) {
        this.chats = chatList;
        this.userId = userId;
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder{

        public TextView messageTextview;
        public TextView userTextView;
        public TextView timeTextView;
        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            messageTextview = (TextView)itemView.findViewById(R.id.textview_message);
            userTextView = (TextView)itemView.findViewById(R.id.textview_username);
            timeTextView = (TextView)itemView.findViewById(R.id.textview_chattime);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chats.get(position).getName().equals(this.userId))
            return 0;
        else
            return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v2 = inflater.inflate(R.layout.cardview_chat_left, parent, false);
        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.cardview_chat_right, parent, false);
                return new ChatMessageViewHolder(v1);
            case 1:
                return new ChatMessageViewHolder(v2);
            default:
                return new ChatMessageViewHolder(v2);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessageViewHolder chatMessageViewHolder = (ChatMessageViewHolder) holder;
        configureChatViewHolder(chatMessageViewHolder,position);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }


    public void configureChatViewHolder(final ChatMessageViewHolder holder,int pos){
        ChatMessage chatMessage = chats.get(pos);
        holder.messageTextview.setText(chatMessage.getText());
        holder.userTextView.setText(chatMessage.getName());

        Long timestamp = chatMessage.getTime();
        if(timestamp!= null) {
            Date currentDate = new Date();
            long currentTimeStamp = currentDate.getTime();

            long difference = TimeUnit.MILLISECONDS.toHours(currentTimeStamp - timestamp);

            Date date = new Date(timestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String dateString = formatter.format(date);

            formatter = new SimpleDateFormat("hh:mm a");
            String time = formatter.format(date);
            if(difference >= 24)
                holder.timeTextView.setText(dateString);
            else
                holder.timeTextView.setText(time);
        }
    }
}
