package felix.chen.ib_carpool.Vehicle;

import felix.chen.ib_carpool.User.User;

public class Tank extends Vehicle
{
    public Tank()
    {
    }

    public Tank(String ownerID, String ownerName, String model, Integer capacity, String vehicleType, Double basePrice)
    {
        super(ownerID, ownerName, model, capacity, vehicleType, basePrice);
    }
}
