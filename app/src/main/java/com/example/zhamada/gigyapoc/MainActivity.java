package com.example.zhamada.gigyapoc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gigya.socialize.GSKeyNotFoundException;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;
import com.gigya.socialize.android.GSAPI;
import com.gigya.socialize.android.GSPluginFragment;
import com.gigya.socialize.android.GSSession;
import com.gigya.socialize.android.event.GSDialogListener;
import com.gigya.socialize.android.event.GSPluginListener;
import com.gigya.socialize.android.event.GSSocializeEventListener;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity implements GSPluginListener, GSDialogListener, GSSocializeEventListener{
    private  Button mButtonLogin;
    private String sUserProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GSAPI.getInstance().initialize(this, "3_YLloYeMGvrKmkWNFfHZsyN_r1PyQ4jOwRRUSIB64oc8uzD2J0SzeC3A3f_LNsG3F");
        GSAPI.getInstance().setSocializeEventListener(this);
        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mButtonLogin = (Button) findViewById(R.id.btnLogin);
        mButtonLogin.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                checkSession();
            }
        });

    }

    /*Gigya Setup
    //1-Include Gigya SDK under libs folder
    //2-add to manifest:
    // <uses-permission android:name="android.permission.INTERNET" />
        <activity
            android:name="com.gigya.socialize.android.ui.HostActivity"
            android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />
                <activity
            android:name="com.gigya.socialize.android.login.providers.WebLoginActivity"
            android:allowTaskReparenting="true"
            android:launchMode="singleTask"
            android:theme="@android:style/Theme.Translucent.NoTitleBar">
                    <intent-filter>
                        <action android:name="android.intent.action.VIEW" />

                        <category android:name="android.intent.category.DEFAULT" />
                        <category android:name="android.intent.category.BROWSABLE" />

                        <data
            android:host="gsapi"
            android:scheme="com.example.zhamada.gigyapoc" />
                    </intent-filter>
                </activity>
     //3- Add Google support:
        a- Google play index https://developer.android.com/studio/write/app-link-indexing.html
        b- dependency: compile "com.google.android.gms:play-services-auth:$PLAY_SERVICES_VERSION"
        c-client oauth: 1062036621303-ehfl6gorlp40l1fgmoqdujqr4dg6uu4j.apps.googleusercontent.com
        d-Gigya provider enabled

     //4-Facebook:
     a-compile 'com.facebook.android:facebook-android-sdk:4.13.2'
        import com.facebook.FacebookSdk;
        import com.facebook.appevents.AppEventsLogger;
     b- onCreate() add
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
     c-Create APP ID on Facebook developer console
     d-Enable and setup Facebook on Gigya console
     e- add string.xml:
        <string name="facebook_app_id">126046541276320</string>
        <string name="fb_login_protocol_scheme">fb126046541276320</string>
     f- Manifest add:
    <activity
            android:name="com.facebook.FacebookActivity"
            android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

        <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id" />

        <activity
            android:name="com.facebook.CustomTabActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="@string/fb_login_protocol_scheme" />
            </intent-filter>
        </activity>

     */
    public void checkSession() {
        GSSession session = GSAPI.getInstance().getSession();
        if (session != null && session.isValid()) {
            Log.d("Gigya session", "session.isValid() true");
            getUserAccountDetails();

        } else {
            Log.d("Gigya session", "session.isValid() false");
            callGigyaLoggingPlugin();
        }
    }


    public void callGigyaLoggingPlugin()
        {
        Log.d("callGigyaLoggingPlugin", "show pluginDialog");
        //TODO askForPermissions();
        /*
        GSObject params = new GSObject();
        params.put("categoryID", "Notify NYC Login");
        params.put("streamID", "1");
        params.put("captionText", "Notify NYC Login");
        GSAPI.getInstance().showPluginDialog("comments.commentsUI", params, null, null);
        */
        GSObject params = new GSObject();
        //params.put("screenSet", "Default-RegistrationLogin");
        params.put("screenSet", "Craig-RegistrationLogin");
        //params.put("captionText", "Notify NYC Login");
        //
        // params.put("screenSet", "Default-LinkAccounts");
        GSAPI.getInstance().showPluginDialog("accounts.screenSet", params, this, this);

        //GSAPI.getInstance().showLoginUI(params,null,null);
    }

    public void getUserAccountDetails(){

        Log.d("callGigyaLoggingPlugin","getUserAccountDetails()");
        GSObject params = new GSObject();
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
                try {
                    if (response.getData().getString("statusReason").equalsIgnoreCase("OK")) { // SUCCESS! response status = OK
                        parseUserAccountDetails(response.getData());
                        startHostAppMainAcitivity();
                        //if (user != null) {
                        //sharedData.setUser(user);
                        //startHostAppMainAcitivity();
                        //}
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        String methodName = "accounts.getAccountInfo";
        GSAPI.getInstance().sendRequest(methodName, params, resListener, null);
    }
    private void parseUserAccountDetails(GSObject data) {
        Log.d("parseUserAccountDetails","data: "+ data.toString());
        //try {
            /*
            {"UID":"ac4f1ae0c065462099a7044d147ee314",
            "UIDSignature":"qvgp1FxyaxIXTHfKXbossM7y9So=",
            "callId":"7d8c3388dabd47dab6e7146e4dbbd3b8",
            "created":"2017-04-24T19:49:34.298Z",
            "createdTimestamp":1493063374298,
            "data":{"terms":true},
            "errorCode":0,
            "isActive":true,
            "isLockedOut":false,
            "isRegistered":true,
            "isVerified":true,
            "lastLogin":"2017-05-08T14:25:10.496Z",
            "lastLoginTimestamp":1494253510496,
            "lastUpdated":"2017-05-08T14:00:33.351Z",
            "lastUpdatedTimestamp":1494252033351,
            "loginProvider":"site",
            "oldestDataUpdated":"2017-04-24T19:49:34.342Z",
            "oldestDataUpdatedTimestamp":1493063374342,
            "profile":{"email":"Zhamada@doitt.nyc.gov","gender":"m","lastName":"Hamada","photoURL":"https:\/\/graph.facebook.com\/v2.5\/103436896896682\/picture?type=large","profileURL":"https:\/\/www.facebook.com\/app_scoped_user_id\/103436896896682\/","thumbnailURL":"https:\/\/graph.facebook.com\/v2.5\/103436896896682\/picture?type=square"},"registered":"2017-04-24T19:49:34.405Z","registeredTimestamp":1493063374405,"signatureTimestamp":"1494254163","socialProviders":"facebook,site","statusCode":200,"statusReason":"OK","time":"2017-05-08T14:36:03.565Z","verified":"2017-05-03T14:42:14.594Z","verifiedTimestamp":1493822534594}
             */
            String sFirstName;
            String slastName;
            String semail;
            String slastLogin;
            String sloginProvider;
            String sUID;
            try {
                sFirstName = data.getObject("profile").getString("firstname") ;
            }catch(Exception e){
                sFirstName="NA";
            }
            try {
                slastName = data.getObject("profile").getString("lastName") ;
            }catch(Exception e){
                slastName="NA";
            }
            try {
                semail = data.getObject("profile").getString("email") ;
            }catch(Exception e){
                semail="NA";

            }
            try {
                slastLogin = data.getString("lastLogin") ;
            }catch(Exception e){
                slastLogin="NA";
            }
            try {
                sloginProvider = data.getString("loginProvider") ;
            }catch(Exception e){
                sloginProvider="NA";
            }
            try {
                sUID = data.getString("UID") ;
            }catch(Exception e){
                sUID="NA";
            }

            //String displayName = data.getObject("profile").getString("firstname") + " " + data.getObject("profile").getString("lastName");
            this.sUserProfile="First Name: " + sFirstName + "\n" + "Last Name: " + slastName + "\n" + "Email: " + semail + "\n" + "Last Login: " + slastLogin + "\n" + "Login Provider: " + sloginProvider;
            this.sUserProfile= this.sUserProfile + "\n" + "UID: " + sUID;
            //String imgUrl = data.getObject("profile").getStr  ing(GIGYA_LARGE_PROFILE_PIC_KEY);
            // return new User(displayName, imgUrl);
            // } catch (GSKeyNotFoundException e) {
            //    e.printStackTrace();
            // }

    }
    public void startHostAppMainAcitivity(){
        Intent oIntent;
        oIntent = new Intent(MainActivity.this, HostAppMainAcitivity.class);
        oIntent.putExtra("USER_PROFILE",this.sUserProfile);
        startActivity(oIntent);
    }

    @Override
    public void onDismiss(boolean b, GSObject gsObject) {
        Log.d("POCMainActivity", "onDismiss()");
    }

    @Override
    public void onLoad(GSPluginFragment gsPluginFragment, GSObject gsObject) {
        Log.d("POCMainActivity", "onLoad():  " + gsObject.toString());

    }

    @Override
    public void onError(GSPluginFragment gsPluginFragment, GSObject gsObject) {
        Log.d("POCMainActivity", "onError():  " + gsObject.toString());
    }

    @Override
    public void onEvent(GSPluginFragment gsPluginFragment, GSObject gsObject) {
        Log.d("POCMainActivity", "onEvent():  " + gsObject.toString());
    }

    @Override
    public void onLogin(String s, GSObject gsObject, Object o) {
        Log.d("POCMainActivity", "onLogin():  " + gsObject.toString());
        getUserAccountDetails();
    }

    @Override
    public void onLogout(Object o) {
        Log.d("POCMainActivity", "onLogout():  " + o.toString());
    }

    @Override
    public void onConnectionAdded(String s, GSObject gsObject, Object o) {
        Log.d("POCMainActivity", "onConnectionAdded():  " + o.toString());
    }

    @Override
    public void onConnectionRemoved(String s, Object o) {
        Log.d("POCMainActivity", "onConnectionRemoved():  " + o.toString());
    }
}
