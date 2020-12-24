package raymondhernandez.pocketuniv.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.HashMap;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

public class RatingBookActivity extends AppCompatActivity {

    public TextView levelTextView, explnTextView, exmplTextView;
    public EditText CommentEditText;
    public SeekBar levelSeekBar, explnSeekBar, exmplSeekBar;
    public AppCompatCheckBox funBook;
    public AppCompatSpinner purposeSpinner,percentSpinner;
    public String bookTitle;

    public int explanations,exercises;
    public String level,purpose,percentage,Comments;
    public String[] levels = {"Beginner","Intermediate","Advanced"};
    public boolean fun;

    public String[] purposes = {"Academic TextBook","Academic Reference","Personal Research"};
    public String[] percent = {"Less than 20","20 - 40","40 - 70","Above 70"};

    public final Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_book);

        Intent intent = getIntent();
        bookTitle = intent.getStringExtra("Name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rating_book);
        toolbar.setTitle(bookTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intializeScreen();
    }

    public void intializeScreen(){
        levelTextView = (TextView) findViewById(R.id.booklevel_text);
        explnTextView = (TextView) findViewById(R.id.bookexplain_text);
        exmplTextView = (TextView) findViewById(R.id.bookexercise_text);

        funBook = (AppCompatCheckBox)findViewById(R.id.bookfun_checkbox);

        CommentEditText = (EditText)findViewById(R.id.bookcomments_text);

        levelSeekBar = (AppCompatSeekBar) findViewById(R.id.booklevel_seekbar);
        explnSeekBar = (AppCompatSeekBar) findViewById(R.id.bookexplain_seekbar);
        exmplSeekBar = (AppCompatSeekBar) findViewById(R.id.bookexercise_seekbar);

        purposeSpinner = (AppCompatSpinner) findViewById(R.id.spinner_bookpurpose);
        percentSpinner = (AppCompatSpinner) findViewById(R.id.spinner_percentageread);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, purposes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        purposeSpinner.setAdapter(spinnerAdapter);

        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, percent);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        percentSpinner.setAdapter(spinnerAdapter2);
    }


    @Override
    protected void onResume() {
        super.onResume();

        fun = funBook.isChecked();
        purposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                purpose = purposeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                purpose = "N/A";
            }
        });
        percentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                percentage = percentSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                percentage = "40 - 60";
            }
        });
        handleSeekbars();
    }

    public void handleSeekbars(){
        levelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                level = levels[progress];
                levelTextView.setText(getString(R.string.book_level,level));
            }
        });

        explnSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                explnTextView.setText(getString(R.string.book_explanation,progress));
                explanations = progress;
            }
        });

        exmplSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {        }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                exmplTextView.setText(getString(R.string.book_exercises,progress));
                exercises = progress;
            }
        });
    }

    public void submitBookRatings(View view){

        Comments = CommentEditText.getText().toString();
        final Firebase ratingsRef = firebaseRef.child(Constants.FIREBASE_LOCATION_RATINGS);
        final Firebase bookRatingRef = ratingsRef.child(Constants.FIREBASE_LOCATION_RATINGS_BOOKS);
        final String studentEmail = firebaseRef.getAuth().getProviderData().get("email").toString();
        final String studentEncodeEmail = studentEmail.replace(".", ",");

        final HashMap<String,Object> ratingMap = new HashMap<>();
        final HashMap<String,Object> bookRateMap = new HashMap<>();
        bookRateMap.put("Purpose", purpose);
        bookRateMap.put("Percentage Read", percentage);
        bookRateMap.put("Level", level);
        bookRateMap.put("Explanations", explanations);
        bookRateMap.put("Exercises", exercises);
        bookRateMap.put("Fun Book", fun);
        bookRateMap.put("Comments",Comments);

        ratingMap.put(studentEncodeEmail, bookRateMap);

        bookRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> ratings = (HashMap<String, Object>) dataSnapshot.getValue();
                if(ratings == null){
                    bookRatingRef.child(bookTitle).setValue(ratingMap);
                }else {
                    Firebase bookRef = bookRatingRef.child(bookTitle);
                    if (ratings.containsKey(bookTitle)) {
                        bookRef.updateChildren(ratingMap);
                    } else {
                        bookRef.setValue(ratingMap);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Toast.makeText(this,"Ratings Submitted to database",Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }
}
