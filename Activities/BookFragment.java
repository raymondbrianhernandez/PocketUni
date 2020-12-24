package raymondhernandez.pocketuniv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

/**
 * Created by Chachi on 4/16/2016.
 */
public class BookFragment extends DialogFragment {
    public Firebase bookRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookRef = new Firebase(Constants.FIREBASE_URL_BOOKS);
    }

    public static BookFragment newInstance(){
        BookFragment bookFragment = new BookFragment();
        return bookFragment;
    }

    public BookFragment() {
    }
    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.contribute_book, null);

        final EditText titleEditText = (EditText)rootView.findViewById(R.id.book_title);
        final EditText authorEditText = (EditText)rootView.findViewById(R.id.et_book_author);
        final EditText majorEditText = (EditText)rootView.findViewById(R.id.book_major);

        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.submit_data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addBook(titleEditText.getText().toString(),authorEditText.getText().toString(),
                                majorEditText.getText().toString());
                    }
                });
        return builder.create();
    }

    public void addBook(final String title, String author, String major) {
        final HashMap<String,Object> bookMap = new HashMap<>();
        bookMap.put("Title",title);
        bookMap.put("Author",author);
        bookMap.put("Major",major);

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> books = (HashMap<String, Object>) dataSnapshot.getValue();
                if(books.containsValue(bookMap)){

                }else{
                    bookRef.child(title).setValue(bookMap);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
