package felix.chen.ib_carpool.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ib_carpool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

import felix.chen.ib_carpool.User.Parent;
import felix.chen.ib_carpool.User.Student;
import felix.chen.ib_carpool.User.Teacher;
import felix.chen.ib_carpool.User.User;

/**
 * This class allows users to sign up with their email
 *
 * @author Felix Chen
 * @version 1.0
 */

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    EditText nameInput, emailInput, passwordInput;

    User user;

    String userType;

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.nameInputEditText);
        emailInput = findViewById(R.id.emailInputEditText);
        passwordInput = findViewById(R.id.passwordInputEditText);

        spinner = findViewById(R.id.userTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.userTypes, R.layout.type_row_view);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * Allows the user to sign up by filling in their email, name,
     * and password with at least 6 characters
     *
     * @param v on button "sign up" clicked
     */
    public void signUp(View v)
    {

        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        System.out.println(String.format("Sign Up - Email: %s, Password: %s", email, password));

        if (!name.equals("") && !email.equals("") && !password.equals(""))
        {
            try
            {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task ->
                        {
                            if (task.isSuccessful())
                            {
                                Log.d("SIGN UP", "createUserWithEmail:success");
                                FirebaseUser currUser = mAuth.getCurrentUser();
                                String uid = currUser.getUid();

                                if (userType.equals("student"))
                                {
                                    User student = new Student(uid, name, email, userType);
                                    user = student;
                                    firestore.collection("/users")
                                            .document(uid).set(student);
                                }
                                else if (userType.equals("teacher"))
                                {
                                    User teacher = new Teacher(uid, name, email, userType);
                                    user = teacher;
                                    firestore.collection("/users")
                                            .document(uid).set(teacher);
                                }
                                else if (userType.equals("parent"))
                                {
                                    User parent = new Parent(uid, name, email, userType);
                                    user = parent;
                                    firestore.collection("/users")
                                            .document(uid).set(parent);
                                }

                                updateUI(currUser);
                            }
                            else
                            {
                                Log.w("SIGN UP", "createUserWithEmail:failure",
                                        task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
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
     * Go to UserProfileActivity
     *
     * @param currUser User object
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
     * Go to SignInActivity
     *
     * @param v button click
     */
    public void goSignInActivity(View v)
    {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String text = "role: " + adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

        if (i == 0)
        {
            userType = "student";
        }
        else if (i == 1)
        {
            userType = "teacher";
        }
        else if (i == 2)
        {
            userType = "parent";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        userType = "student";
    }
}