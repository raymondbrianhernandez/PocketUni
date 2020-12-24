package raymondhernandez.pocketuniv.Model;

import java.util.HashMap;

/**
 * Created 3/26/2016.
 */
public class Student {

    private String firstName;
    private String lastName;
    private String email;
    private HashMap<String,Object> timeStampJoined;
    private boolean hasLoggedInWithPassword;

    public Student(){

    }

    public Student(String firstName, String lastName, String email, HashMap<String,Object> timeStampJoined) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.timeStampJoined = timeStampJoined;
        this.hasLoggedInWithPassword = false;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Object> getTimeStampJoined() {
        return timeStampJoined;
    }

    public void setTimeStampJoined(HashMap<String, Object> timeStampJoined) {
        this.timeStampJoined = timeStampJoined;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }

    public void setHasLoggedInWithPassword(boolean hasLoggedInWithPassword) {
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }
}
