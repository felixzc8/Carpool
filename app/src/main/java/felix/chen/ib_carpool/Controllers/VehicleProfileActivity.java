package felix.chen.ib_carpool.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ib_carpool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import felix.chen.ib_carpool.User.User;
import felix.chen.ib_carpool.Vehicle.Vehicle;

/**
 * Displays information for a vehicle, shows owner options such as opening/closing vehicle if the
 * user owns the vehicle, otherwise show rider options such as booking a ride
 *
 * @author Felix Chen
 * @version 1.0
 */
public class VehicleProfileActivity extends AppCompatActivity
{
    TextView vehicleTypeTV, modelTV, driverTV, capacityTV, seatsLeftTV, priceTV;
    ToggleButton openButton, bookRideButton;

    FirebaseFirestore firestore;

    User user;
    Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        vehicle = (Vehicle) intent.getSerializableExtra("vehicle");

        vehicleTypeTV = findViewById(R.id.vehicleTypeTV);
        modelTV = findViewById(R.id.modelTV);
        driverTV = findViewById(R.id.driverTV);
        capacityTV = findViewById(R.id.capacityTV);
        seatsLeftTV = findViewById(R.id.seatsLeftTV);
        priceTV = findViewById(R.id.priceTV);
        bookRideButton = findViewById(R.id.bookRideButton);
        openButton = findViewById(R.id.openButton);

        vehicleOpenClosed();
        bookRide();
        setUp();
    }

    /**
     * Sets up the screen depending on if the user owns the vehicle
     */
    public void setUp()
    {
        String priceS = vehicle.getBasePrice() + " $";

        vehicleTypeTV.setText(vehicle.getVehicleType());
        modelTV.setText(vehicle.getModel());
        priceTV.setText(priceS);

        if (vehicle.getOwnerID().equals(user.getUid()))
        {
            bookRideButton.setVisibility(View.GONE);
            bookRideButton.setEnabled(false);

            driverTV.setVisibility(View.INVISIBLE);
            seatsLeftTV.setVisibility(View.INVISIBLE);

            String capacityS = "Capacity: " + vehicle.getCapacity();
            capacityTV.setText(capacityS);
        }
        else
        {
            openButton.setVisibility(View.GONE);
            openButton.setEnabled(false);

            capacityTV.setVisibility(View.INVISIBLE);

            String driverS = "Driver: " + vehicle.getOwnerName();
            String seatsLeftS = vehicle.getCapacity() + " seats left";

            driverTV.setText(driverS);
            seatsLeftTV.setText(seatsLeftS);
        }
    }

    /**
     * Checks if the vehicle is open or closed if the user is the owner of the vehicle
     * If the user opens or closes the vehicle, update the firebase data
     */
    public void vehicleOpenClosed()
    {
        if (vehicle.getOpen())
        {
            openButton.setChecked(true);
            creamStyle(openButton);
        }
        else
        {
            openButton.setChecked(false);
            blackStyle(openButton);
        }

        openButton.setOnCheckedChangeListener((compoundButton, b) ->
        {
            if (openButton.isChecked())
            {
                vehicle.setOpen(true);
                firestore.collection("vehicles")
                        .document(vehicle.getVehicleID())
                        .set(vehicle)
                        .addOnCompleteListener(task ->
                        {
                            creamStyle(openButton);
                            Toast.makeText(VehicleProfileActivity.this, "ride open",
                                    Toast.LENGTH_SHORT).show();
                        });

            }
            else
            {
                for (String userID : vehicle.getRidersUIDs())
                {
                    firestore.collection("users")
                            .document(userID)
                            .get()
                            .addOnCompleteListener(task ->
                            {
                                if (task.isSuccessful())
                                {
                                    DocumentSnapshot ds = task.getResult();
                                    User riderUser = ds.toObject(User.class);
                                    riderUser.setCurrRideID("");
                                    firestore.collection("users")
                                            .document(riderUser.getUid())
                                            .set(riderUser);
                                }
                                else
                                {
                                    Log.d("open/close error", "" + task.getException());
                                }
                            });
                }
                vehicle.setOpen(false);
                vehicle.setRidersUIDs(new ArrayList<>());
                vehicle.setSeatsLeft(vehicle.getCapacity());
                firestore.collection("vehicles")
                        .document(vehicle.getVehicleID())
                        .set(vehicle)
                        .addOnCompleteListener(task ->
                        {
                            if (task.isSuccessful())
                            {
                                blackStyle(openButton);
                                Toast.makeText(VehicleProfileActivity.this,
                                        "ride closed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    /**
     * Books the ride for the user, updates the button style and updates the firestore database
     */
    public void bookRide()
    {
        for (String UID : vehicle.getRidersUIDs())
        {
            if (UID.equals(user.getUid()))
            {
                bookRideButton.setChecked(true);
                creamStyle(bookRideButton);

            }
            else
            {
                bookRideButton.setChecked(false);
                blackStyle(bookRideButton);
            }
        }

        bookRideButton.setOnCheckedChangeListener((compoundButton, b) ->
        {
            if (bookRideButton.isChecked())
            {
                vehicle.getRidersUIDs().add(user.getUid());
                vehicle.setSeatsLeft(vehicle.getCapacity() - vehicle.getRidersUIDs().size());
                firestore.collection("vehicles")
                        .document(vehicle.getVehicleID())
                        .set(vehicle)
                        .addOnCompleteListener(task ->
                        {
                            if (task.isSuccessful())
                            {
                                user.setCurrRideID(vehicle.getVehicleID());
                                firestore.collection("users")
                                        .document(user.getUid())
                                        .set(user);

                                creamStyle(bookRideButton);
                                Toast.makeText(VehicleProfileActivity.this,
                                        "Ride booked", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                vehicle.getRidersUIDs().remove(user.getUid());
                vehicle.setSeatsLeft(vehicle.getCapacity() - vehicle.getRidersUIDs().size());
                firestore.collection("vehicles")
                        .document(vehicle.getVehicleID())
                        .set(vehicle).addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                    {
                        user.setCurrRideID("");
                        firestore.collection("users")
                                .document(user.getUid())
                                .set(user);

                        blackStyle(bookRideButton);
                        Toast.makeText(VehicleProfileActivity.this, "Ride cancelled",
                                Toast.LENGTH_SHORT);
                    }
                });
            }
        });
    }

    public void creamStyle(ToggleButton button)
    {
        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg));
        button.setBackgroundColor(getResources().getColor(R.color.cream));
        button.setTextColor(getResources().getColor(R.color.black));
    }

    public void blackStyle(ToggleButton button)
    {
        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg));
        button.setTextColor(getResources().getColor(R.color.cream));
    }

    public void goUserProfileActivity(View v)
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}