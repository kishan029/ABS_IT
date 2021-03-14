package commercial.dlab.abs_it;

public class UserData {
    public String username;
    public String userlocation;

    public UserData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserData(String username, String userlocation) {
        this.username = username;
        this.userlocation = userlocation;
    }
}
