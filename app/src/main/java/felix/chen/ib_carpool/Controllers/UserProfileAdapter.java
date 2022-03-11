package felix.chen.ib_carpool.Controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ib_carpool.R;

import java.util.ArrayList;

import felix.chen.ib_carpool.Vehicle.Vehicle;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.UserProfileViewHolder>
{

    ArrayList<Vehicle> userVehicles;
    RecyclerViewClickListener listener;

    public UserProfileAdapter(ArrayList userVehicles, RecyclerViewClickListener listener)
    {
        this.userVehicles = userVehicles;
        this.listener = listener;
    }

    public class UserProfileViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        TextView modelText;
        TextView ownerText;
        TextView capacityText;
        TextView vehicleTypeText;

        public UserProfileViewHolder(final View view)
        {
            super(view);

            modelText = view.findViewById(R.id.modelTextView);
            ownerText = view.findViewById(R.id.ownerTextView);
            capacityText = view.findViewById(R.id.capacityTextView);
            vehicleTypeText = view.findViewById(R.id.vehicleTypeTextView);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            listener.onClick(view, getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public UserProfileAdapter.UserProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                       int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicles_row_view,
                parent, false);

        return new UserProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileAdapter.UserProfileViewHolder holder,
                                 int position)
    {
        Vehicle vehicle = userVehicles.get(position);

        String capacityText = "Seats available: " + vehicle.getCapacity();
        String driverText = "driver: " + vehicle.getOwnerName();

        holder.modelText.setText(vehicle.getModel());
        holder.ownerText.setText(driverText);
        holder.capacityText.setText(capacityText);
        holder.vehicleTypeText.setText(vehicle.getVehicleType());

    }

    @Override
    public int getItemCount()
    {
        return userVehicles.size();
    }

    public interface RecyclerViewClickListener
    {
        void onClick(View v, int position);
    }
}
