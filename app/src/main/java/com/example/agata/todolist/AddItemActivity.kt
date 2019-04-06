package com.example.agata.todolist

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.savvyapps.togglebuttonlayout.Toggle
import kotlinx.android.synthetic.main.activity_add_item.*
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import android.widget.Toast

class AddItemActivity : AppCompatActivity() {

    private val cal : Calendar = Calendar.getInstance()

    private val TAG : String = "AddItemActivity"

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

    private fun chooseImage(){
        // otworz okienko z obrazkami
        // wybierz obrazek przez klikniecie na niego
    }

    fun onDecline(v: View){
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun onAccept(v: View){
        val taskIntent = Intent()
        if(taskEditText.text.isEmpty()){
            Toast.makeText(this@AddItemActivity, "Set task title", Toast.LENGTH_SHORT).show()
            return
        }
        if(deadlineEditText.text.isEmpty()){
            Toast.makeText(this@AddItemActivity, "Set deadline", Toast.LENGTH_SHORT).show()
            return
        }
        // content may be empty
        val selectedProperty : List<Toggle> = imgsToggleButton.getSelectedToggles()
        if(selectedProperty.isEmpty()){
            Toast.makeText(this@AddItemActivity, "Set priority", Toast.LENGTH_SHORT).show()
            return
        }
        taskIntent.putExtra("task", taskEditText.text.toString())
        taskIntent.putExtra("deadline", deadlineEditText.text.toString())
        taskIntent.putExtra("content", contentEditText.text.toString())
        taskIntent.putExtra("priority", selectedProperty[0].title.toString())
        Log.d(TAG, "intent created")
        setResult(Activity.RESULT_OK, taskIntent)
        finish()
    }
}
