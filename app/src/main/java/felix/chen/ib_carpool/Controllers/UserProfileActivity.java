package felix.chen.ib_carpool.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ib_carpool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

import felix.chen.ib_carpool.User.User;
import felix.chen.ib_carpool.Vehicle.Car;
import felix.chen.ib_carpool.Vehicle.Helicopter;
import felix.chen.ib_carpool.Vehicle.Tank;
import felix.chen.ib_carpool.Vehicle.Vehicle;

/**
 * This class displays the user's information. It also shows the user's vehicles in a recyclerview.
 *
 * @author Felix Chen
 * @version 1.0
 */

public class UserProfileActivity extends AppCompatActivity
{

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    FirebaseUser mUser;

    ArrayList<Vehicle> userVehicles;

    RecyclerView userVehiclesRecView;
    UserProfileAdapter adapter;
    UserProfileAdapter.RecyclerViewClickListener listener;

    TextView nameText, userTypeText, carsOwnedText, emailText;
    Button bookARideButton;

    User user;

    Vehicle currRide;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        nameText = findViewById(R.id.nameTextView);
        userTypeText = findViewById(R.id.userTypeTextView);
        carsOwnedText = findViewById(R.id.carsOwnedTextView);
        emailText = findViewById(R.id.emailTextView);
        bookARideButton = findViewById(R.id.bookARideButton);

        userVehicles = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        getUser();

        setOnClickListener();
        userVehiclesRecView = findViewById(R.id.userVehiclesRecView);
        adapter = new UserProfileAdapter(userVehicles, listener);
        userVehiclesRecView.setAdapter(adapter);
        userVehiclesRecView.setLayoutManager(new LinearLayoutManager(
                UserProfileActivity.this));
    }

    /**
     * Gets user from firebase and sets on screen user info
     * calls getUserVehicles and checkIfBooked
     */
    public void getUser()
    {
        try
        {
            firestore.collection("/users").document(mUser.getUid()).get()
                    .addOnCompleteListener(task ->
                    {
                        DocumentSnapshot ds = task.getResult();
                        if (task.isSuccessful())
                        {
                            user = ds.toObject(User.class);
                            Log.d("USER OBJECT", "user name: " + user.getName());
                            nameText.setText(user.getName());
                            userTypeText.setText(user.getUserType());
                            carsOwnedText.setText("cars owned: " + user.getOwnedVehicles().size());
                            emailText.setText(user.getEmail());

                            getUserVehicles();

                            System.out.println(user.getOwnedVehicles());

                            checkIfBooked();
                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "error getting user", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets all vehicles belonging to the user from firebase and adds it to the user's arraylist
     * of owned vehicles
     */
    public void getUserVehicles()
    {

        firestore.collection("vehicles")
                .whereEqualTo("ownerID", user.getUid())
                .get()
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot qds : task.getResult())
                        {
                            Vehicle vehicle = qds.toObject(Vehicle.class);

                            if (vehicle.getVehicleType().equals("car"))
                            {
                                Vehicle car = qds.toObject(Car.class);
                                userVehicles.add(car);
                            }
                            else if (vehicle.getVehicleType().equals("helicopter"))
                            {
                                Vehicle helicopter = qds.toObject(Helicopter.class);
                                userVehicles.add(helicopter);
                            }
                            else if (vehicle.getVehicleType().equals("tank"))
                            {
                                Vehicle tank = qds.toObject(Tank.class);
                                userVehicles.add(tank);
                            }
                        }
                        System.out.println("VEHICLES: " + userVehicles);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Log.d("Error getting documents: ", "" + task.getException());
                        Toast.makeText(this, "error getting vehicles",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setOnClickListener()
    {
        listener = (v, position) ->
        {
            Intent intent = new Intent(getApplicationContext(), VehicleProfileActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("vehicle", userVehicles.get(position));
            startActivity(intent);
        };
    }

    /**
     * Checks if the user has already booked a ride, changes "book ride" button to "current ride" if
     * a ride is already booked
     */
    public void checkIfBooked()
    {
        if (!user.getCurrRideID().equals(""))
        {
            bookARideButton.setText("current ride");
            bookARideButton.setTextColor(getResources().getColor(R.color.black));
            bookARideButton.setBackgroundColor(getResources().getColor(R.color.cream));
            firestore.collection("vehicles")
                    .document(user.getCurrRideID())
                    .get()
                    .addOnCompleteListener(task ->
                    {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot ds = task.getResult();
                            currRide = ds.toObject(Vehicle.class);
                        }
                        else
                        {
                            Log.d("error checking if booked", "" + task.getException());
                            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * If the user has a booked ride, go to vehicleProfileActivity for the booked ride, otherwise
     * go to vehiclesInfoActivity
     *
     * @param v button click
     */
    public void bookRide(View v)
    {
        if (!user.getCurrRideID().equals(""))
        {
            Intent intent = new Intent(this, VehicleProfileActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("vehicle", currRide);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, VehiclesInfoActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            this.finish();
        }
    }

    /**
     * Goes to addVehicleActivity
     *
     * @param v button click
     */
    public void goAddVehicles(View v)
    {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /**
     * Signs user out of the app, go to signInActivity
     *
     * @param v button click
     */
    public void signOut(View v)
    {
        mAuth.signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        this.finish();
    }
}