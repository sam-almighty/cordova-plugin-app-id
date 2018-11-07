<!---
   Licensed Materials - Property of IBM

   (C) Copyright 2016 IBM Corp.

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->

# IBM App Id Cordova Plugin

Cordova Plugin for the IBM Cloud AppID

## Installation

### Installing necessary libraries

You should already have Node.js/npm and the Cordova package installed. If you don't, you can download and install Node from [https://nodejs.org/en/download/](https://nodejs.org/en/download/).

The Cordova library is also required to use this plugin. You can find instructions to install Cordova and set up your Cordova app at [https://cordova.apache.org/#getstarted](https://cordova.apache.org/#getstarted).

### Creating a Cordova application

Run the following commands to create a new Cordova application. Alternatively you can use an existing application as well. 

	```
	$ cordova create {appName}
	$ cd {appName}
	```

### Adding Cordova platforms

Run the following commands according to which platform you want to add to your Cordova application

```Bash
cordova platform add ios

cordova platform add android

cordova platform add browser
```

### Editing config.xml

1. Edit `config.xml` file and set the desired application name in the `<name>` element instead of a default HelloCordova.

	
2. Continue editing `config.xml`. Update the `<platform name="android">` element with a minimum and target SDK versions as shown in the code snippet below.

	```XML
	<platform name="android">
		<preference name="android-minSdkVersion" value="15" />
		<preference name="android-targetSdkVersion" value="23" />
		// other properties
	</platform>
	```

	> The minSdkVersion should be above 15.
	
	> The targetSdkVersion should always reflect the latest Android SDK available from Google.

### Adding the Cordova plugin



#### Updating your client application to use the Push SDK

### Configuring Your iOS Development Environment



### Configuring Your Android Development Environment



### Configuring Your browser Development Environment




## Usage

The following AppID Javascript functions are available:

Javascript Function | Description
--- | ---

**Android (Native)**
The following native Android function is available.

 Android function | Description
--- | ---


## Examples

