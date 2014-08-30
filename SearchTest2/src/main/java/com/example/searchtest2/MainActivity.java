package com.example.searchtest2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searchtest2.Model.AppDataCollector;
import com.example.searchtest2.Model.ContactDataCollector;
import com.example.searchtest2.Model.DataCollector;
import com.example.searchtest2.Model.ImageDataCollector;
import com.example.searchtest2.Model.SmsDataCollector;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener, ActionBar.TabListener, AdapterView.OnItemClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    public ListView mListView1;
    public ListView mListView2;
    public ListView mListView3;
    SearchView mSearchView;
    public ArrayAdapter<String> mAdapter;
    public ArrayAdapter<String> mContactsAdapter;
    public ArrayAdapter<String> mImageAdapter;
    public ArrayAdapter<String> mSmsAdapter;
    ArrayList<DataCollector> mAppsList;
    ArrayList<DataCollector> mContactsList;
    ArrayList<DataCollector> mImageList;
    ArrayList<DataCollector> mSmsList;
    int mPosition;


    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    SharedPreferences mShPref;

    /**
     * The {@link ViewPager} that will host the section contents.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        populateList();
        populateContactsList();
        populateImageList();
        populateSmsList();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
            //actionBar.getTabAt(1).select();


        }
        //getActionBar().getTabAt(0).select();
        /*mListView1.setAdapter(mAdapter);
        mListView1.setOnItemClickListener(this);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        //mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(false);
        mShPref = PreferenceManager.getDefaultSharedPreferences(this);
        mSearchView.setQuery(mShPref.getString("query", ""),true);
       /* mSearchView = (SearchView) searchItem.getActionView();
        //mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(false);
        mListView1 =(ListView) findViewById(R.id.listView1);
       *//* populateList();
        populateContactsList();*//*
        getActionBar().getTabAt(0).select();
        mListView1.setAdapter(mAdapter);
        mListView1.setOnItemClickListener(this);*/
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //ClickListener(this);
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            //System.out.println("GGGGGGG  "+mAccel);
            if(mAccel>10)
            {
               mSearchView.setQuery("",true);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //System.out.println("HHHHHH  "+mAccel);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onStop() {
        mShPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor toEdit = mShPref.edit();
        toEdit.putString("query", (String) mSearchView.getQuery().toString());
        toEdit.commit();
        super.onStop();

    }

    public void populateList() {
        AppDataCollector appDataCollector = new AppDataCollector();
        ArrayList<DataCollector> appsDatalist = appDataCollector.getData(this);

        ArrayList<String> appsName = new ArrayList<String>();
        for (int i = 0; i < appsDatalist.size(); i++) {
            String s = appsDatalist.get(i).getName();
            appsName.add(s.substring(s.lastIndexOf(".") + 1).trim());
        }
        mAppsList = appsDatalist;

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appsName);
        //mListView1.setAdapter(mAdapter);
    }

    public void populateContactsList() {
        ContactDataCollector contactDataCollector = new ContactDataCollector();
        mContactsList = contactDataCollector.getData(this);
        ArrayList<String> appsName = new ArrayList<String>();
        for (int i = 0; i < mContactsList.size(); i++) {
            String s = mContactsList.get(i).getName();
            appsName.add(s);
        }

        mContactsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appsName);
    }

    public void populateImageList() {
        ImageDataCollector imageDataCollector = new ImageDataCollector();
        mImageList = imageDataCollector.getData(this);
        ArrayList<String> appsName = new ArrayList<String>();
        for (int i = 0; i < mImageList.size(); i++) {
            String s = mImageList.get(i).getName();
            appsName.add(s);
        }

        mImageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appsName);
    }


    public void populateSmsList() {
        SmsDataCollector smsDataCollector = new SmsDataCollector();
        mSmsList = smsDataCollector.getData(this);
        ArrayList<String> appsName = new ArrayList<String>();
        for (int i = 0; i < mSmsList.size(); i++) {
            String s = mSmsList.get(i).getName();
            appsName.add(s);
        }

        mSmsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appsName);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String arg0) {
        // TODO Auto-generated method stub
        this.mAdapter.getFilter().filter(arg0.toString());
        this.mContactsAdapter.getFilter().filter(arg0.toString());
        this.mImageAdapter.getFilter().filter(arg0.toString());
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String arg0) {
        // TODO Auto-generated method stub
        this.mAdapter.getFilter().filter(arg0.toString());
        this.mContactsAdapter.getFilter().filter(arg0.toString());
        this.mImageAdapter.getFilter().filter(arg0.toString());
        return false;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        View ff = mViewPager.getChildAt(tab.getPosition());
        mViewPager.setCurrentItem(tab.getPosition());
        mPosition = tab.getPosition();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long row) {
        /*PackageInfo packageInfo = (PackageInfo) parent
                .getItemAtPosition(position);
        DataCollector appData = (DataCollector) getApplicationContext();*/
        int pos = 0;
        if (mPosition == 0) {

            for (int i = 0; i < mAppsList.size(); i++) {
                if (mAppsList.get(i).getName().equals(((TextView) view).getText())) {
                    pos = i;
                }
            }
            Intent appInfo = mAppsList.get(pos).getIntent();
            startActivity(appInfo);
        } else if (mPosition == 1) {
            /*Intent intent= new Intent(Intent.ACTION_PICK,  Contacts.CONTENT_URI);

            startActivityForResult(intent, PICK_CONTACT);*/
            for (int i = 0; i < mContactsList.size(); i++) {
                if (mContactsList.get(i).getName().equals(((TextView) view).getText())) {
                    pos = i;
                }
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(mContactsList.get(pos).getId()));
            intent.setData(uri);
            this.startActivity(intent);
            /*Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT);
            System.out.println("CONTACTS");*/
        } else{

            for (int i = 0; i < mImageList.size(); i++) {
                if (mImageList.get(i).getName().equals(((TextView) view).getText())) {
                    pos = i;
                }
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(mImageList.get(pos).getBody())), "image/*");
            startActivity(intent);
            Toast.makeText(this, "IMAGES", Toast.LENGTH_SHORT);
            System.out.println("IMAGES");
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<PlaceholderFragment> mFragments = new ArrayList<PlaceholderFragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<PlaceholderFragment>();
            PlaceholderFragment appsFragment = new PlaceholderFragment(0, "apps");
            PlaceholderFragment contactsFragment = new PlaceholderFragment(1, "contacts");
            PlaceholderFragment imagesFragment = new PlaceholderFragment(3, "images");

            mFragments.add(appsFragment);
            mFragments.add(contactsFragment);
            mFragments.add(imagesFragment);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        MainActivity m;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private String mFragmentType;

        public PlaceholderFragment(int sectionNumber, String type) {
            //PlaceholderFragment fragment = new PlaceholderFragment();
            this.setFragmentType(type);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            this.setArguments(args);
        }

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public void setFragmentType(String type) {
            mFragmentType = type;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            m = (MainActivity) getActivity();


            if (mFragmentType.equals("apps")) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                m.mListView1 = (ListView) rootView.findViewById(R.id.listView1);
                //textView.setText("apps");
                m.mListView1.setAdapter(m.mAdapter);
                m.mListView1.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

            } else if (mFragmentType.equals("contacts")) {
                rootView = inflater.inflate(R.layout.fragment_contacts_main, container, false);
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label2);
                m.mListView2 = (ListView) rootView.findViewById(R.id.listView2);
                m.mListView2.setAdapter(m.mContactsAdapter);
                //textView.setText("contacts");
                m.mListView2.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

            }else if (mFragmentType.equals("images")) {
                rootView = inflater.inflate(R.layout.fragment_images_main, container, false);
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label3);
                m.mListView3 = (ListView) rootView.findViewById(R.id.listView3);
                m.mListView3.setAdapter(m.mImageAdapter);
                //textView.setText("images");
                m.mListView3.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

            }

            return rootView;
        }


    }

}
