package com.moyinoluwa.gdgngevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.batch.android.Batch;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.moyinoluwa.gdgngevents.Champion.ChampionLoginActivity;
import com.moyinoluwa.gdgngevents.Favorites.FavoriteEventsFragment;
import com.moyinoluwa.gdgngevents.Organizers.OrganizersLoginActivity;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Tracker mTracker;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Obtain the shared Tracker instance
        GDGNGEventsApplication application = (GDGNGEventsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_upcoming);

        Fragment newFragment = new UpcomingEventsFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, newFragment);
        transaction.commit();

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_organizers) {
            Intent i = new Intent(this, OrganizersLoginActivity.class);
            startActivity(i);
        } else if (id == R.id.action_champion) {
            Intent i = new Intent(this, ChampionLoginActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment newFragment = null;

        if (id == R.id.nav_upcoming) {
            newFragment = new UpcomingEventsFragment();
        } else if (id == R.id.nav_past) {
            newFragment = new PastEventsFragment();
        } else if (id == R.id.nav_favorites) {
            newFragment = new FavoriteEventsFragment();
        } else if (id == R.id.change_gdg) {
            newFragment = new ChangeGDGFragment();
        }

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, newFragment);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Batch.onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName("ItemListActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onStop() {
        Batch.onStop(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Batch.onDestroy(this);

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Batch.onNewIntent(this, intent);

        super.onNewIntent(intent);
    }
}
