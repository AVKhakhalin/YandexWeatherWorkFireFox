package com.example.yandexweatherwork.controller.messages

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.ui.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebasePushMessagesService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // отправка токна на сервер
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMessageData = remoteMessage.data
        if (remoteMessageData.isNotEmpty()) {
            val title = remoteMessageData[ConstantsController.PUSH_KEY_TITLE]
            val message = remoteMessageData[ConstantsController.PUSH_KEY_MESSAGE]
            if(!title.isNullOrBlank()&&!message.isNullOrBlank()){
                pushNotification(title, message)
            }
        }
    }

    private fun pushNotification(title: String, message: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //This is the intent of PendingIntent
        val intentAction = Intent(this, MainActivity::class.java)
        intentAction.putExtra(ConstantsController.ACTION_WORD, ConstantsController.ACTION_NAME)
        val intent = PendingIntent.getActivity(this,0, intentAction, 0)
        val notificationBuilder_1 = NotificationCompat.Builder(
            this, ConstantsController.CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.ic_map_flag)
            setContentTitle(title)
            setContentText(message)
            addAction(
                R.drawable.ic_map_flag,
                resources.getString(R.string.firebase_dialog_message_title), intent)
            priority = NotificationCompat.PRIORITY_MAX
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannel_1 =
                "${ConstantsController.CHANNEL_BASE_NAME} ${ConstantsController.CHANNEL_ID_1}"
            val descChannel_1 =
                "${ConstantsController.CHANNEL_BASE_DESCRIPTION} ${ConstantsController.CHANNEL_ID_1}"
            val importanceChannel_1 = NotificationManager.IMPORTANCE_HIGH
            val channel_1 =
                NotificationChannel(
                    ConstantsController.CHANNEL_ID_1,
                    nameChannel_1,
                    importanceChannel_1).apply {
                    description = descChannel_1
                }
            notificationManager.createNotificationChannel(channel_1)
        }
        notificationManager.notify((0..10000).random(), notificationBuilder_1.build())
    }
}