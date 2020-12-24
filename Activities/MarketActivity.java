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
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

import raymondhernandez.pocketuniv.Model.Sell;
import raymondhernandez.pocketuniv.R;
import raymondhernandez.pocketuniv.Utils.Constants;
import raymondhernandez.pocketuniv.Utils.DividerItemDecoration;

public class MarketActivity extends BaseActivity {

    public Firebase mFirebaseMarketRef;
    public ArrayList<Sell> items = new ArrayList<>();
    public RecyclerView recyclerView;
    public SellAdapter sellAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_market, null, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mFirebaseMarketRef = new Firebase(Constants.FIREBASE_URL_MARKET);
//
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_market);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
         sellAdapter = new SellAdapter(this, items);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(sellAdapter);

        getItems();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sell);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                SellFragment sellFragment = SellFragment.newInstance();
                sellFragment.show(fragmentManager,"Professor Fragment");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void getItems(){
        mFirebaseMarketRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {
                    Sell sell = dataSnapshot.getValue(Sell.class);
                    items.add(sell);
                    sellAdapter.notifyItemInserted(items.size() - 1);
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
    }
}
