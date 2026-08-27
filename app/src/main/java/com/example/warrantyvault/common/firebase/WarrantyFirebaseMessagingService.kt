package com.example.warrantyvault.common.firebase

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.warrantyvault.MainActivity
import com.example.warrantyvault.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelID="notification_channel"
const val channelName="com.example.warrantyvault"

class WarrantyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM", "Token : $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

//        Log.d("FCM", "Title : ${remoteMessage.notification?.title}")
//        Log.d("FCM", "Body : ${remoteMessage.notification?.body}")

        if(remoteMessage.getNotification()!=null){

            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)

        }
    }

    fun generateNotification(title: String, message: String)
    {
        val intent=Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        //channel id,name




        var builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.fcm_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setCustomContentView(getRemoteView(title,message))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())



//        val builder = NotificationCompat.Builder(this, channelID)
//            .setSmallIcon(R.drawable.fcm_icon)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)


        val notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){


            val notificationChannel=
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

            notificationManager.notify(0,builder.build())
        }
    }


    fun getRemoteView(title: String, message: String): RemoteViews
    {
        val remoteview=RemoteViews("com.example.warrantyvault",R.layout.notification_custom)
        remoteview.setTextViewText(R.id.title,title)
        remoteview.setTextViewText(R.id.message,message)
        remoteview.setImageViewResource(R.id.app_logo,R.drawable.fcm_icon)


        return remoteview

    }


}