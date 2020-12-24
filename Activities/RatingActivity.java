package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import raymondhernandez.pocketuniv.Model.Book;
import raymondhernandez.pocketuniv.Model.Course;
import raymondhernandez.pocketuniv.Model.Professor;
import raymondhernandez.pocketuniv.Model.Student;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class RatingActivity extends BaseActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<Object> items = new ArrayList<>();
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_rating, null, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ratings Home");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rating_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        itemAdapter = new ItemAdapter(RatingActivity.this, items);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(itemAdapter);

        professorData();
        bookData();
        courseData();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuItem menuItem = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {

                Log.d(TAG, query);
                query = query.toLowerCase();
                final ArrayList<Object> filteredItems = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getClass() == Professor.class) {
                        Professor professor = (Professor) items.get(i);
                        if (professor.getName().toLowerCase().contains(query)) {
                            filteredItems.add(professor);
                        }
                    }
                    if (items.get(i).getClass() == Book.class) {
                        Book book = (Book) items.get(i);
                        if (book.getTitle().toLowerCase().contains(query)) {
                            filteredItems.add(book);
                        }
                    }
                    if (items.get(i).getClass() == Course.class) {
                        Course course = (Course) items.get(i);
                        Log.d(TAG, "onQueryTextChange: "+ course.getCourseName() + "/" + course.getCourseCode());
                        if (course.getCourseName().toLowerCase().contains(query)
                                || course.getCourseCode().toLowerCase().contains(query))
                            filteredItems.add(course);
                    }
                }

                ItemAdapter filteredItemAdapter = new ItemAdapter(RatingActivity.this, filteredItems);
                recyclerView.setAdapter(filteredItemAdapter);
                itemAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void professorData() {
        final Firebase profRef = new Firebase(Constants.FIREBASE_URL_PROFESSORS);
        profRef.addChildEventListener(new ChildEventListener() {
            String[] courseArray = {};

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<String, Object> profMap = (HashMap<String, Object>) dataSnapshot.getValue();
                List<String> courselist = null;

                String profEmail = (String) profMap.remove("email");
                if (!items.contains(profEmail) && profEmail != null) {
                    String firstName = (String) (profMap.remove("firstName"));
                    String lastName = (String) (profMap.remove("lastName"));
                    String name = firstName + " " + lastName;
                    Long phoneNumber = (Long) profMap.remove("phone");
                    String dept = (String) profMap.remove("department");
                    String courses = (String) profMap.remove("courses");
                    if (courses != null) {
                        courselist = Arrays.asList(courses.split(","));
                        courseArray = courselist.toArray(new String[0]);
                    }
                    String imageUrl = (String) profMap.remove("imageURL");
                    String bio = (String) profMap.remove("bio");

                    Professor professor = new Professor(name, "CSUN", profEmail.replace(",", "."), dept, phoneNumber, imageUrl, 3f, courseArray, bio);
                    items.add(professor);

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

        itemAdapter.notifyItemInserted(items.size() - 1);
    }

    public void bookData() {
        final Firebase bookRef = new Firebase(Constants.FIREBASE_URL_BOOKS);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> books = (HashMap<String, Object>) dataSnapshot.getValue();
                for (Object b : books.values()) {
                    HashMap<String, Object> bookMap = (HashMap<String, Object>) b;
                    String bookTitle = (String) bookMap.remove("Title");
                    if (!items.contains(bookTitle)) {
                        String author = (String) (bookMap.remove("Author"));
                        String subject = (String) (bookMap.remove("Major"));

                        Book book = new Book(bookTitle, author, subject, 3.66f);
                        items.add(book);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void courseData() {
        final Firebase courseRef = new Firebase(Constants.FIREBASE_URL_COURSES);
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> courses = (HashMap<String, Object>) dataSnapshot.getValue();
                for (Object course : courses.values()) {
                    HashMap<String, Object> courseMap = (HashMap<String, Object>) course;
                    String courseName = (String) courseMap.remove("Name");
                    Log.d("Course:", "" + courseName);
                    if (!items.contains(courseName)) {
                        String code = (String) (courseMap.remove("Code"));
                        Course c = new Course(courseName, code);
                        items.add(c);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}