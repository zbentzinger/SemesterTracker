package com.wgu.zbentz2.semestertracker.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.wgu.zbentz2.semestertracker.R;
import com.wgu.zbentz2.semestertracker.utils.NotificationUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Notifications
        NotificationUtils.createNotificationChannel(this);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Nav
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Fragments
        loadFragment(
            getIntent().getIntExtra("fragmentID", R.id.nav_terms) // Notifications can populate this, else load the default terms fragment.
        );

    }

    @Override public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();

        }
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        loadFragment(item.getItemId());
        return true;

    }

    private void loadFragment(int menuId) {

        Fragment fragment = null;

        switch (menuId) {

            case R.id.nav_terms:

                fragment = new TermsList();
                navigationView.getMenu().getItem(0).setChecked(true);
                break;

            case R.id.nav_courses:

                fragment = new CoursesList();
                navigationView.getMenu().getItem(1).setChecked(true);
                break;

            case R.id.nav_assessments:

                fragment = new AssessmentsList();
                navigationView.getMenu().getItem(2).setChecked(true);
                break;

            case R.id.nav_notes:

                fragment = new NotesList();
                navigationView.getMenu().getItem(3).setChecked(true);
                break;

        }

        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
}
