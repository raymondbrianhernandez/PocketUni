package raymondhernandez.pocketuniv.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.firebase.client.ServerValue;

import java.util.Date;

/**
 * Created by Chachi on 4/18/2016.
 */
public class ChatMessage {

    private String name;
    private String text;

    @JsonProperty
    private Object time;

    public ChatMessage(){}
    
    public ChatMessage(String name, String text) {
        this.name = name;
        this.text = text;
        this.time = ServerValue.TIMESTAMP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonIgnore
    public Long getTime() {
        if (time instanceof Long) {
            return (Long) time;
        }
        else {
            return null;
        }
    }
}
