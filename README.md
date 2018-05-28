# FacebookLoginDemo
Android Facebook Login

Today we will learn how to integrate Facebook login in android applications. You must have seen Facebook login as an easier way to create account in many applications.


**Registering Your Facebook Application**

Login using your Facebook account to: https://developers.facebook.com/apps/.

**add the following dependencies in your app‘s build.gradle.**

`buildscript { 
repositories {
mavenCentral()
}
} `


**Use the latest version of the SDK dependency.**

`implementation 'com.facebook.android:facebook-android-sdk:4.31.0'
`

**Add your Facebook App ID**

The App ID present in the Facebook developers application dashboard needs to be added in the strings.xml in our Android Studio Project.

`<string name="facebook_app_id">894882580618030</string>

`
Enable internet permissions in the AndroidManifest.xml file by adding the following line in the manifest element.

`<uses-permission android:name="android.permission.INTERNET"/>`



Now add the following code inside the application tag in the Manifest.


__`<meta-data android:name="com.facebook.sdk.ApplicationId" 
        android:value="@string/facebook_app_id"/>
    
    <activity android:name="com.facebook.FacebookActivity"
        android:configChanges=
                "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
        android:label="@string/app_name" />
    <activity
        android:name="com.facebook.CustomTabActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <data android:scheme="@string/fb_login_protocol_scheme" />
        </intent-filter>
    </activity>__
`


