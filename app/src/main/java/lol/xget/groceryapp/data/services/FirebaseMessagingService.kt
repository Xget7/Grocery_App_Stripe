package lol.xget.groceryapp.data.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.traceEventEnd
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import lol.xget.groceryapp.MainActivity
import lol.xget.groceryapp.R
import lol.xget.groceryapp.common.Constants.NOTIFICATION_CHANNEL_CODE
import lol.xget.groceryapp.common.Constants.NOTIFICATION_CHANNEL_NAME
import lol.xget.groceryapp.common.Constants.NOTIFICATION_REQUEST_CODE
import lol.xget.groceryapp.domain.util.Destinations


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingService constructor() : FirebaseMessagingService() {

    val user = FirebaseAuth.getInstance().currentUser

    //generate notificaiton
    //attach it
    // SHOW IT
    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data["notificationType"] == "NewOrder") {
            if (user != null && user.uid == message.data["sellerUid"]){
                generateNotification(message.data["notificationTitle"].toString(),
                    message.data["notificationMessage"].toString(),
                    message.data["orderId"].toString())
            }
        } else if (message.data["notificationType"] == "OrderStatusChanged") {

            if (user != null && user.uid == message.data["buyerUid"]){
                generateNotification(message.data["notificationTitle"].toString(),
                    message.data["notificationMessage"].toString(),
                    message.data["orderId"].toString(),
                    message.data["notificationType"].toString(),
                    message.data["buyerUid"].toString()
                    )
            }
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    @OptIn(ExperimentalMaterialApi::class)
    fun generateNotification(
        title: String,
        message: String,
        orderId: String,
        notificationType: String? = null,
        buyerUid: String? = null
    ) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("orderId", orderId)
        intent.putExtra("notificationType", notificationType)
        intent.putExtra("sellerUid", buyerUid)
        val pendingIntent = PendingIntent.getActivity(this,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_ONE_SHOT)

        //channel id and name
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_CODE)
                .setSmallIcon(R.mipmap.ic_logo_app)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentTitle(title)
                .setSubText(message)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_CODE,
            NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(0, builder.build())
    }


}