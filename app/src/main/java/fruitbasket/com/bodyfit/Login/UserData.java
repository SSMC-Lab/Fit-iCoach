package fruitbasket.com.bodyfit.Login;

/**
 * Created by lkeye on 2018/10/14
 */
public class UserData {
    private String UserName;  //用户名
    private String userPwd;   //用户密码
    private int userId;  //用户ID

    public UserData(String userName,String userPwd){
        this.UserName=userName;
        this.userPwd=userPwd;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
