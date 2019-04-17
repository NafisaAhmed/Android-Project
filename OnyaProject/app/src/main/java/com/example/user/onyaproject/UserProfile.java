package com.example.user.onyaproject;

public class UserProfile {
    public String Phone;
    public String Email;
    public String Name;

    public UserProfile(){
    }

    public UserProfile(String userPhone, String userEmail, String userName) {
        this.Phone = userPhone;
        this.Email = userEmail;
        this.Name = userName;
    }

    /*public String getPhone() {
        return Phone;
    }

    public void setUserPhone(String userPhone) {
        this.Phone =userPhone;
    }

    public String getEmail() {
        return Email;
    }

    public void setUserEmail(String userEmail) {
        this.Email = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }*/

}
