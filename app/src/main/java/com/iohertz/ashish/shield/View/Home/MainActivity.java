package com.iohertz.ashish.shield.View.Home;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iohertz.ashish.shield.R;
import com.iohertz.ashish.shield.View.Contact.ContactUsActivity;
import com.iohertz.ashish.shield.View.Devices.AddDeviceActivity;
import com.iohertz.ashish.shield.View.LoginSignup.LoginSignupActivity;
import com.iohertz.ashish.shield.View.Systems.ManageSystemsActivity;
import com.iohertz.ashish.shield.View.Users.AddUsersActivity;
import com.iohertz.ashish.shield.View.other.BottomNavigationViewHelper;
import com.iohertz.ashish.shield.View.other.CircleTransform;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private BottomNavigationView bottomNavigationView;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtEmail;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private int mTag;

    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    public static int navItemIndex = 0;
    public static int previousnavItemIndex = -1;

    private static final String TAG_HOME = "home";
    private static final String TAG_LOCK = "lock";
    private static final String TAG_BELL = "bell";
    private static final String TAG_USERS = "users";
    private static final String TAG_LOG = "log";
    public static String CURRENT_TAG = TAG_HOME;

    private String[] activityTitles;

    private boolean shoudldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigation);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtEmail = (TextView) navHeader.findViewById(R.id.email);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        loadNavHeader();

        setUpNavigationView();

        setUpBottomNavigationView();

        if (savedInstanceState == null) {
            mTag = bottomNavigationView.getMenu().getItem(navItemIndex).getItemId();
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void loadNavHeader() {
        txtName.setText("Ashish Kumar");
        txtEmail.setText("ashishkumar160@gmail.com");

        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
    }

    private void loadHomeFragment() {

        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                int currentTag = bottomNavigationView.getMenu().getItem(navItemIndex).getItemId();
                if(currentTag < mTag)
                {
                    fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_from_left, R.anim.fragment_slide_to_right);
                }
                else if(currentTag > mTag)
                {
                    fragmentTransaction.setCustomAnimations(R.anim.fragment_slide_from_right, R.anim.fragment_slide_to_left);
                }
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
                mTag = currentTag;
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return new HomeFragment();
            case 1:
                return new LockFragment();
            case 2:
                return new BellFragment();
            case 3:
                return new UserFragment();
            case 4:
                return new LogFragment();
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        TextView textView = (TextView) findViewById(R.id.toolbar_text);
        textView.setText(activityTitles[navItemIndex]);
        fab.setVisibility(View.INVISIBLE);
        if (navItemIndex == 4) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_search_black_24dp);
        }
    }

    private void selectNavMenu() {
        bottomNavigationView.getMenu().getItem(navItemIndex).setChecked(true);

    }

    private void setUpBottomNavigationView() {
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.action_lock:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_LOCK;
                        break;
                    case R.id.action_bell:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_BELL;
                        break;
                    case R.id.action_user:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_USERS;
                        break;
                    case R.id.action_log:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_LOG;
                        break;
                }
                loadHomeFragment();
                return true;
            }
        });
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_systems:
                        startActivity(new Intent(MainActivity.this, ManageSystemsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_contactus:
                        startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                        drawer.closeDrawers();;
                        return true;
                    default:
                        navItemIndex = 0;
                }
                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (shoudldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    public void invite(View view) {
        startActivity(new Intent(MainActivity.this, AddUsersActivity.class));
    }

    public void addDevice(View view) {
        startActivity(new Intent(MainActivity.this , AddDeviceActivity.class));
    }
}
