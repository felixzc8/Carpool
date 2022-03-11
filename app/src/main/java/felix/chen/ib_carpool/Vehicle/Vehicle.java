package felix.chen.ib_carpool.Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Vehicle implements Serializable
{
    String ownerID;
    String ownerName;
    String model;
    Integer capacity;
    Integer seatsLeft;
    String vehicleID;
    ArrayList<String> ridersUIDs;
    Boolean open;
    String vehicleType;
    Double basePrice;

    public Vehicle()
    {
    }

    public Vehicle(String ownerID, String ownerName, String model, Integer capacity, String vehicleType, Double basePrice)
    {
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.model = model;
        this.capacity = capacity;
        this.seatsLeft = capacity;
        this.vehicleType = vehicleType;
        this.basePrice = basePrice;
        this.ridersUIDs = new ArrayList<>();
        this.open = false;
        vehicleID = UUID.randomUUID().toString();
    }

    @Override
    public String toString()
    {
        return "Vehicle{" +
                "ownerID='" + ownerID + '\'' +
                ", model='" + model + '\'' +
                ", capacity=" + capacity +
                ", vehicleID='" + vehicleID + '\'' +
                ", ridersUIDs=" + ridersUIDs +
                ", open=" + open +
                ", vehicleType='" + vehicleType + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }

    public String getOwnerID()
    {
        return ownerID;
    }

    public void setOwnerID(String ownerID)
    {
        this.ownerID = ownerID;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public Integer getCapacity()
    {
        return capacity;
    }

    public void setCapacity(Integer capacity)
    {
        this.capacity = capacity;
    }

    public Integer getSeatsLeft()
    {
        return seatsLeft;
    }

    public void setSeatsLeft(Integer seatsLeft)
    {
        this.seatsLeft = seatsLeft;
    }

    public String getVehicleID()
    {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID)
    {
        this.vehicleID = vehicleID;
    }

    public ArrayList<String> getRidersUIDs()
    {
        return ridersUIDs;
    }

    public void setRidersUIDs(ArrayList<String> ridersUIDs)
    {
        this.ridersUIDs = ridersUIDs;
    }

    public Boolean getOpen()
    {
        return open;
    }

    public void setOpen(Boolean open)
    {
        this.open = open;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public Double getBasePrice()
    {
        return basePrice;
    }

    public void setBasePrice(Double basePrice)
    {
        this.basePrice = basePrice;
    }
}
