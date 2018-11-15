package com.example.mike.loginemailgooglefacebooktwiitter.auth;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.mike.loginemailgooglefacebooktwiitter.R;
import com.example.mike.loginemailgooglefacebooktwiitter.ui.SuperActivities.AuthenticatableActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class UserAuthorization {

    public static final int GOOGLE_REQUEST_CODE = 1;

    AuthenticatableActivity context;
    private CallbackManager mCallbackManager;

    public UserAuthorization(AuthenticatableActivity context) {
        this.context = context;
    }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }


    // Google sign in
    public void googleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("681590149120-4u1c4r2inc22pe13a2caj7g3qrfolbcj.apps.googleusercontent.com")
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(context, gso);

        Intent signInIntent = client.getSignInIntent();
        this.context.startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE);
    }


    // Email Password Sign In
    public void emailPassSignIn( String email, String password ){
        getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            context.onUserAuthenticated(getFirebaseAuth().getCurrentUser());
                        } else {
                            // Try to sign them up
                            context.onUserAuthenticationFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    //Email Password sign up
    public void emailPassSignUp( String email, String password ){
        getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            context.onUserAuthenticated( getFirebaseAuth().getCurrentUser());
                        } else {
                            context.onUserAuthenticationFailure( task.getException().getMessage() );
                        }
                    }
                });
    }

    //Facebook log in
    public void facebookSignIn(){
        FacebookSdk.setApplicationId("350548882175119");

        FacebookSdk.sdkInitialize(context);

        // Facebook Shit

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("__TAG__UserAuthorization.onSuccess" + loginResult.toString());
                context.firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                context.onUserAuthenticationFailure("Request Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                context.onUserAuthenticationFailure("Error: " + error.getMessage());
            }
        });

        ArrayList<String> permissions = new ArrayList<>();

        permissions.add( "email" );
        permissions.add( "public_profile" );

        LoginManager.getInstance().logInWithReadPermissions(context, permissions);
    }

    public void facebookCallback( int a, int b, Intent i ){
        mCallbackManager.onActivityResult( a,b,i );
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

}
