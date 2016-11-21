package com.adobe.typekitui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.creativesdk.foundation.auth.AdobeAuthException;
import com.adobe.creativesdk.foundation.auth.AdobeAuthSessionHelper;
import com.adobe.creativesdk.foundation.auth.AdobeAuthSessionLauncher;
import com.adobe.creativesdk.foundation.auth.AdobeUXAuthManager;
import com.adobe.creativesdk.typekit.AdobeTypekitFont;
import com.adobe.creativesdk.typekit.AdobeTypekitFontBrowser;
import com.adobe.creativesdk.typekit.AdobeTypekitManager;
import com.adobe.creativesdk.typekit.TypekitNotification;
import com.adobe.creativesdk.typekit.UserNotAuthenticatedException;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/* Implement the `Observer` interface  */
public class MainActivity extends AppCompatActivity implements Observer {

    public static final String TAG = MainActivity.class.getSimpleName();
    static final int REQ_CODE_CSDK_USER_AUTH = 1001;

    private AdobeUXAuthManager mUXAuthManager = AdobeUXAuthManager.getSharedAuthManager();
    private AdobeAuthSessionHelper mAuthSessionHelper;

    /* Store an instance of `AdobeTypekitManager` as a member variable */
    private AdobeTypekitManager mTypekitManager = AdobeTypekitManager.getInstance();

    private Button mSyncTypekitFontsButton;
    private TextView mTargetTextView;
    private TextView mFontNameTextView;
    private Spinner mFontSpinner;
    private TextView mTargetTextView2;
    private EditText mFontEditText;
    private ProgressBar mSubsetFontProgressBar;
    private ProgressBar mEntireFontProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthSessionHelper = new AdobeAuthSessionHelper(mStatusCallback);
        mAuthSessionHelper.onCreate(savedInstanceState);

        mSyncTypekitFontsButton = (Button) findViewById(R.id.syncTypekitFontsButton);
        mTargetTextView = (TextView) findViewById(R.id.targetTextView);
        mFontNameTextView = (TextView) findViewById(R.id.fontNameTextView);
        mFontSpinner = (Spinner) findViewById(R.id.fontSpinner);
        mTargetTextView2 = (TextView) findViewById(R.id.targetTextView2);
        mFontEditText = (EditText) findViewById(R.id.fontEditText);
        mSubsetFontProgressBar = (ProgressBar) findViewById(R.id.subsetFontProgressBar);
        mEntireFontProgressBar = (ProgressBar) findViewById(R.id.entireFontProgressBar);

        View.OnClickListener applyRandomFontListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSyncTypekitFontsButton.setEnabled(false);
                mSubsetFontProgressBar.setVisibility(View.VISIBLE);
                mEntireFontProgressBar.setVisibility(View.VISIBLE);
                mTypekitManager.syncFonts();
            }
        };
        mSyncTypekitFontsButton.setOnClickListener(applyRandomFontListener);

        mFontEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    return true;
                }

                return false;
            }
        });

    }

    private AdobeAuthSessionHelper.IAdobeAuthStatusCallback mStatusCallback;
    {
        mStatusCallback = new AdobeAuthSessionHelper.IAdobeAuthStatusCallback() {
            @Override
            public void call(AdobeAuthSessionHelper.AdobeAuthStatus adobeAuthStatus, AdobeAuthException e) {
                if (!mUXAuthManager.isAuthenticated()) {
                    /* 3 */
                    login();
                } else {
                    Log.d(TAG, "Already logged in!");
                    initializeTypekitManager();
                }
            }
        };
    }

    /* 4 */
    private void login() {
        final String[] authScope = {"email", "profile", "address"};

        AdobeAuthSessionLauncher authSessionLauncher = new AdobeAuthSessionLauncher.Builder()
                .withActivity(this)
                .withRedirectURI(Keys.CSDK_REDIRECT_URI)
                .withAdditonalScopes(authScope)
                .withRequestCode(REQ_CODE_CSDK_USER_AUTH) // Can be any int
                .build();

        mUXAuthManager.login(authSessionLauncher);
    }

    private void initializeTypekitManager() {
        try {
            /* Initialize the `AdobeTypekitManager` instance */
            mTypekitManager.init(this);

            /* Add the Activity as an Observer to watch for Typekit notifications */
            mTypekitManager.addObserver(this);
        } catch (UserNotAuthenticatedException e) {
            e.printStackTrace();

            Toast.makeText(this, "Please log in to Creative Cloud to use Typekit fonts!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuthSessionHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthSessionHelper.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthSessionHelper.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuthSessionHelper.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuthSessionHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthSessionHelper.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_CSDK_USER_AUTH:
                    Log.i(TAG, "User successfully logged in!");

                    break;
            }
        }
    }

    /* Implement the `Observer` interface method */
    @Override
    public void update(Observable observable, Object data) {

        TypekitNotification notification = (TypekitNotification) data;

        switch (notification.getTypekitEvent()) {

            case TypekitNotification.Event.FONT_SELECTION_SYNC_START:
                Toast.makeText(MainActivity.this, "Syncing Typekit Synced Fonts list...", Toast.LENGTH_SHORT).show();
                break;

            case TypekitNotification.Event.FONT_SELECTION_REFRESH:
                Toast.makeText(MainActivity.this, "Typekit fonts synced", Toast.LENGTH_SHORT).show();
                ArrayList<AdobeTypekitFont> syncList = AdobeTypekitFont.getFonts();

                Random random = new Random();
                applySubsetFont(syncList.get(random.nextInt(syncList.size())));

                /* Set Spinner Adapter to display Typekit Synced Fonts list */
                setSpinnerAdapter(syncList);

                if (mFontSpinner != null) {

                    mFontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            applyEntireFont(syncList.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }

                mSyncTypekitFontsButton.setEnabled(true);

                break;

            case TypekitNotification.Event.FONT_SELECTION_SYNC_ERROR:
                Log.e(MainActivity.class.getSimpleName(), "Error: " + notification.getTypekitEvent());
                mSyncTypekitFontsButton.setEnabled(true);
                break;

            case TypekitNotification.Event.FONT_NETWORK_ERROR:
                Log.e(MainActivity.class.getSimpleName(), "Error: " + notification.getTypekitEvent());
                mSyncTypekitFontsButton.setEnabled(true);
                break;

            case TypekitNotification.Event.FONT_CACHE_EXPIRY:
                Log.e(MainActivity.class.getSimpleName(), "Warning: " + notification.getTypekitEvent());
                mSyncTypekitFontsButton.setEnabled(true);
                break;

            default:
                break;
        }
    }

    private void setSpinnerAdapter (ArrayList<AdobeTypekitFont> syncList) {

        ArrayList<String> list = new ArrayList<>();

        for (AdobeTypekitFont font : syncList) {
            list.add(font.displayName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mFontSpinner.setAdapter(dataAdapter);
    }

    private void applySubsetFont(AdobeTypekitFont adobeTypekitFont) {

        if (mSubsetFontProgressBar.getVisibility() == View.INVISIBLE) {
            mSubsetFontProgressBar.setVisibility(View.VISIBLE);
        }

        /* Get the string you will apply the typeface to */
        String targetString = mTargetTextView.getText().toString();

        /* Pass the string and a callback to `getSubsetTypeface()` */
        adobeTypekitFont.getSubsetTypeface(targetString, new AdobeTypekitFont.ITypekitCallback<Typeface, String>() {
            @Override
            public void onSuccess(AdobeTypekitFont adobeTypekitFont, Typeface typeface) {
                mSubsetFontProgressBar.setVisibility(View.INVISIBLE);

                /* Handle success */
                mTargetTextView.setTypeface(typeface);
                mFontNameTextView.setText(adobeTypekitFont.displayName());
            }

            @Override
            public void onError(AdobeTypekitFont adobeTypekitFont, String s) {
                mSubsetFontProgressBar.setVisibility(View.INVISIBLE);

                /* Handle errors */
                Log.e(MainActivity.class.getSimpleName(), s);
            }
        });
    }

    private void applyEntireFont(AdobeTypekitFont adobeTypekitFont) {

        if (mEntireFontProgressBar.getVisibility() == View.INVISIBLE) {
            mEntireFontProgressBar.setVisibility(View.VISIBLE);
        }

        String targetString = "You applied the selected Typekit font.";

        adobeTypekitFont.getTypeface(new AdobeTypekitFont.ITypekitCallback<Typeface, String>() {
            @Override
            public void onSuccess(AdobeTypekitFont adobeTypekitFont, Typeface typeface) {
                mEntireFontProgressBar.setVisibility(View.INVISIBLE);

                /* Handle success */
                mTargetTextView2.setTypeface(typeface);
                mTargetTextView2.setText(targetString);
                mFontEditText.setTypeface(typeface);
            }

            @Override
            public void onError(AdobeTypekitFont adobeTypekitFont, String s) {
                mEntireFontProgressBar.setVisibility(View.INVISIBLE);

                /* Handle errors */
                Log.e(MainActivity.class.getSimpleName(), s);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_launch_typekit) {
            AdobeTypekitFontBrowser.launchActivity(MainActivity.this);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mUXAuthManager.logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
