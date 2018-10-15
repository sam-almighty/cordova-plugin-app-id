/*
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

package com.ibm.appid.clientsdk.cordovaplugins;

import android.content.Context;

import com.ibm.cloud.appid.android.api.AppIDAuthorizationManager;



/**
 * A helper class for providing notifications to the sample users about the authorization process and
 * profile state at App ID
 *
 */
public class NoticeHelper {
    private Context ctx;
    private AppIDAuthorizationManager authorizationManager;
    private TokensPersistenceManager persistenceManager;

    enum AuthState {
        progressive_auth, switch_to_identified, logged_in_new, logged_in_again, new_guest, returning_guest, login_back, login_back_and_change_login
    }

    public NoticeHelper(Context ctx, AppIDAuthorizationManager authorizationManager, TokensPersistenceManager persistenceManager) {
        this.ctx = ctx;
        this.authorizationManager = authorizationManager;
        this.persistenceManager = persistenceManager;
    }

    public String getNoticeForState(AuthState state){
        switch(state){
            case logged_in_again:
                return "A known identified user logged in again. His Profile is fetched from App ID.";
            case login_back:
                return "An identified user returned to the app with the same identity. The app accesses his identified profile and the previous selections that he made.";
            case logged_in_new:
                return "An identified user logged in for the first time. A new Profile was created on App ID. Now when he logs in with the same credentials from any device or web client, the app will show his same profile and selections.";
            case login_back_and_change_login:
                return "The user signed back into the device with a different identity (e.g. was with Facebook, now with Google). The profile related to the identity he currenlty logged in with is now shown.";
            case new_guest:
                return "A guest user started using the app. App ID created a new anonymous profile, where the user’s selections can be stored. Note that this anonymous profile is only available from this device.";
            case progressive_auth:
                return "A guest user logged-in for the first time. App ID assigned this user’s identity to his anonymous profile, so his previous selections are saved. The anonymous token previously used to access this profile is now invalid.";
            case returning_guest:
                return "A guest user returned. The app uses his existing anonymous profile, so his previous selections are shown. Note that this anonymous user profile is only available from this device.";
            case switch_to_identified:
                return "A user started to use the app anonymously, made some selections, and then logged in. Since he had logged in in the past, the app switches over to his existing identified profile in place of his anonymous profile.";
            default:
                return null;
        }
    }

    /**
     * Calculating the authorization state based on stored token and new authorization data
     * @param isAnonLogin true means last action is anonymousLogin, otherwise its an identity login
     * @return
     */
    public AuthState determineAuthState(boolean isAnonLogin){
        String lastAuthorizedUserId = authorizationManager.getAccessToken().getSubject();

        switch(persistenceManager.getStoreTokenState()){
            case empty:
                if(isAnonLogin){
                    return AuthState.new_guest;
                }else{
                    return AuthState.logged_in_again;
                }

            case anonymous:
                if(isAnonLogin){
                    return AuthState.returning_guest;
                }else{
                    if( lastAuthorizedUserId.equals(persistenceManager.getStoredUserID())) {
                        return AuthState.progressive_auth;
                    }else{
                        return AuthState.switch_to_identified;
                    }
                }

            case identified:
                if(isAnonLogin){
                    return AuthState.new_guest;
                }else{
                    if( lastAuthorizedUserId.equals(persistenceManager.getStoredUserID())) {
                        return AuthState.login_back;
                    }else{
                        return AuthState.login_back_and_change_login;
                    }
                }

            default: throw new RuntimeException("Invalid token persistence state");
        }
    }

}
