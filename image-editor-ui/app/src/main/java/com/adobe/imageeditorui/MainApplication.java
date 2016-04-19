package com.adobe.imageeditorui;

import android.app.Application;

import com.adobe.creativesdk.aviary.IAviaryClientCredentials;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;

/**
 * Created by ash on 3/11/16.
 */
public class MainApplication extends Application implements IAviaryClientCredentials {

    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID = Keys.CSDK_CLIENT_ID;
    private static final String CREATIVE_SDK_CLIENT_SECRET = Keys.CSDK_CLIENT_SECRET;

    @Override
    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String getBillingKey() {
        return ""; // Leave this blank
    }
}