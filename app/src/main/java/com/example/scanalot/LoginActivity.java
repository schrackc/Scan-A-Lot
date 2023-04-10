package com.example.scanalot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scanalot.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This class is used for the login Activity. It creates the login Activity and uses the activity_login layout. This will be used for
 * further handling of the data when the user has attempted to login. This class is responsible for handling user authentication through Firebase
 *
 * @author Andrew Hoffer
 * @Created 1/25/23
 * @Contributors Andrew Hoffer - 1/25/23 - Created the Activity
 * @Contributors Nick Downey - 3/1/23 - Added loading bar to login.
 */

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize loadingBar
        loadingBar = binding.loading;
        loadingBar.setVisibility(View.INVISIBLE);

        setContentView(binding.getRoot());
        setTitle("Scan A Lot");
        Button btn_login = binding.login;
        btn_login.setEnabled(true);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strUserEmail = binding.username.getText().toString().trim();
                String strUserPassword = binding.password.getText().toString().trim();
                if (validEmailPassWord()) {
                    loadingBar.setVisibility(View.VISIBLE);
                    login(strUserEmail, strUserPassword);
                }else
                {
                    Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Logs In the User Based on if their user email and password are correct.
     *
     * @param {string} p_strUserEmail    the user email string
     * @param {string} p_strUserPassword the user password
     */
    private void login(String p_strUserEmail, String p_strUserPassword) {
        mAuth.signInWithEmailAndPassword(p_strUserEmail, p_strUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to Log In", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Validates the users email and password
     *
     * @return validates if the email and password fields have been filled out correctly.
     */
    private boolean validEmailPassWord() {
        boolean isTrue = true;
        EditText userEmail = binding.username;
        EditText userPass = binding.password;
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.getText()).matches()) {
            isTrue = false;
        }
        if (userPass.getText().length() < 6) {
            isTrue = false;
        }


        return isTrue;
    }

}