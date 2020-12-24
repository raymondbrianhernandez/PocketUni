package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;

import raymondhernandez.pocketuniv.Model.ChatList;
import raymondhernandez.pocketuniv.Model.Friend;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;
import raymondhernandez.pocketuniv.Utils.DividerItemDecoration;

public class FriendsActivity extends BaseActivity {

    private static final String LOG_TAG = ChatListActivity.class.getSimpleName();

    private Firebase mFirebaseRef;
    private Firebase mFirebaseFriendsRef = new Firebase(Constants.FIREBASE_URL_FRIENDS);
    private FriendAdapter friendAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Friend> friends = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_friends, null,false);
        drawer.addView(contentView, 0);

        final String studentName = studentFullName;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Friends");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_friends);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        friendAdapter = new FriendAdapter(this,studentName,friends);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(friendAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Friend friend = friends.get(position);

                Intent intent = new Intent(FriendsActivity.this,ChatActivity.class);
                intent.putExtra("Title",friend.getName());
                intent.putExtra("StudentName",studentFullName);
                intent.putExtra("PushID",friend.getChatId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_friends);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FriendFragment friendFragment = FriendFragment.newInstance(mEncodedEmail,studentFullName);
                friendFragment.show(fragmentManager,"Friend Fragment");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getFriends();
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
                final ArrayList<Friend> filteredItems = new ArrayList<>();
                for (int i = 0; i < friends.size(); i++) {
                    if (friends.get(i).getName().toLowerCase().contains(query)) {
                        filteredItems.add(friends.get(i));
                    }
                }

                FriendAdapter filteredChatListAdapter = new FriendAdapter(FriendsActivity.this,studentFullName,filteredItems);
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

    public void getFriends(){

        if(mFirebaseFriendsRef!=null) {
            mFirebaseFriendsRef.child(mEncodedEmail).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    HashMap<String, String> friendMap = (HashMap<String, String>) dataSnapshot.getValue();

                    String name = friendMap.get("Name").toString();
                    String chatId = friendMap.get("ChatId").toString();
                    String email = dataSnapshot.getKey();

                    Friend friend = new Friend(name, email.replace(",", "."), chatId);

                    if (friends.contains(friend)) {
                        Toast.makeText(getApplicationContext(), "you are already friends!", Toast.LENGTH_LONG).show();
                    } else
                        friends.add(friend);

                    recyclerView.scrollToPosition(friends.size() - 1);
                    friendAdapter.notifyItemInserted(friends.size() - 1);
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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FriendsActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FriendsActivity.ClickListener clickListener) {
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
