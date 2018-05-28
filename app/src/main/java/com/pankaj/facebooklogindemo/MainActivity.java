package com.pankaj.facebooklogindemo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    private LoginButton loginButton;
    CallbackManager callbackManager;
    private static final String USER_POSTS = "user_posts";
    private static final String USER_PUBLIC_PROFILE= "public_profile";

    TextView tvAppID,tvName,tvEmail,tvGender;
    String first_name="",last_name="",email="",id="",image_url="";

    ImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFbKeyHash("com.pankaj.facebooklogindemo");


        initView();
       initFBSDK();


        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {
            Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imgUser);
            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());

            //Using Graph API
            getUserProfile(AccessToken.getCurrentAccessToken());
        }


        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    tvName.setText(null);
                    tvEmail.setText(null);
                    imgUser.setImageResource(0);
                    Toast.makeText(getApplicationContext(),"User Logged Out.",Toast.LENGTH_LONG).show();
                }
            }
        };
        fbTracker.startTracking();

    }

    private void initFBSDK() {
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList(EMAIL,USER_PUBLIC_PROFILE));
        // If you are using in a fragment, call loginButton.setFragment(this);


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        // App code
                        //loginResult.getAccessToken();
                        //loginResult.getRecentlyDeniedPermissions()
                        //loginResult.getRecentlyGrantedPermissions()
                        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                        Log.d("API123", loggedIn + " ??");

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(MainActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();


                    }
                });
    }


    private void initView() {

        tvAppID = findViewById(R.id.tvAppID);
        imgUser = findViewById(R.id.imgUser);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvGender = findViewById(R.id.tvGender);
        loginButton = (LoginButton) findViewById(R.id.login_button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getFbKeyHash(String packageName){
        PackageInfo info;
        try {

            info = getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashString = new String(Base64.encode(md.digest(), 0));
                System.out.println("App KeyHash : " + hashString);
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {

                            if ( object.getString("first_name")!=null)
                            first_name = object.getString("first_name");

                            if ( object.getString("last_name")!=null)
                                last_name     = object.getString("last_name");

                            if ( object.has("email"))
                                email   = object.getString("email");

                            if ( object.getString("id")!=null){

                                id    = object.getString("id");
                                image_url  = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            }


                            tvName.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                            tvEmail.setText(email);
                            tvAppID.setText(id);

                            if (!image_url.isEmpty())
                                Picasso.get().load(image_url).into(imgUser);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
