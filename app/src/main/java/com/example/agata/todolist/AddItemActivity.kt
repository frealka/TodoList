package com.example.agata.todolist

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.savvyapps.togglebuttonlayout.Toggle
import kotlinx.android.synthetic.main.activity_add_item.*
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import android.widget.Toast
import com.example.agata.todolist.database.AppDatabase
import com.example.agata.todolist.database.DbWorker
import com.example.agata.todolist.recycler.CardItem
import com.example.agata.todolist.recycler.RecyclerViewAdapter
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_card.*

class AddItemActivity : AppCompatActivity() {

    private val cal : Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

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

        val cardItem = CardItem(0,
            taskEditText.text.toString(),
            contentEditText.text.toString(),
            deadlineEditText.text.toString(),
            priorityColor,
            toDraw)

        // add to database
        val mDbWorker = DbWorker("dbWorkerThread")
        val mDb = this.let { AppDatabase.getInstance(it) }
        mDbWorker.start()

        val task = Runnable {
            mDb.todoItemsDAO().insertAll(cardItem)
        }
        mDbWorker.postTask(task)
        finish()
    }
}
