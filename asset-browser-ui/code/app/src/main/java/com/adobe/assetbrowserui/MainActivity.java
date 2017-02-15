/*
	 * Copyright (c) 2017 Adobe Systems Incorporated. All rights reserved.
	 *
	 * Permission is hereby granted, free of charge, to any person obtaining a
	 * copy of this software and associated documentation files (the "Software"),
	 * to deal in the Software without restriction, including without limitation
	 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
	 * and/or sell copies of the Software, and to permit persons to whom the
	 * Software is furnished to do so, subject to the following conditions:
	 *
	 * The above copyright notice and this permission notice shall be included in
	 * all copies or substantial portions of the Software.
	 *
	 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
	 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
	 * DEALINGS IN THE SOFTWARE.
	 */


package com.adobe.assetbrowserui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.adobe.creativesdk.foundation.auth.AdobeAuthException;
import com.adobe.creativesdk.foundation.auth.AdobeAuthSessionHelper;
import com.adobe.creativesdk.foundation.auth.AdobeAuthSessionLauncher;
import com.adobe.creativesdk.foundation.auth.AdobeUXAuthManager;
import com.adobe.creativesdk.foundation.internal.utils.AdobeCSDKException;
import com.adobe.creativesdk.foundation.storage.AdobePhotoAsset;
import com.adobe.creativesdk.foundation.storage.AdobePhotoAssetRendition;
import com.adobe.creativesdk.foundation.storage.AdobePhotoException;
import com.adobe.creativesdk.foundation.storage.AdobeSelection;
import com.adobe.creativesdk.foundation.storage.AdobeSelectionPhotoAsset;
import com.adobe.creativesdk.foundation.storage.AdobeUXAssetBrowser;
import com.adobe.creativesdk.foundation.storage.IAdobeGenericRequestCallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    static final int REQ_CODE_CSDK_USER_AUTH = 1001;
    static final int REQ_CODE_CSDK_ASSET_BROWSER = 2001;

    private Button mLaunchAssetBrowserButton;
    private ImageView mSelectedAssetImageView;

    private AdobeUXAuthManager mUXAuthManager = AdobeUXAuthManager.getSharedAuthManager();
    private AdobeAuthSessionHelper mAuthSessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthSessionHelper = new AdobeAuthSessionHelper(mStatusCallback);
        mAuthSessionHelper.onCreate(savedInstanceState);

        mLaunchAssetBrowserButton = (Button) findViewById(R.id.launchAssetBrowserButton);
        mSelectedAssetImageView = (ImageView) findViewById(R.id.selectedAssetImageView);

        View.OnClickListener mLaunchAssetBrowserButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAssetBrowser();
            }
        };
        mLaunchAssetBrowserButton.setOnClickListener(mLaunchAssetBrowserButtonListener);
        
    }

    private void launchAssetBrowser() {
        AdobeUXAssetBrowser assetBrowser = AdobeUXAssetBrowser.getSharedInstance();

        try {
            assetBrowser.popupFileBrowser(this, REQ_CODE_CSDK_ASSET_BROWSER); // Can be any int
        }
        catch (AdobeCSDKException e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
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
                }
            }
        };
    }

    private void login() {

        AdobeAuthSessionLauncher authSessionLauncher = new AdobeAuthSessionLauncher.Builder()
                .withActivity(this)
                .withRequestCode(REQ_CODE_CSDK_USER_AUTH) // Can be any int
                .build();

        mUXAuthManager.login(authSessionLauncher);
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

        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_CSDK_USER_AUTH:

                    Log.i(TAG, "User successfully logged in!");

                    break;

                case REQ_CODE_CSDK_ASSET_BROWSER: // The request code we used in launchAssetBrowser()

                    /* 1) */
                    AdobeUXAssetBrowser.ResultProvider assetBrowserResult = new AdobeUXAssetBrowser.ResultProvider(data);
                    ArrayList listOfSelectedAssetFiles = assetBrowserResult.getSelectionAssetArray();
                    AdobeSelection selection = (AdobeSelection) listOfSelectedAssetFiles.get(0);

                    /* 2) */
                    if (selection instanceof AdobeSelectionPhotoAsset) {

                        /* 3) */
                        IAdobeGenericRequestCallback<byte[], AdobePhotoException> downloadCallBack = new IAdobeGenericRequestCallback<byte[], AdobePhotoException>() {
                            @Override
                            public void onCancellation() {
                                /* 3.a) Cancellation code here */
                                Log.d(TAG, "onCancellation()");
                            }

                            @Override
                            public void onCompletion(byte[] bytes) {
                                Log.d(TAG, "onCompletion()");

                                /* 3.b) */
                                InputStream inputStream = new ByteArrayInputStream(bytes);
                                Bitmap image = BitmapFactory.decodeStream(inputStream);
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mSelectedAssetImageView.setImageBitmap(image);
                            }

                            @Override
                            public void onError(AdobePhotoException e) {
                                /* 3.c) Error handler here */
                                Log.d(TAG, "onError()");
                                e.printStackTrace();
                            }

                            @Override
                            public void onProgress(double v) {
                                /* 3.d) Code for indicating download progress here */
                                Log.d(TAG, "onProgress()");
                            }
                        };

                        /* 4) */
                        AdobePhotoAsset photoAsset = ((AdobeSelectionPhotoAsset) selection).getSelectedItem();
                        Map<String, AdobePhotoAssetRendition> renditionMap = photoAsset.getRenditions();
                        for (Map.Entry<String, AdobePhotoAssetRendition> entry : renditionMap.entrySet()) {
                            Log.d(TAG, entry.getKey());
                        }
                        photoAsset.downloadRendition(renditionMap.get(AdobePhotoAsset.AdobePhotoAssetRenditionImage2048), downloadCallBack);

                        //photoAsset.downloadMasterData(downloadCallBack);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Please choose a Lightroom Photo", Toast.LENGTH_LONG).show();
                    }

                    break;
            }
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
