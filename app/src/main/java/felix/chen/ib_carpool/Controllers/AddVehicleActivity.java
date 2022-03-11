package felix.chen.ib_carpool.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ib_carpool.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import felix.chen.ib_carpool.User.User;
import felix.chen.ib_carpool.Vehicle.Car;
import felix.chen.ib_carpool.Vehicle.Helicopter;
import felix.chen.ib_carpool.Vehicle.Tank;
import felix.chen.ib_carpool.Vehicle.Vehicle;

/**
 * Allows users to add vehicles that they own by inputting the model, the capacity, the price,
 * and choosing the vehicle type
 *
 * @author Felix Chen
 * @version 1.0
 */
public class AddVehicleActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener
{
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    FirebaseUser mUser;

    User user;
    ArrayList<String> userVehicles;

    EditText modelInput, capacityInput, priceInput;

    Spinner spinner;

    String capacityS, priceS;

    String model, type;
    Integer capacity;
    Double price;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        System.out.println(user.getEmail() + user.getName());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        userVehicles = new ArrayList<>();

        modelInput = findViewById(R.id.modelInputEditText);
        capacityInput = findViewById(R.id.capacityInputEditText);
        priceInput = findViewById(R.id.priceInputEditText);

        spinner = findViewById(R.id.vehicleTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.vehicleTypes, R.layout.type_row_view);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * Checks if all the required fields are filled
     *
     * @return true if all fields are filled, false if otherwise
     */
    public boolean formValid()
    {
        model = modelInput.getText().toString();
        capacityS = capacityInput.getText().toString();
        priceS = priceInput.getText().toString();

        if (!model.equals("") && !capacityS.equals("") && !priceS.equals(""))
        {
            return true;
        }
        else
        {
            Toast.makeText(this, "missing info", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Once it checks that all fields are valid, add a vehicle to the firestore database under
     * "vehicles" as well as under the user's owned vehicles
     *
     * @param v
     */
    public void addNewVehicle(View v)
    {
        if (formValid())
        {
            capacity = Integer.parseInt(capacityS);
            price = Double.parseDouble(priceS);

            Toast successMsg = Toast.makeText(this, "vehicle added",
                    Toast.LENGTH_SHORT);

            try
            {
                if (type.equals("car"))
                {
                    Vehicle car = new Car(user.getUid(), user.getName(), model,
                            capacity, "car", price);

                    firestore.collection("vehicles")
                            .document(car.getVehicleID()).set(car);

                    userVehicles = user.getOwnedVehicles();
                    userVehicles.add(car.getVehicleID());
                    user.setOwnedVehicles(userVehicles);
                    firestore.collection("users").document(user.getUid()).set(user);

                    successMsg.show();
                    goVehicleProfileActivity(car);
                }
                else if (type.equals("helicopter"))
                {
                    Vehicle helicopter = new Helicopter(user.getUid(), user.getName(), model,
                            capacity, "helicopter", price);

                    firestore.collection("vehicles")
                            .document(helicopter.getVehicleID()).set(helicopter);

                    userVehicles = user.getOwnedVehicles();
                    userVehicles.add(helicopter.getVehicleID());
                    user.setOwnedVehicles(userVehicles);
                    firestore.collection("users").document(user.getUid()).set(user);

                    successMsg.show();
                    goVehicleProfileActivity(helicopter);
                }
                else if (type.equals("tank"))
                {
                    Vehicle tank = new Tank(user.getUid(), user.getName(), model,
                            capacity, "tank", price);

                    firestore.collection("vehicles")
                            .document(tank.getVehicleID()).set(tank);

                    userVehicles = user.getOwnedVehicles();
                    userVehicles.add(tank.getVehicleID());
                    user.setOwnedVehicles(userVehicles);
                    firestore.collection("users").document(user.getUid()).set(user);

                    successMsg.show();
                    goVehicleProfileActivity(tank);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "error adding vehicle", Toast.LENGTH_SHORT).show();
            }
        }
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String text = "Vehicle type: " + adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

        if (i == 0)
        {
            type = "car";
        }
        else if (i == 1)
        {
            type = "helicopter";
        }
        else if (i == 2)
        {
            type = "tank";
        }
    }

    /**
     * Goes to vehicleProfileActivity, passing the added vehicle to the next activity
     *
     * @param vehicle Vehicle created in this activity
     */
    public void goVehicleProfileActivity(Vehicle vehicle)
    {
        Intent intent = new Intent(this, VehicleProfileActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("vehicle", vehicle);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        type = "car";
    }
}