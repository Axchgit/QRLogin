package cn.trunch.auth.entity;


import java.io.Serializable;
import java.sql.Timestamp;

public class AuthEntity implements Serializable {

  private String qruid;
  private String auth_time;
  private String auth_ip;
  private String auth_address;
  private Integer auth_state;
  private long user_uuid;


  public String getAuthToken() {
    return qruid;
  }

  public void setAuthToken(String authToken) {
    this.qruid = authToken;
  }


  public String getAuthTime() {
    return auth_time;
  }

  public void setAuthTime(String authTime) {
    this.auth_time = authTime;
  }


  public String getAuthIp() {
    return auth_ip;
  }

  public void setAuthIp(String authIp) {
    this.auth_ip = authIp;
  }


  public String getAuthAddress() {
    return auth_address;
  }

  public void setAuthAddress(String authAddress) {
    this.auth_address = authAddress;
  }


  public Integer getAuthState() {
    return auth_state;
  }

  public void setAuthState(Integer authState) {
    this.auth_state = authState;
  }


  public long getUserId() {
    return user_uuid;
  }

  public void setUserId(long userId) {
    this.user_uuid = userId;
  }

}
