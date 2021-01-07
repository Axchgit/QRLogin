package cn.xct.qrLogin.entity;


public class UserEntity {

  private long work_num;
  private String password;
  private String user_token;

  private String nick_name;
  private String avatar;
  private long phone;


  public long getWorkNum() {
    return work_num;
  }

  public void setWorkNum(long work_num) {
    this.work_num = work_num;
  }

  public String getUserToken() {
    return user_token;
  }

  public void setUserToken(String user_token) {
    this.user_token = user_token;
  }



  public String getUserPassword() {
    return password;
  }

  public void setUserPassword(String password) {
    this.password = password;
  }


  public String getUserName() {
    return nick_name;
  }

  public void setUserName(String nick_name) {
    this.nick_name = nick_name;
  }


  public String getUserAvatar() {
    return avatar;
  }

  public void setUserAvatar(String avatar) {
    this.avatar = avatar;
  }


  public long getUserPhone() {
    return phone;
  }

  public void setUserPhone(long phone) {
    this.phone = phone;
  }

}
