package jonomoneta.juno.moviecash.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WinnerListItem implements Serializable {

    @SerializedName("ID")
    @Expose
    private int ID;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("ProfilePicture")
    @Expose
    private String ProfilePicture;

    public WinnerListItem(int ID, String name, String profilePicture) {
        this.ID = ID;
        Name = name;
        ProfilePicture = profilePicture;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }
}
