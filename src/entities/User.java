package entities;
public class User {
    protected final int userID;
    protected String username;
    protected String phone;
    protected String password;
    protected String role;
    public User(int userID, String username, String phone, String password, String role){
        this.password = password;
        this.phone = phone;
        this.userID = userID;
        this.username = username;
        this.role = role;
    }
    public int getUserID(){
        return userID;
    }
    public String getUsername(){
        return username;
    }
    public void setUserName(String username){
        this.username = username;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public String getRole(){
        return role;
    }
}
