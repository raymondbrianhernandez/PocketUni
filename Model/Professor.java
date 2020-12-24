package raymondhernandez.pocketuniv.Model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created 3/27/2016.
 */
public class Professor {

    private String Name;
    private String school;
    private String email;
    private String department;
    private Long phoneNumber;
    private String imageUrl;
    private float rating;
    private String[] courses;
    private String biography;
    public Professor(){    }

    public Professor(String name, String school, String email, String department, Long phoneNumber, String imageUrl,float rating, String[] courses,String biography) {
        Name = name;
        this.school = school;
        this.email = email;
        this.department = department;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.courses = courses;
        this.biography = biography;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String[] getCourses() {
        return courses;
    }

    public void setCourses(String[] courses) {
        this.courses = courses;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
