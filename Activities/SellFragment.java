package raymondhernandez.pocketuniv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.support.v4.app.DialogFragment;
import raymondhernandez.pocketuniv.Model.Sell;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;

/**
 * Created by Chachi on 5/12/2016.
 */
public class SellFragment extends DialogFragment{
    public Firebase marketRef;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marketRef = new Firebase(Constants.FIREBASE_URL_MARKET);
    }

    public static SellFragment newInstance(){
        SellFragment sellFragment = new SellFragment();
        return sellFragment;
    }

    public SellFragment() {
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
        View rootView = inflater.inflate(R.layout.sell_dialog, null);

        final EditText item = (EditText)rootView.findViewById(R.id.sell_item);
        final EditText type = (EditText)rootView.findViewById(R.id.sell_type);
        final EditText notes = (EditText)rootView.findViewById(R.id.sell_notes);
        final EditText usage = (EditText)rootView.findViewById(R.id.sell_months);
        final EditText price = (EditText)rootView.findViewById(R.id.sell_price);

        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.submit_data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addSell(item.getText().toString(),type.getText().toString(),
                                notes.getText().toString(),Integer.parseInt(usage.getText().toString()),
                                Double.parseDouble(price.getText().toString()));
                    }
                });
        return builder.create();
    }

    public void addSell(final String title, String type, String notes,int months,double price) {

        final Sell sell = new Sell(title,type,notes,months,price);

        marketRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                marketRef.push().setValue(sell);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
