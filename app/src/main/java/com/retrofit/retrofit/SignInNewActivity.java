package com.retrofit.retrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by kamal on 03/07/2018.
 */

public class SignInNewActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 111;
    private String vtext;
    private String vlocatie;
    private String key;
    private FrameLayout googleLoginFrame;
    private TextView btn_sign_in;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_new_activity);
        googleLoginFrame=(FrameLayout)findViewById(R.id.googleLoginFrame);
        googleLoginFrame.setOnClickListener(this);
        final SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtext = mySharedPreferences.getString("edittext_preference", "");
        vlocatie = mySharedPreferences.getString("idlocatie", "");
        key= mySharedPreferences.getString("token", "");
        initGoogleSignIn();

    }
    private void initGoogleSignIn() {
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.googleLoginFrame:
                signIn();
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                String id= account.getId();
                String fullName= account.getDisplayName();
                Uri imgUrl = account.getPhotoUrl();
                String email=account.getEmail();
                //commonAPI.showToastMsg("Thanks..."+fullName);
                if (imgUrl!=null&&id!=null&&fullName!=null)
                    doSocialLogin(id,fullName,imgUrl.toString(),email);


            } else {
                showToastMsg("Something went wrong...");
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void doSocialLogin(String id, String fullName, String imgUrl, String email) {
        showToastMsg(id+email+fullName);
    }

    private void showToastMsg(String s) {
        Toast.makeText(SignInNewActivity.this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
