package raymondhernandez.pocketuniv.Model;

import android.util.Log;

/**
 * Created 4/19/2016.
 */
public class ChatList {

    private String title;
    private String course;
    private String createdBy;
    private String id;

    public ChatList(){}

    public ChatList(String title, String course,String createdBy,String id) {
        this.title = title;
        this.course = course;
        this.createdBy = createdBy;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatList chatList = (ChatList) o;

        if (!getId().equals(chatList.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
