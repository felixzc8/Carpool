package felix.chen.ib_carpool.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ib_carpool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import felix.chen.ib_carpool.User.User;

/**
 * This activity allows the user to sign in to their account with their email and their password
 *
 * @author Felix Chen
 * @version 1.0
 */
public class SignInActivity extends AppCompatActivity
{

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    User user;

    EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailInput = findViewById(R.id.emailInputEditText);
        passwordInput = findViewById(R.id.passwordInputEditText);

    }

    @Override
    public void onStart()
    {
        super.onStart();
//         Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null)
        {
            String email = currentUser.getEmail();

            System.out.println(String.format("Current User - email: %s", email));
            updateUI(currentUser);
        }
    }

    /**
     * sign in to firebase with email and password
     *
     * @param v button click
     */

    public void signIn(View v)
    {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        System.out.println(String.format("Log In - Email: %s, Password: %s", email, password));

        if (!email.equals("") && !password.equals(""))
        {
            try
            {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task ->
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGN IN", "signInWithEmail:success");
                                FirebaseUser currUser = mAuth.getCurrentUser();
                                updateUI(currUser);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGN IN", "signInWithEmail:failure",
                                        task.getException());
                                Toast.makeText(SignInActivity.this,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * goes to UserProfileActivity if the user is valid
     *
     * @param currUser User object that is created on sign in
     */
    public void updateUI(FirebaseUser currUser)
    {
        if (currUser != null)
        {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Goes to the sign up activity
     *
     * @param v button click
     */
    public void goSignUpActivity(View v)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}