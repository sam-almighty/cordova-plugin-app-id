/*
   Licensed Materials - Property of IBM

   (C) Copyright 2016 IBM Corp.

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ibm.appid.clientsdk.cordovaplugins;

import android.util.Log;

import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.AppIDAuthorizationManager;
import com.ibm.cloud.appid.android.api.AuthorizationException;
import com.ibm.cloud.appid.android.api.LoginWidget;
import com.ibm.cloud.appid.android.api.tokens.AccessToken;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;
import com.ibm.cloud.appid.android.api.tokens.RefreshToken;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;



public class CDVAppId extends CordovaPlugin {

    private AppID appId;

    private AppIDAuthorizationManager appIDAuthorizationManager;

    private static CallbackContext notificationCallback;

    private TokensPersistenceManager tokensPersistenceManager;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
       
        if ("initialize".equals(action)) {
            String tenantId = args.getString(0);
            String region = args.getString(1);
            this.initialize(tenantId,region,callbackContext);
            return true;
        } else if ("login".equals(action)) {
            String userName = args.getString(0);
            String password = args.getString(1);
            this.login(userName,password,callbackContext);
            return true;
        }
        return false;
    }
    
    /**
     * Initializes AppId instance
     * @param tenantId appId tenantId
     * @param region region to connect to
     * @param callbackContext Javascript callback
     */    
    private void initialize(String tenantId ,String region, final CallbackContext callbackContext) {
    	try  {
            appId = AppID.getInstance();
            appId.initialize(this.cordova.getActivity().getApplicationContext(), tenantId, region);
            appIDAuthorizationManager = new AppIDAuthorizationManager(this.appId);
            tokensPersistenceManager = new TokensPersistenceManager(this.cordova.getActivity(), appIDAuthorizationManager);
            String storedRefreshToken = tokensPersistenceManager.getStoredRefreshToken();
            if (storedRefreshToken != null && !storedRefreshToken.isEmpty()) {
                refreshTokens(storedRefreshToken,callbackContext);
            } else {
                callbackContext.error("");
            }
    	} catch (Exception e) {
    		callbackContext.error(e.toString());
    	}
    }


    private void refreshTokens(String refreshToken, CallbackContext callbackContext) {
        Log.d("refreshTokens", "Trying to refresh tokens using a refresh token");
        boolean storedTokenAnonymous = tokensPersistenceManager.isStoredTokenAnonymous();
        AppIdSampleAuthorizationListener appIdSampleAuthorizationListener =
                new AppIdSampleAuthorizationListener(this.cordova.getActivity(), appIDAuthorizationManager, storedTokenAnonymous,callbackContext) {
                    @Override
                    public void onAuthorizationFailure(AuthorizationException exception) {
                        tokensPersistenceManager.persistTokensOnDevice();
                        callbackContext.error("");
                    }

                    @Override
                    public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
                        callbackContext.success();
                    }
                };
        appId.signinWithRefreshToken(this.cordova.getActivity(), refreshToken, appIdSampleAuthorizationListener);
    }


    private void login(String userName,String password,final CallbackContext callbackContext){
        try{
            AppIdSampleAuthorizationListener appIdSampleAuthorizationListener =
                    new AppIdSampleAuthorizationListener(this.cordova.getActivity(), appIDAuthorizationManager, false,callbackContext);
            appId.signinWithResourceOwnerPassword(this.cordova.getActivity(),userName,password,appIdSampleAuthorizationListener);
             // LoginWidget loginWidget = appId.getLoginWidget();
            // loginWidget.launch(this.cordova.getActivity(),appIdSampleAuthorizationListener, null);
        }catch(Exception e){
            callbackContext.error(e.toString());
        }
    }
}