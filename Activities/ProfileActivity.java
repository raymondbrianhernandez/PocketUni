package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class ProfileActivity extends BaseActivity {

    public AppCompatSpinner degreeSpinner,yearSpinner;
    public ArrayList<String> degrees = new ArrayList<String>();
    public ArrayList<String> levels = new ArrayList<String>();
    public final Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);
    Firebase studentRef;
    public ArrayList<String> courses;
    public android.support.v7.widget.AppCompatMultiAutoCompleteTextView courseTextView;

    TextInputEditText firstNameEditText,lastNameEditText,emailEditText,phoneEditText,univEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_profile, null,false);
        drawer.addView(contentView, 0);
        courses = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        studentRef = firebaseRef.child(Constants.FIREBASE_LOCATION_USERS).child(mEncodedEmail);

        intializeScreen();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    public void intializeScreen(){

        firstNameEditText = (TextInputEditText) findViewById(R.id.profile_firstname);
        lastNameEditText = (TextInputEditText) findViewById(R.id.profile_lastname);
        emailEditText = (TextInputEditText) findViewById(R.id.profile_email);
        phoneEditText = (TextInputEditText)findViewById(R.id.profile_phone);
        univEditText = (TextInputEditText) findViewById(R.id.profile_school);

        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> details = (HashMap<String,String>) dataSnapshot.getValue();
                firstNameEditText.setText(details.get("firstName").toString());
                lastNameEditText.setText(details.get("lastName").toString());
                phoneEditText.setText(details.get("phone")!= null ?  String.valueOf(details.get("phone")) : "");
                univEditText.setText(details.get("school")!= null ?  details.get("school").toString() : "");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        emailEditText.setText(mEncodedEmail.replace(",","."));
        emailEditText.setKeyListener(null);

        getCourseList();

        degrees.add("Select degree");
        degrees.add("Undergraduate");
        degrees.add("Graduate");

        levels.add("Select level of Study");
        levels.add("Freshmen");
        levels.add("Sophomore");
        levels.add("Junior");
        levels.add("Senior");

        degreeSpinner = (AppCompatSpinner)findViewById(R.id.spinner_degree);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,degrees);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        degreeSpinner.setAdapter(spinnerAdapter);

        yearSpinner = (AppCompatSpinner)findViewById(R.id.spinner_yearofstudy);
        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,levels);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        yearSpinner.setAdapter(spinnerAdapter2);

        courseTextView = (android.support.v7.widget.AppCompatMultiAutoCompleteTextView) findViewById(R.id.profile_courses);
        courseTextView.setTokenizer(new android.support.v7.widget.AppCompatMultiAutoCompleteTextView.CommaTokenizer());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_dropdown_item_1line,courses);
        //Getting the instance of AutoCompleteTextView

        courseTextView.setThreshold(1);//will start working from first character
        courseTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    }

    public void getCourseList(){
        Firebase courseRef = firebaseRef.child(Constants.FIREBASE_LOCATION_COURSES);
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot course : dataSnapshot.getChildren()){
                    Log.d("dds","" + course.getKey());
                    courses.add(course.getKey().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void saveToFirebase(View view){

        HashMap<String,Object> studentMap = new HashMap<>();
        studentMap.put("firstName",firstNameEditText.getText().toString());
        studentMap.put("lastName",lastNameEditText.getText().toString());
        studentMap.put("school",univEditText.getText().toString());
        studentMap.put("phone",Long.parseLong(phoneEditText.getText().toString()));
        studentMap.put("degree",degreeSpinner.getSelectedItem().toString());
        studentMap.put("year of study",yearSpinner.getSelectedItem().toString());
        studentMap.put("Courses",courseTextView.getText().toString());

        studentRef.updateChildren(studentMap);
        Toast.makeText(this,"Changes saved",Toast.LENGTH_SHORT).show();

    }
}
