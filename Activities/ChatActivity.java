package raymondhernandez.pocketuniv.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import raymondhernandez.pocketuniv.Model.ChatList;
import raymondhernandez.pocketuniv.Model.ChatMessage;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class ChatActivity extends AppCompatActivity {

    private Firebase mFirebaseChatRef;
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private ArrayList<ChatMessage> messages = new ArrayList<>();

    private EditText messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final Intent intent = getIntent();
        final String chatroomID = intent.getStringExtra("PushID");
        final String studentName = intent.getStringExtra("StudentName");
        final String chatroom = intent.getStringExtra("Title");

        mFirebaseChatRef = new Firebase(Constants.FIREBASE_URL_CHATROOMS).child(chatroomID).child(Constants.FIREBASE_LOCATION_CHATS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        toolbar.setTitle(chatroom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatAdapter = new ChatAdapter(messages,studentName);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                hideSoftKeyboard();
                return false;
            }
        });

        messageEditText = (EditText)findViewById(R.id.edittext_chat);
        sendButton = (Button)findViewById(R.id.button_chat);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();

                if(!message.isEmpty()){
                    mFirebaseChatRef.push().setValue(new ChatMessage(studentName,message));
                }
                messageEditText.setText("");
            }
        });

        mFirebaseChatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!= null && dataSnapshot!= null){
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    messages.add(chatMessage);
                    recyclerView.scrollToPosition(messages.size() - 1);
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                }
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


    public void hideSoftKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}
