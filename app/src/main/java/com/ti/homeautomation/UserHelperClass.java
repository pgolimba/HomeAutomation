package com.ti.homeautomation;

public class UserHelperClass {

 String lname,fname,email,username,password;

    public UserHelperClass(){
    }

    public UserHelperClass(String lname, String fname, String email, String username, String password) {
        this.lname = lname;
        this.fname=fname;
        this.email=email;
        this.username=username;
        this.password=password;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
