package com.starsoft.medinfo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.starsoft.medinfo.adapters.SearchAdapter;
import com.starsoft.medinfo.adapters.TipPagerAdapter;
import com.starsoft.medinfo.asyncrssclient.AsyncRssClient;
import com.starsoft.medinfo.asyncrssclient.AsyncRssResponseHandler;
import com.starsoft.medinfo.asyncrssclient.RssFeed;
import com.starsoft.medinfo.asyncrssclient.RssItem;
import com.starsoft.medinfo.model.Drug;

import org.apache.http.Header;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AutoScrollViewPager viewPager;
    private TipPagerAdapter tipPagerAdapter = null;
    private List<RssItem> rssItemList = null;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CardView btn1,btn2,btn3,btn4;
    private ArrayList<String> mCountries = null;
    private ArrayList<Drug> drugList = null;
    private DatabaseReference reference = null;
    private ValueEventListener mPostListener;
    private TextView pname,pemail;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        sharedPreferences = getApplicationContext().getSharedPreferences("pref_user",MODE_PRIVATE);
        editor = sharedPreferences.edit();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // if user is null launch login activity
                    //startActivity(new Intent(UserActivity.this, LoginActivity.class));
                    //finish();
                }else{
                    //helloUserText.setText("Hello  " + user.getEmail() +"");
                    Log.d(TAG,user.getEmail());
                }
            }
        };


        pname = (TextView)findViewById(R.id.uName);
        pemail = (TextView)findViewById(R.id.uEmail);

        try {
            pname.setText(firebaseUser.getDisplayName());
            pemail.setText(firebaseUser.getEmail());
        }catch(Exception e){e.printStackTrace();}

        viewPager = (AutoScrollViewPager) findViewById(R.id.healthTip);
        final TextView pView = (TextView)findViewById(R.id.loadingText);
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

        String uri = "http://www.health.com/fitness/feed";
        AsyncRssClient client = new AsyncRssClient();
        client.read(uri, new AsyncRssResponseHandler() {
            @Override
            public void onSuccess(RssFeed rssFeed) {
                rssItemList = new ArrayList<RssItem>();
                ArrayList<Integer> list = new ArrayList<Integer>();
                for (int i=0; i<10; i++) {
                    list.add(new Integer(i));
                }
                Collections.shuffle(list);

                for(int k = 0; k<5; k++){
                    rssItemList.add(rssFeed.getRssItems().get(list.get(k)));
                }


                if(rssItemList.size() > 0)
                pView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                tipPagerAdapter = new TipPagerAdapter(getSupportFragmentManager(),rssItemList);
                viewPager.setAdapter(tipPagerAdapter);
                viewPager.setVisibility(View.VISIBLE);
                viewPager.startAutoScroll();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set the Viewpager

        btn1 = (CardView)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PresActivity.class));

            }
        });

        btn2 = (CardView)findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NearbyActivity.class));
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(mCountries == null){
                    mCountries = new ArrayList<>();
                }
                if(drugList == null){
                    drugList = new ArrayList<>();
                }
                else
                mCountries.clear();
                drugList.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d(TAG,""+snapshot.child("brand_name").getValue());
                    String brand_id = snapshot.child("drug_id").getValue().toString();
                    String brand_name = snapshot.child("brand_name").getValue().toString();
                    String generic_name = snapshot.child("generic_name").getValue().toString();
                    String brand_image = snapshot.child("info").child("brand_info").child("brand_image").getValue().toString();
                    String brand_contains = snapshot.child("info").child("brand_info").child("contains").getValue().toString();
                    String brand_manufacturer_name = snapshot.child("info").child("brand_info").child("manufacturer_name").getValue().toString();
                    String brand_type = snapshot.child("info").child("brand_info").child("type").getValue().toString();
                    String category = snapshot.child("info").child("category").getValue().toString();
                    String diseases = snapshot.child("info").child("diseases").getValue().toString();
                    String precautions = snapshot.child("info").child("precautions").getValue().toString();
                    String side_effects = snapshot.child("info").child("side_effects").getValue().toString();
                    String storage = snapshot.child("info").child("storage").getValue().toString();
                    String use_of_drug = snapshot.child("info").child("use_of_drug").getValue().toString();
                    String adult = snapshot.child("info").child("drug_dosage").child("adult").getValue().toString();
                    String child = snapshot.child("info").child("drug_dosage").child("child").getValue().toString();

                    Drug drug = new Drug(brand_id,brand_name,generic_name,brand_image,brand_contains,brand_manufacturer_name
                                         ,brand_type,category,diseases,precautions,side_effects,storage,use_of_drug,adult,child);

                    drugList.add(drug);
                    mCountries.add(brand_name);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
//                Toast.makeText(PrescriptionActivity.this, "Failed to load post.",
//                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

        reference.child("drugs").addValueEventListener(postListener);
        mPostListener = postListener;

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mPostListener!=null){
            reference.removeEventListener(mPostListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPostListener!=null){
            reference.removeEventListener(mPostListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else {
            super.onBackPressed();
        }

        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
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
        if (id == R.id.action_search) {
            loadToolBarSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            firebaseAuth.signOut();
            editor.putBoolean("showLogin",true);
            editor.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
//            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            if (matches != null && matches.size() > 0) {
//                String searchWrd = matches.get(0);
//                if (!TextUtils.isEmpty(searchWrd)) {
//                    searchView.setQuery(searchWrd, false);
//                }
//            }
//
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    public void loadToolBarSearch() {


        ArrayList<String> countryStored = SharedPreference.loadList(MainActivity.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES);



        View view = MainActivity.this.getLayoutInflater().inflate(R.layout.view_toolbar_search, null);
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        ImageView imgToolMic = (ImageView) view.findViewById(R.id.img_tool_mic);
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);

        Utils.setListViewHeightBasedOnChildren(listSearch);

        edtToolSearch.setHint("Search any medicines");

        final Dialog toolbarSearchDialog = new Dialog(MainActivity.this, R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(false);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();

        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        countryStored = (countryStored != null && countryStored.size() > 0) ? countryStored : new ArrayList<String>();
        final SearchAdapter searchAdapter = new SearchAdapter(MainActivity.this, countryStored, false);

        listSearch.setVisibility(View.VISIBLE);
        listSearch.setAdapter(searchAdapter);


        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String country = String.valueOf(adapterView.getItemAtPosition(position));
                SharedPreference.addList(MainActivity.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES, country);
                edtToolSearch.setText(country);
                Drug drug = drugList.get(position);
                Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
                System.out.println(drug);
                intent.putExtra("drugData",drug);
                listSearch.setVisibility(View.GONE);
                startActivity(intent);

            }
        });


        edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                String[] country = MainActivity.this.getResources().getStringArray(R.array.countries_array);
                listSearch.setVisibility(View.VISIBLE);
                searchAdapter.updateList(mCountries, true);


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> filterList = new ArrayList<String>();
                boolean isNodata = false;
                if (s.length() > 0) {
                    for (int i = 0; i < mCountries.size(); i++) {

                        if (mCountries.get(i).toLowerCase().startsWith(s.toString().trim().toLowerCase())) {

                            filterList.add(mCountries.get(i));

                            listSearch.setVisibility(View.VISIBLE);
                            searchAdapter.updateList(filterList, true);
                            isNodata = true;
                        }

                    }
                    if (!isNodata) {
                        listSearch.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                        txtEmpty.setText("No data found");
                    }
                } else {
                    listSearch.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
            }
        });

        imgToolMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtToolSearch.setText("");

            }
        });


    }
}
