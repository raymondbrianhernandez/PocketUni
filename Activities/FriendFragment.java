package raymondhernandez.pocketuniv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import raymondhernandez.pocketuniv.Model.Professor;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;


public class FriendFragment extends DialogFragment {

    public Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL_USERS);
    public Firebase friendsRef,chatListRef;
    public TextInputEditText emailEditText;
    public String userName, user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsRef = new Firebase(Constants.FIREBASE_URL_FRIENDS);
        chatListRef = new Firebase(Constants.FIREBASE_URL_CHATROOMS);
        user = getArguments().getString("Email");
        userName = getArguments().getString("Name");
    }

    public static FriendFragment newInstance(String userEmail,String name) {
        FriendFragment friendFragment = new FriendFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Email", userEmail);
        bundle.putString("Name",name);
        friendFragment.setArguments(bundle);
        return friendFragment;
    }

    public FriendFragment() {
    }

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
        View rootView = inflater.inflate(R.layout.add_friend, null);

        emailEditText = (TextInputEditText) rootView.findViewById(R.id.friend_email);

        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.create_chatroom, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (emailEditText.getText().toString() == user.replace(",",".")) {
                            emailEditText.setError("You cannot add yourself as a friend");
                            return;
                        }
                        else
                            addFriend(emailEditText.getText().toString());
                    }
                });
        return builder.create();
    }

    public void addFriend(final String friendEmail) {

        Query queryRef = firebaseRef.orderByChild("email").equalTo(friendEmail.replace(".", ","));

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<String, Object> friendMap,frienddataMap;
                HashMap<String, Object> friend = (HashMap<String, Object>) dataSnapshot.getValue();

                final HashMap<String,Object> ChatListMap = new HashMap<>();
                ChatListMap.put("title","Personal");
                ChatListMap.put("createdBy",userName);
                ChatListMap.put("course","Personal");

                Firebase ChatRef = chatListRef.push();
                String chatId = ChatRef.getKey();
                ChatRef.setValue(ChatListMap);

                /* Add relationship between user and friend */
                friendMap = new HashMap<String, Object>();
                friendMap.put("Name", friend.get("firstName").toString() + " " + friend.get("lastName").toString());
                friendMap.put("ChatId",chatId);

                frienddataMap = new HashMap<String, Object>();
                frienddataMap.put(dataSnapshot.getKey(), friendMap);

                friendsRef.child(getArguments().getString("Email")).updateChildren(frienddataMap);

                /* Add relationship between friend and user*/
                friendMap = new HashMap<String, Object>();
                friendMap.put("Name",userName);
                friendMap.put("ChatId",chatId);

                frienddataMap = new HashMap<String, Object>();
                frienddataMap.put(user,friendMap);

                friendsRef.child(dataSnapshot.getKey()).updateChildren(frienddataMap);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
