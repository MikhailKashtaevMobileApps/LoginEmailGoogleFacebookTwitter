package com.example.mike.loginemailgooglefacebooktwiitter.ui.SuperActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mike.loginemailgooglefacebooktwiitter.R;
import com.example.mike.loginemailgooglefacebooktwiitter.auth.UserAuthorization;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class AuthenticatableActivity extends AppCompatActivity {

    public static final String TAG = AuthenticatableActivity.class.getSimpleName()+"__TAG__";
    private UserAuthorization auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create firebase auth
        auth = new UserAuthorization(this);
    }


    // GOOGLE AUTHENTICATION
    /* ########################################## */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == UserAuthorization.GOOGLE_REQUEST_CODE){
            // Google authorization
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                firebaseAuthWithGoogle(task.getResult(ApiException.class));
                return;
            } catch (ApiException e) {
                onUserAuthenticationFailure(e.getMessage());
            }
        }
        auth.facebookCallback( requestCode, resultCode, data );
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onUserAuthenticated(auth.getFirebaseAuth().getCurrentUser());
                        } else {
                            onUserAuthenticationFailure( task.getException().getMessage() );
                        }
                    }
                });
    }

    /* ########################################## */



    // FACEBOOK LOGIN
    public void firebaseAuthWithFacebook(AccessToken token){
        System.out.println("AuthenticatableActivity.firebaseAuthWithFacebook"+TAG+ "handleFacebookAccessToken:" + token.toString());

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onUserAuthenticated(auth.getFirebaseAuth().getCurrentUser());
                        } else {
                            onUserAuthenticationFailure( task.getException().getMessage() );
                        }
                    }
                });

    }


    protected UserAuthorization getUserAuthorizationService(){
        return this.auth;
    }

    public void onUserAuthenticated( FirebaseUser user ){
        Log.d(TAG, "onUserAuthenticated: ");
    }

    public void onUserAuthenticationFailure( String error ){
        Log.d(TAG, "onUserAuthenticationFailure: ");
    }
}
