//
//  SigninDelegate.swift
//  app ID Plugin
//
//

import UIKit
import IBMCloudAppID
import BMSCore


class SigninDelegate: AuthorizationDelegate {
    let callback: CDVInvokedUrlCommand
    let commandDelegate: CDVCommandDelegate

    public init(_ callback: CDVInvokedUrlCommand, _ commandDelegate: CDVCommandDelegate) {
        self.callback = callback
        self.commandDelegate = commandDelegate
    }

    public func onAuthorizationSuccess(accessToken: AccessToken?,
                                       identityToken: IdentityToken?,
                                       refreshToken: RefreshToken?,
                                       response:Response?) {
        guard accessToken != nil || identityToken != nil else {
            return
        }

        if accessToken!.isAnonymous {
            TokenStorageManager.sharedInstance.storeToken(token: accessToken!.raw)
        } else {
            TokenStorageManager.sharedInstance.clearStoredTokens()
        }

        if (refreshToken != nil) {
            TokenStorageManager.sharedInstance.storeRefreshToken(token: refreshToken!.raw)
        }
        TokenStorageManager.sharedInstance.storeUserId(userId: accessToken!.subject)
        
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "")
        // call success callback
        self.commandDelegate.send(pluginResult, callbackId: self.callback.callbackId)

    }

    public func onAuthorizationCanceled() {
        print("cancel")
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Authorization Cancelled")
        // call callback
        self.commandDelegate.send(pluginResult, callbackId:self.callback.callbackId)
    }

    public func onAuthorizationFailure(error: AuthorizationError) {
        print("Authorization failure: "+error.localizedDescription)
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Authorization failure: "+error.localizedDescription)
        // call callback
        self.commandDelegate.send(pluginResult, callbackId:self.callback.callbackId)
       
    }

    static func navigateToLandingView(navigationController: UINavigationController?) {
     
    }
}
