package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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



public class AssignmentActivity extends BaseActivity {

    private Firebase mFirebaseRef, mFirebaseAssignRef;
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private RecyclerView recyclerView;
    private AssignmentAdapter assignmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_assignment, null, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assignments");
        setSupportActionBar(toolbar);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mFirebaseAssignRef = mFirebaseRef.child(Constants.FIREBASE_LOCATION_ASSIGNMENTS);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_assignment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        assignmentAdapter = new AssignmentAdapter(this, assignments);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(assignmentAdapter);

        getAssignments();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_assign);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssignmentActivity.this, CreateAssignmentActivity.class);
                intent.putExtra("Email",mEncodedEmail);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    public void getAssignments() {
        mFirebaseAssignRef.child(mEncodedEmail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Assignment assignment = dataSnapshot.getValue(Assignment.class);
                    assignments.add(assignment);
                    assignmentAdapter.notifyItemInserted(assignments.size() - 1);
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
        assignmentAdapter.notifyDataSetChanged();
    }
}
