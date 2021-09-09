package com.cstor.tanjiance.listener;

public interface TaskListener {
    String SYSTEM_UPDATE = "SYSTEM_UPDATE";
    String SYSTEM_REBOOT = "SYSTEM_REBOOT";
    String MEET_REFRESH = "MEET_REFRESH";
    String DEBUG = "debug";

    void task_message(String TaskName, String param);
}
