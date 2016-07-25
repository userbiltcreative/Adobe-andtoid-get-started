package com.adobe.typekitui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    private AdobeUXAuthManager mUXAuthManager = AdobeUXAuthManager.getSharedAuthManager();
    private AdobeAuthSessionHelper mAuthSessionHelper;

    /* Store an instance of `AdobeTypekitManager` as a member variable */
    private AdobeTypekitManager mTypekitManager = AdobeTypekitManager.getInstance();

    private Button mApplyRandomFontButton;
    private TextView mTargetTextView;
    private TextView mFontNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthSessionHelper = new AdobeAuthSessionHelper(mStatusCallback);
        mAuthSessionHelper.onCreate(savedInstanceState);

        try {
            /* Initialize the `AdobeTypekitManager` instance */
            mTypekitManager.init(this);

            /* Add the Activity as an Observer to watch for Typekit notifications */
            mTypekitManager.addObserver(this);
        } catch (UserNotAuthenticatedException e) {
            e.printStackTrace();

            Toast.makeText(this, "Please log in to Creative Cloud to use Typekit fonts!", Toast.LENGTH_LONG).show();
        }

        mApplyRandomFontButton = (Button) findViewById(R.id.applyRandomFontButton);
        mTargetTextView = (TextView) findViewById(R.id.targetTextView);
        mFontNameTextView = (TextView) findViewById(R.id.fontNameTextView);

        View.OnClickListener applyRandomFontListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypekitManager.syncFonts();
            }
        };
        mApplyRandomFontButton.setOnClickListener(applyRandomFontListener);

    }

    private AdobeAuthSessionHelper.IAdobeAuthStatusCallback mStatusCallback;
    {
        mStatusCallback = new AdobeAuthSessionHelper.IAdobeAuthStatusCallback() {
            @Override
            public void call(AdobeAuthSessionHelper.AdobeAuthStatus adobeAuthStatus, AdobeAuthException e) {
                if (AdobeAuthSessionHelper.AdobeAuthStatus.AdobeAuthLoggedIn == adobeAuthStatus) {
                    showAuthenticatedUI();
                } else {
                    showAdobeLoginUI();
                }
            }
        };
    }

    private void showAdobeLoginUI() {
        mUXAuthManager.login(new AdobeAuthSessionLauncher.Builder()
                        .withActivity(this)
                        .withRequestCode(200) // Can be any int
                        .build()
        );
    }

    private void showAuthenticatedUI() {

        Log.i(MainActivity.class.getSimpleName(), "User is logged in!");

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
                ArrayList<AdobeTypekitFont> syncList = AdobeTypekitFont.getFonts();
                Random random = new Random();
                applyFont(syncList.get(random.nextInt(syncList.size())));
                break;

            case TypekitNotification.Event.FONT_SELECTION_SYNC_ERROR:
                Log.e(MainActivity.class.getSimpleName(), "Error: " + notification.getTypekitEvent());
                break;

            case TypekitNotification.Event.FONT_NETWORK_ERROR:
                Log.e(MainActivity.class.getSimpleName(), "Error: " + notification.getTypekitEvent());
                break;

            case TypekitNotification.Event.FONT_CACHE_EXPIRY:
                Log.e(MainActivity.class.getSimpleName(), "Warning: " + notification.getTypekitEvent());
                break;

            default:
                break;
        }
    }

    private void applyFont(AdobeTypekitFont adobeTypekitFont) {

        /* Get the string you will apply the typeface to */
        String targetString = mTargetTextView.getText().toString();

        /* Pass the string and a callback to `getSubsetTypeface()` */
        adobeTypekitFont.getSubsetTypeface(targetString, new AdobeTypekitFont.ITypekitCallback<Typeface, String>() {
            @Override
            public void onSuccess(AdobeTypekitFont adobeTypekitFont, Typeface typeface) {

                /* Handle success */
                mTargetTextView.setTypeface(typeface);
                mFontNameTextView.setText(String.format("Font name: %s", adobeTypekitFont.displayName()));
            }

            @Override
            public void onError(AdobeTypekitFont adobeTypekitFont, String s) {

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
