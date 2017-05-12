package com.example.rohankankapurkar.facebook;

/**
 * Created by NACHIKET on 5/11/2017.
 */

public class ListModel {

    //NACHIKET: Try for name and email first
    private  String FriendFirstName;
    private  String FriendLastName;
    private  String FriendEmail;

    public String getFriendFirstName() {
        return FriendFirstName;
    }

    public void setFriendFirstName(String friendFirstName) {
        FriendFirstName = friendFirstName;
    }

    public String getFriendLastName() {
        return FriendLastName;
    }

    public void setFriendLastName(String friendLastName) {
        FriendLastName = friendLastName;
    }

    public String getFriendEmail() {
        return FriendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        FriendEmail = friendEmail;
    }

    ListModel(String first, String last, String email) {
        FriendFirstName = first;
        FriendLastName = last;
        FriendEmail = email;
    }

}
