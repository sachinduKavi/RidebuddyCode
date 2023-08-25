package com.example.ridebuddy23;

public class UserDetails {
    String user_name, email, password, DP, mobile, user_dob, userID;
    byte gender;
    int profile_state;

    public String getUser_dob() {
        return user_dob;
    }

    public void setUser_dob(String user_dob) {
        this.user_dob = user_dob;
    }

    public byte getGender() {
        return this.gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public UserDetails(String user_name, String email, String mobile, String password, String DP, int profile_state, String user_dob, byte gender, String userID) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.DP = DP;
        this.mobile = mobile;
        this.user_dob = user_dob;
        this.userID = userID;
        this.gender = gender;
        this.profile_state = profile_state;

    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    // For Authentication
    public UserDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        String all_string = user_name + "\n" + email + "\n" + mobile + "\n" + password + "\n" + DP + "\n" + profile_state + "\n" + '\n' + "\n" + user_dob + "\n" + userID;
        return all_string;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDP() {
        return DP;
    }

    public void setDP(String DP) {
        this.DP = DP;
    }

    public int getProfile_state() {
        return profile_state;
    }

    public void setProfile_state(int profile_state) {
        this.profile_state = profile_state;
    }

}