package felix.chen.ib_carpool.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ib_carpool.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import felix.chen.ib_carpool.User.User;
import felix.chen.ib_carpool.Vehicle.Car;
import felix.chen.ib_carpool.Vehicle.Helicopter;
import felix.chen.ib_carpool.Vehicle.Tank;
import felix.chen.ib_carpool.Vehicle.Vehicle;

/**
 * This class displays a recyclerview of vehicles that are not owned by the user
 * Each row shows the vehicle type, model, driver, and price of the vehicle
 *
 * @author Felix Chen
 * @version 1.0
 */
public class VehiclesInfoActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firestore;

    User user;

    RecyclerView recView;
    VehiclesInfoAdapter adapter;
    VehiclesInfoAdapter.RecyclerViewClickListener listener;

    ArrayList<Vehicle> vehiclesList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_info);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        vehiclesList = new ArrayList<>();

//        Vehicle car = new Car("67", "felix", "sickCar", 4, 4, "car", 2.0);
//        Vehicle tank = new Tank("68", "james", "sicktank", 4, 4, "tank", 2.0);
//        Vehicle heli = new Helicopter("69", "lucayan", "sickHeli", 4, 4, "helicopter", 2.0);
//        vehiclesList.add(car);
//        vehiclesList.add(tank);
//        vehiclesList.add(heli);


        getVehicles();

        setOnClickListener();
        recView = findViewById(R.id.recView);
        adapter = new VehiclesInfoAdapter(vehiclesList, listener);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(VehiclesInfoActivity.this));
    }

    /**
     * Retrieves all vehicles from the firebse, and if the vehicle isn't owned by the user, add it
     * to an arraylist and passes it to the adapter
     */
    public void getVehicles()
    {
        firestore.collection("vehicles")
                .whereEqualTo("open", true)
                .whereGreaterThan("seatsLeft", 0)
                .get()
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                    {
                        Log.d("get documents ", "success");

                        for (QueryDocumentSnapshot qds : task.getResult())
                        {
                            Vehicle vehicle = qds.toObject(Vehicle.class);

                            if (!vehicle.getOwnerID().equals(user.getUid()))
                            {
                                if (vehicle.getVehicleType().equals("car"))
                                {
                                    Vehicle car = qds.toObject(Car.class);
                                    vehiclesList.add(car);
                                }
                                else if (vehicle.getVehicleType().equals("helicopter"))
                                {
                                    Vehicle helicopter = qds.toObject(Helicopter.class);
                                    vehiclesList.add(helicopter);
                                }
                                else if (vehicle.getVehicleType().equals("tank"))
                                {
                                    Vehicle tank = qds.toObject(Tank.class);
                                    vehiclesList.add(tank);
                                }
                            }
                        }
                        System.out.println("VEHICLES: " + vehiclesList);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Log.d("Error getting documents: ", "" + task.getException());
                    }
                });
    }

    public void setOnClickListener()
    {
        listener = (v, position) ->
        {
            Intent intent = new Intent(getApplicationContext(), VehicleProfileActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("vehicle", vehiclesList.get(position));
            startActivity(intent);
        };
    }

    /**
     * Goes to UserProfileActivity
     *
     * @param v button click
     */
    public void goUserProfileActivity(View v)
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to AddVehicleActivity
     *
     * @param v button click
     */
    public void goAddVehicles(View v)
    {
        System.out.println(user.toString());
        Intent intent = new Intent(this, AddVehicleActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}