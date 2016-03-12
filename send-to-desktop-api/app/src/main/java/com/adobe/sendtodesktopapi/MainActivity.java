package com.adobe.sendtodesktopapi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.adobe.creativesdk.foundation.auth.AdobeAuthException;
import com.adobe.creativesdk.foundation.auth.AdobeAuthSessionHelper;
import com.adobe.creativesdk.foundation.auth.AdobeAuthSessionLauncher;
import com.adobe.creativesdk.foundation.auth.AdobeUXAuthManager;

public class MainActivity extends AppCompatActivity {

    private AdobeUXAuthManager mUXAuthManager = AdobeUXAuthManager.getSharedAuthManager();
    private AdobeAuthSessionHelper mAuthSessionHelper;

    private Button mOpenGalleryButton;
    private Button mSendToPhotoshopButton;
    private ImageView mSelectedImageView;

    private Uri mSelectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthSessionHelper = new AdobeAuthSessionHelper(mStatusCallback);
        mAuthSessionHelper.onCreate(savedInstanceState);

        mOpenGalleryButton = (Button) findViewById(R.id.openGalleryButton);
        mSendToPhotoshopButton = (Button) findViewById(R.id.sendToPhotoshopButton);
        mSelectedImageView = (ImageView) findViewById(R.id.selectedImageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

        View.OnClickListener mOpenGalleryButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryPickerIntent = new Intent();
                galleryPickerIntent.setType("image/*");
                galleryPickerIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryPickerIntent, "Select an Image"), 203); // Can be any int
            }
        };
        mOpenGalleryButton.setOnClickListener(mOpenGalleryButtonListener);

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

        if (resultCode == RESULT_OK && requestCode == 203) { // the int we used for startActivityForResult()

            mSelectedImageUri = data.getData();
            mSelectedImageView.setImageURI(mSelectedImageUri);
            
        }
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
        if (id == R.id.action_logout) {
            mUXAuthManager.logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
