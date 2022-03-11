package felix.chen.ib_carpool.User;

import java.io.Serializable;
import java.util.ArrayList;

import felix.chen.ib_carpool.Vehicle.Vehicle;

public class User implements Serializable
{
    private String uid;
    private String name;
    private String email;
    private String userType;
    private ArrayList<String> ownedVehicles;
    private String currRideID;

    @Override
    public String toString()
    {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", ownedVehicles=" + ownedVehicles +
                '}';
    }

    public User()
    {
    }

    public User(String uid, String name, String email, String userType)
    {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.ownedVehicles = new ArrayList<>();
        this.currRideID = "";
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public ArrayList<String> getOwnedVehicles()
    {
        return ownedVehicles;
    }

    public void setOwnedVehicles(ArrayList<String> ownedVehicles)
    {
        this.ownedVehicles = ownedVehicles;
    }

    public String getCurrRideID()
    {
        return currRideID;
    }

    public void setCurrRideID(String currRideID)
    {
        this.currRideID = currRideID;
    }
}
