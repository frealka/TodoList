package com.example.agata.todolist

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.example.agata.todolist.database.AppDatabase
import com.example.agata.todolist.database.DbWorker

class NotificationReceiver : BroadcastReceiver()  {
    private var mDb: AppDatabase? = null
    private var mDbWorker : DbWorker = DbWorker("dbWorkerThread")

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("notification", "on receive")

        // init database
        mDb = AppDatabase.getInstance(context)
        mDbWorker.start()

        val task = Runnable {
            // These intents is used to launch the MainActivity when the notification is tapped
            val contentIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0)

            val id = intent.getIntExtra("id", 0)
            val cardItem = mDb?.todoItemsDAO()?.getById(id)

            // Filling the notification with data
            val builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.plus_icon)
                .setContentTitle(cardItem?.title)
                .setContentText(cardItem?.content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

            Log.d("notification", id.toString())

            with(NotificationManagerCompat.from(context)) {
                notify(id, builder.build())
                Log.d("notification", "should work")
            }
        }
        mDbWorker.postTask(task)
    }
}