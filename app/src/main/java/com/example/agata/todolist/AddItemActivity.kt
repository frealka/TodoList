package com.example.agata.todolist

import android.util.Log
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.savvyapps.togglebuttonlayout.Toggle
import kotlinx.android.synthetic.main.activity_add_item.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import com.example.agata.todolist.database.AppDatabase
import com.example.agata.todolist.database.DbWorker
import com.example.agata.todolist.recycler.CardItem
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout
import java.sql.Timestamp

class AddItemActivity : AppCompatActivity() {

    private val cal : Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        createChannel()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }


        deadlineEditText!!.setOnClickListener {
            DatePickerDialog(this@AddItemActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        deadlineEditText!!.text = sdf.format(cal.time)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("dateTime", deadlineEditText!!.text.toString())
        val selectedPrior = priorityToggleButton.getSelectedToggles()
        if(!selectedPrior.isEmpty()){
            outState?.putInt("priority", selectedPrior[0].id)
        }

        val selectedType = imgsToggleButton.getSelectedToggles()
        if(!selectedType.isEmpty()){
            outState?.putInt("imgType", selectedType[0].id)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val deadline = savedInstanceState?.getString("dateTime")
        if(deadline != null){
            deadlineEditText.text = deadline
        }

        checkToggle(priorityToggleButton, savedInstanceState?.getInt("priority", -1))
        checkToggle(imgsToggleButton, savedInstanceState?.getInt("imgType", -1))
    }

    private fun checkToggle(toggle: ToggleButtonLayout, id: Int?){
        if(id!= null && id != -1){
            toggle.setToggled(id, true)
        }
    }

    fun onDecline(v: View){
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun onAccept(v: View){
        if(taskEditText.text.isEmpty()){
            Toast.makeText(this@AddItemActivity, "Set task title", Toast.LENGTH_SHORT).show()
            return
        }
        if(deadlineEditText.text.isEmpty()){
            Toast.makeText(this@AddItemActivity, "Set deadline", Toast.LENGTH_SHORT).show()
            return
        }
        // content may be empty
        val selectedProperty : List<Toggle> = priorityToggleButton.getSelectedToggles()
        if(selectedProperty.isEmpty()){
            Toast.makeText(this@AddItemActivity, "Set priority", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedType : List<Toggle> = imgsToggleButton.getSelectedToggles()
        if(selectedType.isEmpty()){
            Toast.makeText(this@AddItemActivity, "Set type", Toast.LENGTH_SHORT).show()
            return
        }
        val toDraw = when(selectedType[0].id){
            R.id.img_toggle_left -> R.drawable.computer_icon
            R.id.img_toggle_center -> R.drawable.home_icon
            else -> R.drawable.work_icon
        }
        val priorityColor = when(selectedProperty[0].id){
            R.id.toggle_left -> R.color.colorGreen
            R.id.toggle_center -> R.color.colorAccent
            else -> R.color.colorRed
        }

        val cardItem = CardItem(
            id = 0,
            title = taskEditText.text.toString(),
            content = contentEditText.text.toString(),
            deadline = deadlineEditText.text.toString(),
            taskPriority = priorityColor,
            type = toDraw)
        // add to database
        val mDbWorker = DbWorker("dbWorkerThread")
        val mDb = this.let { AppDatabase.getInstance(it) }
        mDbWorker.start()

        val task = Runnable {
            mDb.todoItemsDAO().insertAll(cardItem)
            val id = mDb.todoItemsDAO().getLargestId()
            //should be time from cardItem deadline
            setAlarm(this, Calendar.getInstance().timeInMillis+10000, id)
        }
        mDbWorker.postTask(task)

        // start service
        Log.d("notification", "intent service created")
        finish()
    }

    private fun createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = "Simple notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MainActivity.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        val NOTIFY_ID = 134

        @JvmStatic
        fun setAlarm(context: Context, timestamp: Long, id: Int){
            Log.d("NOTIFICATION::", "set alarm")
            val newIntent = Intent(context, NotificationReceiver::class.java)
            newIntent.putExtra("id", id)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                NOTIFY_ID,
                newIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                timestamp,
                pendingIntent
            )
        }
    }
}
