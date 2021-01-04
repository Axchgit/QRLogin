package cn.trunch.auth.entity;


public class UserEntity {

  private long work_num;
  private String password;
  private String nick_name;
  private String avatar;
  private long phone;


  public long getUserId() {
    return work_num;
  }

  public void setUserId(long nick_name) {
    this.work_num = nick_name;
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
