package nidhikamath.com.chatapplicationdemo.model;

public class User {
    private String uId = "";
    private String username = "";
    private String status = "";

    public User(){

    }

    public User(String uId, String username, String status){
        this.uId = uId;
        this.username = username;
        this.status = status;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
