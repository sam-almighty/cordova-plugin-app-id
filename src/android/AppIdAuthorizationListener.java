package com.ibm.appid.clientsdk.cordovaplugins;/*
 * Copyright 2016, 2017 IBM Corp.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.ibm.cloud.appid.android.api.AppIDAuthorizationManager;
import com.ibm.cloud.appid.android.api.AuthorizationException;
import com.ibm.cloud.appid.android.api.AuthorizationListener;
import com.ibm.cloud.appid.android.api.tokens.AccessToken;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;
import com.ibm.cloud.appid.android.api.tokens.RefreshToken;

import org.apache.cordova.CallbackContext;

/**
 * This listener provides the callback methods that are called at the end of App ID
 * authorization process when using the {@link com.ibm.cloud.appid.android.api.AppID} login APIs
 */
public class AppIdAuthorizationListener implements AuthorizationListener {
    private NoticeHelper noticeHelper;
    private TokensPersistenceManager tokensPersistenceManager;
    private boolean isAnonymous;
    private Activity activity;
    CallbackContext callbackContext;

    public AppIdAuthorizationListener(Activity activity, AppIDAuthorizationManager authorizationManager, boolean isAnonymous, CallbackContext callbackContext) {
        tokensPersistenceManager = new TokensPersistenceManager(activity, authorizationManager);
        noticeHelper = new NoticeHelper(activity, authorizationManager, tokensPersistenceManager);
        this.isAnonymous = isAnonymous;
        this.activity = activity;
        this.callbackContext=callbackContext;
    }

    @Override
    public void onAuthorizationFailure(AuthorizationException exception) {
        Log.e(logTag("onAuthorizationFailure"),"Authorization failed", exception);
        callbackContext.error(exception.toString());
    }

    @Override
    public void onAuthorizationCanceled() {
        Log.w(logTag("onAuthorizationCanceled"),"Authorization canceled");
    }

    @Override
    public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
        Log.i(logTag("onAuthorizationSuccess"),"Authorization succeeded");
        tokensPersistenceManager.persistTokensOnDevice();
        callbackContext.success(accessToken.toString());
    }

    private String logTag(String methodName){
        return this.getClass().getCanonicalName() + "." + methodName;
    }
}
