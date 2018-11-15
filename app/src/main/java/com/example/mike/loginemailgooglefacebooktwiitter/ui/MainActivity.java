package com.example.mike.loginemailgooglefacebooktwiitter.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mike.loginemailgooglefacebooktwiitter.R;
import com.example.mike.loginemailgooglefacebooktwiitter.ui.SuperActivities.AuthenticatableActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AuthenticatableActivity implements MainContract.View {

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById( R.id.etEmail );
        password = findViewById( R.id.etPass );
    }

    public void authEmailPassLogin(View view) {
        getUserAuthorizationService().emailPassSignIn( email.getText().toString(),
                password.getText().toString());
    }

    public void authEmailPassSignUp(View view) {
        getUserAuthorizationService().emailPassSignUp( email.getText().toString(),
                password.getText().toString());

    }

    public void authGoogleLogin(View view) {
        getUserAuthorizationService().googleSignIn();
    }

    public void authFacebookLogin(View view) {
        getUserAuthorizationService().facebookSignIn();
    }

    public void authTwitterLogin(View view) {

    }

    public void authSignOut(View view) {
        getUserAuthorizationService().signOut();
    }

    public void authCheckStatus(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if ( user == null ){
            Toast.makeText( this, "Already signed out", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText( this, "User authenticated:"+user.getDisplayName(), Toast.LENGTH_SHORT ).show();
    }


    @Override
    public void onUserAuthenticated(FirebaseUser user) {
        Toast.makeText( this, "User authenticated:"+user.getDisplayName(), Toast.LENGTH_SHORT ).show();
        super.onUserAuthenticated(user);
    }

    @Override
    public void onUserAuthenticationFailure(String error) {
        Toast.makeText( this, "Authentication Failed: e="+error, Toast.LENGTH_SHORT ).show();
        super.onUserAuthenticationFailure(error);
    }
}
