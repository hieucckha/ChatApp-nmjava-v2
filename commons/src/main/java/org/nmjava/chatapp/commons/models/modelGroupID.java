package org.nmjava.chatapp.commons.models;

public class modelGroupID {
    private int GroupID;
    private String GroupName;
    private String GroupUserName;

    private String GroupRole;

    public modelGroupID(int GroupID, String GroupName, String GroupUserName, String GroupRole) {
        this.GroupID = GroupID;
        this.GroupName = GroupName;
        this.GroupUserName = GroupUserName;
        this.GroupRole = GroupRole;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupUserName() {
        return GroupUserName;
    }

    public void setGroupUserName(String groupUserName) {
        GroupUserName = groupUserName;
    }

    public String getGroupRole() {
        return GroupRole;
    }

    public void setGroupRole(String groupRole) {
        GroupRole = groupRole;
    }
}
