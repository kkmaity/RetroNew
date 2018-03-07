package com.retrofit.retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mircea on 04.03.2018.
 */


public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0 ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button signInButton;
    private GoogleApiClient mGoogleApiClient;
    private Button signOutButton;
    private TextView nameTextView;
    private ImageView imageView;
    private EditText email;
    private EditText nume;
    private EditText user;
    private EditText idlocatie;
    String vtext;
    String vlocatie;
    String key;
    String poza;
    String url;
    String iduser;
    String montator;
    String loc;
    String adresamail;
    //  public static final String MY_PREFS_NAME = "preferences";
  //  private Drawer result = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtext = mySharedPreferences.getString("edittext_preference", "");
        vlocatie = mySharedPreferences.getString("idlocatie", "");
        key= mySharedPreferences.getString("token", "");
        // setTheme(android.R.style.Theme_Holo); // (for Android Built In Theme)

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signOutButton = (Button)findViewById(R.id.sign_out_button);
        nameTextView = (TextView)findViewById(R.id.name_text_view);
        email = (EditText)findViewById(R.id.et_email);
        nume = (EditText)findViewById(R.id.nume);
        user = (EditText)findViewById(R.id.user);
        user.setText(vtext);
        idlocatie = (EditText)findViewById(R.id.locatie);
        idlocatie.setText(vlocatie);
        email.setVisibility(View.GONE);
        nume.setVisibility(View.GONE);

        imageView = (ImageView)findViewById(R.id.foto);
        imageView.setImageResource(R.drawable.camera);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //   SignInButton.setVisibility(View.GONE);
                signOutButton.setVisibility(View.VISIBLE);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if(user.getDisplayName() != null)
                        nameTextView.setText("HI " + user.getDisplayName().toString());
                    email.setVisibility(View.VISIBLE);
                    email.setText(user.getEmail().toString());
                    adresamail = user.getEmail().toString();
                    nume.setVisibility(View.VISIBLE);
                    nume.setText(user.getDisplayName().toString());
                    poza =user.getPhotoUrl().toString();
                    Context context;
                    context = imageView.getContext();
                    Picasso.with(context).load(user.getPhotoUrl().toString()).resize(60, 60).into(imageView);

                    preiadate("http://www.floridaconstruct.eu/comenzi/test/drepturiuseri.php?mail="+adresamail);

                    url ="http://www.floridaconstruct.eu/comenzi/test/registercutoken.php?Token="+key+"&idmember="+iduser+"&poza="+poza+"&mail="+adresamail;
                    register(url);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                //        SignInButton.setVisibility(View.VISIBLE);
                                signOutButton.setVisibility(View.GONE);
                                nameTextView.setText(" ".toString());
                                email.setText(" ".toString());
                                imageView.setImageResource(R.drawable.camera);
                            }
                        });
            }
            // ..
        });

    }
    public void register(final String adresaurl) {

     //   new HttpHandler() {
      //      @Override
     //       public HttpUriRequest getHttpRequestMethod() {
     //           return new HttpGet(adresaurl);
     //       }
     //       @Override
     //       public ArrayList<String> onResponse(String result) {
     //           return null;
      //      }
     //   }.execute();
    };

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(SignInActivity.this,"Eroare la conectare!", Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void preiadate(final String adresaurl) {

     //   new HttpHandler() {
     //       @Override
     //       public HttpUriRequest getHttpRequestMethod() {
     //           return new HttpGet(adresaurl);
     //       }
     //       @Override
     //       public ArrayList<String> onResponse(String result) {

     //           try {
     //               JSONObject json = new JSONObject(result);
     //               JSONArray earthquakes = json.getJSONArray("earthquakes");

     //               for (int i = 0; i < earthquakes.length(); i++) {
     //                   JSONObject e = earthquakes.getJSONObject(i);


     //                   iduser = e.getString(("idEmployee"));
     //                   montator = e.getString(("montator"));
     //                   loc = e.getString(("locatie"));

    //                }

    //            } catch (JSONException e) {
    //                e.printStackTrace();
     //           }


     //           return null;
     //       }
    //    }.execute();
    };
    private void updateList() {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        user.setText(iduser);
        idlocatie.setText(loc);
        //   Toast.makeText(SignInActivity.this, "User " +iduser , Toast.LENGTH_SHORT).show();
        mySharedPreferences.edit().putString("edittext_preference", iduser.toString()).commit();
        mySharedPreferences.edit().putString("idmember",iduser.toString()).commit();
        mySharedPreferences.edit().putString("montator",montator.toString()).commit();
        mySharedPreferences.edit().putString("idlocatie",loc.toString()).commit();
        mySharedPreferences.edit().putString("mail",adresamail.toString()).commit();
        Toast.makeText(SignInActivity.this, "montator " +montator , Toast.LENGTH_SHORT).show();
        Toast.makeText(SignInActivity.this, "idlocatie " +loc , Toast.LENGTH_SHORT).show();
    };


}
