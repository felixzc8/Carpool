package felix.chen.ib_carpool.Vehicle;

import felix.chen.ib_carpool.User.User;

public class Helicopter extends Vehicle
{
    public Helicopter()
    {
    }

    public Helicopter(String ownerID, String ownerName, String model, Integer capacity, String vehicleType, Double basePrice)
    {
        super(ownerID, ownerName, model, capacity, vehicleType, basePrice);
    }
}
