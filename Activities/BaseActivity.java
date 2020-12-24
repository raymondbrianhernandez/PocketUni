package raymondhernandez.pocketuniv.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = BaseActivity.class.getSimpleName();

    protected FrameLayout placeHolderLayout;
    protected DrawerLayout drawer;
    private TextView sName;
    private TextView sEmail;
    private CircleImageView sImage;

    protected String mProvider, mEncodedEmail;
    protected Firebase mFirebaseRef;
    protected Firebase mFirebaseStudentRef;
    protected ValueEventListener mUserRefListener;
    protected String studentFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
        /* Get mEncodedEmail and mProvider from SharedPreferences, use null as default value */
        mEncodedEmail = sp.getString(Constants.KEY_ENCODED_EMAIL, null);
        mProvider = sp.getString(Constants.KEY_PROVIDER, null);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mFirebaseStudentRef = new Firebase((Constants.FIREBASE_URL_USERS));


        placeHolderLayout = (FrameLayout)findViewById(R.id.placeholder_layout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideSoftKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        sName = (TextView) headerView.findViewById(R.id.StudentName);
        sEmail = (TextView) headerView.findViewById(R.id.StudentMail);
        sImage = (CircleImageView) headerView.findViewById(R.id.profile_image);

        mUserRefListener = mFirebaseStudentRef.child(mEncodedEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String,Object> student = (HashMap<String, Object>) snapshot.getValue();

                if (student != null) {

                    String studentName = student.get("firstName").toString() + " " + student.get("lastName").toString();
                    studentFullName = studentName;
                    String studentMail = student.get("email").toString().replace(",",".");

                    String studentModName = studentName.replace(" ","%20");
                    sName.setText(studentName);
                    sEmail.setText(studentMail);
                    String sImageURL = getResources().getString(R.string.image_url,studentModName);
                    Log.d(LOG_TAG,sImageURL);
                    Picasso.with(getApplicationContext())
                            .load(sImageURL)
                            .into(sImage);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //Log.e(LOG_TAG,getString(R.string.log_error_the_read_failed) + firebaseError.getMessage());
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_dashboard){

            Intent intent = new Intent(BaseActivity.this, DashboardActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_assignments) {
            Intent intent = new Intent(BaseActivity.this, AssignmentActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_ratings) {
            Intent intent = new Intent(BaseActivity.this, RatingActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.nav_chats){
            Intent intent = new Intent(BaseActivity.this, ChatListActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_STUDENT_NAME,studentFullName);
            startActivity(intent);
        } else if (id == R.id.nav_market) {
            Intent intent = new Intent(BaseActivity.this, MarketActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_profile){
            Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share) {
            Intent intent = new Intent(BaseActivity.this, ContributeActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_send){
            Intent intent = new Intent(BaseActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_friends){
            Intent intent = new Intent(BaseActivity.this, FriendsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            logout();
            takeUserToLoginScreenOnUnAuth();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void logout() {
        if (mProvider != null) {
            mFirebaseRef.unauth();
        }
    }

    private void takeUserToLoginScreenOnUnAuth() {
        /* Move user to LoginActivity, and remove the backstack */
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void hideSoftKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}
