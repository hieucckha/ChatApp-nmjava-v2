package org.nmjava.chatapp.commons.enums;

// #TODO: Add type request and response
public enum ResponseType {
    // User
    AUTHENTICATION,
    CREATE_ACCOUNT,
    FOR_GOT_PASSWORD,
    //

    // Friend
    GET_LIST_FRIEND,
    GET_LIST_FRIEND_ONLINE,
    GET_LIST_REQUEST_FRIEND,
    ADD_FRIEND,
    ACCEPT_REQUEST_FRIEND,
    REJECT_REQUEST_FRIEND,
    //

    // Conservation
    GET_LIST_CONSERVATION,
    GET_LIST_MESSAGE_CONSERVATION,
    SEND_MESSAGE,
    DELETE_MESSAGE,
    SEARCH_MESSAGE_USER,
    SEARCH_MESSAGE_ALL,
    // Group chat
    CREATE_GROUP_CHAT,
    RENAME_GROUP_CHAT,
    ADD_MEMBER_GROUP_CHAT,
    GIVE_ADMIN_USER_GROUP_CHAT,
    REMOVE_USER_GROUP_CHAT,
    //
}
