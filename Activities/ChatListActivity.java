package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import raymondhernandez.pocketuniv.Model.ChatList;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;
import raymondhernandez.pocketuniv.Utils.DividerItemDecoration;

public class ChatListActivity extends BaseActivity {

    private static final String LOG_TAG = ChatListActivity.class.getSimpleName();

    private Firebase mFirebaseRef, mFirebaseChatListRef;
    private ChatListAdapter chatListAdapter;
    private RecyclerView recyclerView;
    private ArrayList<ChatList> chatrooms = new ArrayList<>();

    public String studentNameExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_chat_list, null, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chat rooms");
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        final String studentName = intent.getStringExtra(Constants.INTENT_EXTRA_STUDENT_NAME);
        studentNameExtra = studentName;
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mFirebaseChatListRef = mFirebaseRef.child(Constants.FIREBASE_LOCATION_CHATROOMS);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_chatlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatListAdapter = new ChatListAdapter(this, studentName, chatrooms);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(chatListAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ChatList chatList = chatrooms.get(position);
                Context mContext = getApplicationContext();
                Intent intent = new Intent(mContext,ChatActivity.class);

                intent.putExtra("Title",chatList.getTitle());
                intent.putExtra("StudentName",studentNameExtra);
                intent.putExtra("PushID",chatList.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_chatlist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                CreateChatFragment createChatFragment = CreateChatFragment.newInstance(mEncodedEmail, studentNameExtra);
                createChatFragment.show(fragmentManager, "Chat Fragment");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getChatLists();
    }

    public void getChatLists() {
        mFirebaseChatListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,String s) {
                HashMap<String, String> chatroomMap = (HashMap<String, String>) dataSnapshot.getValue();
                String title = chatroomMap.get("title");
                String createdBy = chatroomMap.get("createdBy");
                String course = chatroomMap.get("course");
                String id = dataSnapshot.getKey();


                if(title!=null && createdBy!= null && !title.equals("Personal")) {
                    ChatList chatList = new ChatList(title,course,createdBy,id);
                    if (chatrooms.size() == 0) {
                        chatrooms.add(chatList);
                    } else if (!chatrooms.contains(chatList)) {
                        chatrooms.add(chatList);
                    }
                }
                chatListAdapter.notifyItemInserted(chatrooms.size() - 1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {

                Log.d(LOG_TAG, query);
                query = query.toLowerCase();
                final ArrayList<ChatList> filteredItems = new ArrayList<>();
                for (int i = 0; i < chatrooms.size(); i++) {
                    if (chatrooms.get(i).getTitle().toLowerCase().contains(query)) {
                        filteredItems.add(chatrooms.get(i));
                    }
                }

                ChatListAdapter filteredChatListAdapter = new ChatListAdapter(ChatListActivity.this, studentNameExtra, filteredItems);
                recyclerView.setAdapter(filteredChatListAdapter);
                filteredChatListAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }
        });
        return true;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ChatListActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ChatListActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}