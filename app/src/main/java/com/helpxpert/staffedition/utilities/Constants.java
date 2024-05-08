package com.helpxpert.staffedition.utilities;

import java.util.HashMap;

public class Constants {
public static final String NOTIFICATION_VALUE = "notification_switch_value";
    public static final String DISPLAY_VALUE = "display_switch_value";

    //FACILITY OR STAFF DETAILS
    public static final String KEY_COLLECTION_USERS = "staffUsers"; //staff collection
    public static final String KEY_FACILITY_USERS = "FacilityUsers"; //facility collection

    public static final String KEY_COLLECTION_REPORT = "ReportHistory";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "issignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";

    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";

    //REPORT HISTORY
    public static final String KEY_COLLECTION_HISTORY = "ReportHistory";
    public static final String KEY_REPORTER = "reporterName";
    public static final String KEY_REPORTERIMAGE = "reporterImage";



    //SHARED CHATS
    public static final String KEY_COLLECTION_CHAT = "Chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";


    //APP TYPE CONVERSATIONS
    public static final String KEY_COLLECTION_CONVERSATIONS = "Conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";

    public static final String KEY_AVAILABILITY = "availability";


    //PUSH NOTIFICATION CONSTANTS
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final String KEY_SECRETPHRASE = "secretphrase";

    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAK7ObM4g:APA91bEh01KKjxAepjdDiDlKFbAUTF-giZaisQizXOKrsbxCas-_oi99iv8vV_0HL1RLE1c9BgAuwvrqIyHZjPx-n7yGI17rY8qOPy_SEhEwJVy7LII8R_Jkk2wTHuL_b2yqBP1MRsa4"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }

    //REPORT INFORMATION
    public static final String KEY_REPORT_ID = "reportId";
    public static final String KEY_STATUS = "status";
    public static final String KEY_DESCRIPTION = "description";

    public static final String REPO_ID = "reportid";
    public static final String USE_ID = "reporterId";
    public static final String KEY_HANDLERNAME = "handlerName";

}
