package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import raymondhernandez.pocketuniv.R;

public class ContributeActivity extends BaseActivity {

    public Button profButton,bookButton,courseButton,restaurantButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_contribute, null,false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Provide Data");
        setSupportActionBar(toolbar);

        intializeScreen();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void intializeScreen(){
        profButton = (Button)findViewById(R.id.btn_prof);
        bookButton = (Button)findViewById(R.id.btn_book);
        courseButton = (Button)findViewById(R.id.btn_course);
        //restaurantButton = (Button)findViewById(R.id.btn_restaurant);

        profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ProfessorFragment professorFragment = ProfessorFragment.newInstance();
                professorFragment.show(fragmentManager,"Professor Fragment");
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                BookFragment bookFragment = BookFragment.newInstance();
                bookFragment.show(fragmentManager2,"Book Fragment");
            }
        });

        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                CourseFragment courseFragment = CourseFragment.newInstance();
                courseFragment.show(fragmentManager3,"Course Fragment");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }
}
