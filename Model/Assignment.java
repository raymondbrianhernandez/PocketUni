package raymondhernandez.pocketuniv.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.firebase.client.ServerValue;

/**
 * Created 5/10/2016.
 */
public class Assignment {

    private String name;
    private String course;
    private String notes;
    @JsonProperty
    private Object deadline;

    public Assignment(String name, String course, String notes,Long deadline) {
        this.name = name;
        this.course = course;
        this.notes = notes;
        this.deadline = deadline;
    }

    public Assignment(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @JsonIgnore
    public Long getDeadline() {
        if (deadline instanceof Long) {
            return (Long) deadline;
        }
        else {
            return null;
        }
    }
}
