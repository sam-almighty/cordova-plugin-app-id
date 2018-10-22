/*
   Licensed Materials - Property of IBM

   (C) Copyright 2016 IBM Corp.

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
var exec = require('cordova/exec');

var failure = function(res) {
	alert(res);
}

var APPID = {
		
	/**
	 * Initializes MFPPush instance. This is required for the client application to connect to MFPPush service with the right
	 * application context. This API should be called first before using other MFPPush APIs.
	 * 
	 * @param success callback
	 * @param failure callback 
	 * @param timeout request timeout in seconds
	 */
	initialize: function(success, failure, tenantId, region) {
		if (typeof tenantId !== "undefined" && typeof region !== "undefined" && typeof tenantId === "string" && typeof region === "string") {
			cordova.exec(success, failure, "AppIdPlugin", "initialize", [tenantId,region]);
		} else {
			cordova.exec(success, failure, "AppIdPlugin", "initialize", []);
		}
	},
	
	login: function(success, failure,userName,password) {
			cordova.exec(success, failure, "AppIdPlugin", "login",[userName,password]);
	},
	logout: function(success, failure) {
		
	}
};

module.exports = APPID;