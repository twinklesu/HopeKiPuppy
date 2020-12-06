package com.example.hopekipuppy;

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service;
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    private fun sendNotification(msgData: Map<String, String>) {
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시되도록 함
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 일회용 PendingIntent
        // PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임한다.
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack 을 경로만 남긴다. A-B-C-D-B => A-B
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)

        // 알림 채널 이름
        val channelId = ""	// Notice
        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정한다.
        val notificationBuilder = NotificationCompat.Builder(this, channelId) // 아이콘
            .setContentTitle(msgData.getValue("title"))               // 제목
            .setContentText(msgData.getValue("msg"))              // 세부내용
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)                          // 알림 실행 시 Intent

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }

//    override fun onNewToken(token: String?) {
//        super.onNewToken(token)
//        Log.i(TAG, "Represhed token: " + token)
//
//        //필요하면 이 토큰을 앱서버에 저장하는 과정을 거치면 된다.
//        sendToken(token)
//    }

    override fun onMessageReceived(p0: RemoteMessage) {
        // Check if message contains a data payload.
        if (p0 != null) {

            // 데이터 메시지인 경우
            if (p0.data.isNotEmpty()) {
                sendNotification(p0.data)

            }
            // 알림 메시지인 경우
            if (p0.notification != null) {
                val remoteMessageData = mapOf("title" to p0.notification?.title.toString(),
                    "msg" to p0.notification?.body.toString())
                sendNotification(remoteMessageData)

            }
        }
    }

}