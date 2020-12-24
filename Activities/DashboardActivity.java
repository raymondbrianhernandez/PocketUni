package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import raymondhernandez.pocketuniv.Model.Assignment;
import raymondhernandez.pocketuniv.Model.Book;
import raymondhernandez.pocketuniv.Model.ChatList;
import raymondhernandez.pocketuniv.Model.Course;
import raymondhernandez.pocketuniv.Model.Professor;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class DashboardActivity extends BaseActivity {


    public static final String TAG = DashboardActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<Object> items = new ArrayList<>();
    private DashboardItemAdapter itemAdapter;
    public Firebase mFirebase,mAssignRef,mProfRef,mBooksRef,mChatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_dashboard, null,false);
        drawer.addView(contentView, 0);

        mFirebase = new Firebase(Constants.FIREBASE_URL);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.dashboard_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(items.get(position) instanceof Professor){
                    return 2;
                }else
                    return 2;
            }
        });
        itemAdapter = new DashboardItemAdapter(DashboardActivity.this, items);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(itemAdapter);

        professorData();
        bookData();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

//    public void getItems(){
//        mAssignRef = mFirebase.child(Constants.FIREBASE_LOCATION_ASSIGNMENTS);
//        mChatRef = mFirebase.child(Constants.FIREBASE_LOCATION_CHATROOMS);
//        mBooksRef = mFirebase.child(Constants.FIREBASE_LOCATION_BOOKS);
//        mAssignRef.child(mEncodedEmail).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Assignment assignment = dataSnapshot.getValue(Assignment.class);
//                items.add(assignment);
//                final String course = assignment.getCourse();
//                final String name = assignment.getName();
//                mChatRef.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot,String s) {
//                        HashMap<String, String> chatroomMap = (HashMap<String, String>) dataSnapshot.getValue();
//                        String title = chatroomMap.get("title");
//                        String createdBy = chatroomMap.get("createdBy");
//                        String course1 = chatroomMap.get("course");
//                        String id = dataSnapshot.getKey();
//
//                        if(course1== course) {
//                            ChatList chatList = new ChatList(title,course,createdBy,id);
//                            if (!items.contains(chatList)) {
//                                items.add(chatList);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//                mBooksRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        HashMap<String, Object> books = (HashMap<String, Object>) dataSnapshot.getValue();
//                        for (Object b : books.values()) {
//                            HashMap<String, Object> bookMap = (HashMap<String, Object>) b;
//                            String bookTitle = (String) bookMap.remove("Title");
//                            if(name.contains(bookTitle)){
//                                String author = (String) (bookMap.remove("Author"));
//                                String subject = (String) (bookMap.remove("Major"));
//
//                                Book book = new Book(bookTitle, author, subject, 3.66f);
//                                items.add(book);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }

    public void professorData() {
        final Firebase profRef = new Firebase(Constants.FIREBASE_URL_PROFESSORS);

                    String firstName = "George";
                    String lastName = "Wang";
                    String name = firstName + " " + lastName;
                    Long phoneNumber = 8186773881L;
                    String dept = "Computer Science";
                    String courses = "COMP 380,COMP 583";
                    String profEmail = "twang@csun,edu";
                    String imageUrl = "http://www.chrislong365.com/professors/Wang_George.jpg";
                    Professor professor = new Professor(name, "CSUN", profEmail.replace(",", "."), dept, phoneNumber, imageUrl, 3f, new String[]{"COMP380"}, "");
                    items.add(professor);
    }

    public void bookData() {

                    String bookTitle = "Object Oriented Software Engineering";

                        String author = "Stephen R. Schach";
                        String subject = "Software Engineering";

                        Book book = new Book(bookTitle, author, subject, 3.66f);
                        items.add(book);
        String bookTitle2 = "Software Development Life Cycle";

        String author2 = "Kevin Reobuck";
        String subject2 = "Software Engineering";

        Book book2 = new Book(bookTitle2, author2, subject2, 3.66f);
        items.add(book2);


    }
}
