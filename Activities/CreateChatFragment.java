package raymondhernandez.pocketuniv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class CreateChatFragment extends DialogFragment {
    public Firebase chatListRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            chatListRef = new Firebase(Constants.FIREBASE_URL_CHATROOMS);
    }

    public static CreateChatFragment newInstance(String userEmail,String username){
        CreateChatFragment createChatFragment = new CreateChatFragment();

        Bundle bundle = new Bundle();
        bundle.putString("UserEmail",userEmail);
        bundle.putString("UserName",username);
        createChatFragment.setArguments(bundle);
        return createChatFragment;
    }

    public CreateChatFragment() {        }
        /**
         * Open the keyboard automatically when the dialog fragment is opened
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        @Nullable
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View rootView = inflater.inflate(R.layout.create_chat, null);

            final TextInputEditText chatRoomName = (TextInputEditText) rootView.findViewById(R.id.chatroom_name);
            final String StudentName = getArguments().getString("UserName");
            final TextInputEditText chatRoomCourse = (TextInputEditText) rootView.findViewById(R.id.chatroom_course);

            builder.setView(rootView)
                /* Add action buttons */
                    .setPositiveButton(R.string.create_chatroom, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            addChatRoom(chatRoomName.getText().toString(),chatRoomCourse.getText().toString(),StudentName);
                        }
                    });
            return builder.create();
        }

    public void addChatRoom(final String title,final String course,final String studentName){
        final HashMap<String,Object> ChatListMap = new HashMap<>();
        ChatListMap.put("title",title);
        ChatListMap.put("createdBy",studentName);
        ChatListMap.put("course",course);

        chatListRef.push().setValue(ChatListMap);
    }
}
