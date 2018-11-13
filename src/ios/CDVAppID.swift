/*
 Copyright 2015 IBM Corp.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import Foundation
import UIKit
import BMSCore
import IBMCloudAppID

@objc(CDVAppID) @objcMembers class CDVAppID : CDVPlugin {

    /*
     * Initialize the SDK with appGUID and IBM Cloud Region
     */
    func initialize(_ command: CDVInvokedUrlCommand) {
        self.commandDelegate!.run(inBackground: {
                guard let backendGUID  = command.arguments[0] as? String else {
                    let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Invalid tenantId")
                    // call callback
                    self.commandDelegate!.send(pluginResult, callbackId:command.callbackId)
                    return
                }
                guard let region  = command.arguments[1] as? String else {
                    let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Invalid region")
                    // call callback
                    self.commandDelegate!.send(pluginResult, callbackId:command.callbackId)
                    return
                }
                
                let bmsclient = BMSClient.sharedInstance
                bmsclient.initialize(bluemixRegion: region)
                let appid = AppID.sharedInstance
                appid.initialize(tenantId: backendGUID, region: region)
                let appIdAuthorizationManager = AppIDAuthorizationManager(appid:appid)
                bmsclient.authorizationManager = appIdAuthorizationManager
                TokenStorageManager.sharedInstance.initialize(tenantId: backendGUID)
                let refreshToken = TokenStorageManager.sharedInstance.loadStoredRefreshToken()
                if (refreshToken != nil) {
                    AppID.sharedInstance.signinWithRefreshToken(refreshTokenString: refreshToken!, tokenResponseDelegate: SigninDelegate(command, self.commandDelegate))
                } else {
                    self.loginWidget(command)
            }
        })
    }
    
    
    /*
     * Login the SDK with username and password
     */
    func login(_ command: CDVInvokedUrlCommand) {
        self.commandDelegate!.run(inBackground: {
                guard let username  = command.arguments[0] as? String else {
                    let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Invalid username")
                    // call callback
                    self.commandDelegate!.send(pluginResult, callbackId:command.callbackId)
                    return
                }
                guard let password  = command.arguments[1] as? String else {
                    let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Invalid password")
                    // call callback
                    self.commandDelegate!.send(pluginResult, callbackId:command.callbackId)
                    return
                }
                AppID.sharedInstance.signinWithResourceOwnerPassword(username: username, password: password, tokenResponseDelegate: SigninDelegate(command, self.commandDelegate))
        } ) }
    
    
    /*
     * Login the SDK using Facebook or Google Accounts
     */
    func loginWidget(_ command: CDVInvokedUrlCommand) {
        self.commandDelegate!.run(inBackground: {
            let token = TokenStorageManager.sharedInstance.loadStoredToken()
            AppID.sharedInstance.loginWidget?.launch(accessTokenString: token, delegate:SigninDelegate(command, self.commandDelegate))
        } ) }
    
    func application(_ application: UIApplication,_ open: URL, _ options: [UIApplicationOpenURLOptionsKey : Any])  -> Bool {
        return AppID.sharedInstance.application(application, open: open, options: options)
    }
}
