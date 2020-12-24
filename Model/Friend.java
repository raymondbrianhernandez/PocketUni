package raymondhernandez.pocketuniv.Model;

/**
 * Created 4/26/2016.
 */
public class Friend {
    private String email;
    private String name;
    private String chatId;

    public Friend(){}

    public Friend(String name, String email,String chatId) {
        this.email = email;
        this.name = name;
        this.chatId = chatId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend = (Friend) o;

        if (!getEmail().equals(friend.getEmail())) return false;
        return getName().equals(friend.getName());

    }

    @Override
    public int hashCode() {
        int result = getEmail().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
