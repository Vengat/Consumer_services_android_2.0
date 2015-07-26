package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.listener.ServerUnreachableListener;

/**
 * Created by vengat.r on 7/24/2015.
 */
public class ServerUnreachableNotifier {

    public ServerUnreachableNotifier(ServerUnreachableListener serverUnreachableListener) {
        serverUnreachableListener.onUnreachableServer();
    }
}
