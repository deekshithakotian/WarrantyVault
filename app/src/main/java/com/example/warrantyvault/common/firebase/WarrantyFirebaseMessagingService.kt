package com.example.warrantyvault.common.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class WarrantyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM", "Token : $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM", "Title : ${message.notification?.title}")
        Log.d("FCM", "Body : ${message.notification?.body}")
    }


}