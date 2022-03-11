package felix.chen.ib_carpool.Vehicle;


public class Car extends Vehicle
{
    public Car()
    {
    }

    public Car(String ownerID, String ownerName, String model, Integer capacity, String vehicleType, Double basePrice)
    {
        super(ownerID, ownerName, model, capacity, vehicleType, basePrice);
    }
}
