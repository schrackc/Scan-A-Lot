package com.example.scanalot;
import android.content.Intent;
import android.os.Bundle;
import com.example.scanalot.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.scanalot.databinding.ActivityLoginBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityLoginBinding.inflate(getLayoutInflater());
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

     setContentView(binding.getRoot());

        Button btn_login = binding.login;
        btn_login.setEnabled(true);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String strUserEmail = binding.username.getText().toString().trim();
               String strUserPassword = binding.password.getText().toString().trim();
                if(validEmailPassWord()) {

                    login(strUserEmail,strUserPassword);


                    }
            }
        });



    }

    /**
     *Logs In the User Based on if their user email and password are correct.
     * @param p_strUserEmail the user email string
     * @param p_strUserPassword the user password
     */
    private void login(String p_strUserEmail, String p_strUserPassword) {
    mAuth.signInWithEmailAndPassword(p_strUserEmail,p_strUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful())
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }else
            {
                Toast.makeText(LoginActivity.this,"Failed To Log In",Toast.LENGTH_LONG).show();
            }
        }
    });

    }

    /**
     *
     * @return validates if the email and password fields have been filled out correctly.
     */
    private boolean validEmailPassWord() {
        boolean isTrue = true;
        EditText userEmail = binding.username;
        EditText userPass = binding.password;
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail.getText()).matches())
        {
            userEmail.setError("Please Enter Valid Email Address");
            userEmail.requestFocus();
            isTrue = false;
        }
        if(userPass.getText().length() < 6)
        {
            userPass.setError("Enter Valid Password");
            userPass.requestFocus();
            isTrue = false;
        }



        return isTrue;
    }

}