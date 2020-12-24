package raymondhernandez.pocketuniv.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class RatingCourseActivity extends AppCompatActivity {

    public TextView difficultyTextView,contentTextView;
    public AppCompatSeekBar diffSeekbar,contentSeekbar;
    public int difficulty,content;
    String Code;

    public final Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_course);
        Intent intent = getIntent();
        Code = intent.getStringExtra("CourseCode");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Code);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intializeScreen();
    }

    public void intializeScreen() {
        difficultyTextView = (TextView) findViewById(R.id.diff_text);
        contentTextView = (TextView) findViewById(R.id.content_text);

        diffSeekbar = (AppCompatSeekBar) findViewById(R.id.diff_seekbar);
        contentSeekbar = (AppCompatSeekBar) findViewById(R.id.content_seekbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        diffSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                difficultyTextView.setText(getString(R.string.course_difficulty,progress));
                difficulty = progress;
            }
        });

        contentSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                contentTextView.setText(getString(R.string.course_content,progress));
                content = progress;
            }
        });
    }

    public void submitCourseRatings(View view){
        final Firebase ratingsRef = firebaseRef.child(Constants.FIREBASE_LOCATION_RATINGS);
        final Firebase courseRef = ratingsRef.child(Constants.FIREBASE_LOCATION_COURSES);
        final String studentEmail = firebaseRef.getAuth().getProviderData().get("email").toString();
        final String studentEncodeEmail = studentEmail.replace(".", ",");

        final HashMap<String,Object> ratingMap = new HashMap<>();
        HashMap<String,Object> courseRateMap = new HashMap<>();
        courseRateMap.put("Difficulty", difficulty);
        courseRateMap.put("Content", content);

        ratingMap.put(studentEncodeEmail, courseRateMap);

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> ratings = (HashMap<String, Object>) dataSnapshot.getValue();
                if(ratings == null){
                    courseRef.child(Code).setValue(ratingMap);
                }else {
                    if (ratings.containsKey(studentEncodeEmail)) {
                        courseRef.child(Code).updateChildren(ratingMap);
                    } else {
                        courseRef.child(Code).setValue(ratingMap);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Toast.makeText(this,"Ratings Submitted to database",Toast.LENGTH_LONG).show();
        //test();
        super.onBackPressed();
    }
}
