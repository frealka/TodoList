package com.example.agata.todolist

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CardItem(val title: String,
               val content: String,
               val deadline: String,
               val taskPriority: String) : Parcelable{
    var deadlineTime : LocalDate = initDealineTime()

    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        content = parcel.readString() ?: "",
        deadline = parcel.readString() ?: "",
        taskPriority = parcel.readString() ?: ""
    ){
        deadlineTime = initDealineTime()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(deadline)
        parcel.writeString(taskPriority)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardItem> {
        override fun createFromParcel(parcel: Parcel): CardItem {
            return CardItem(parcel)
        }

        override fun newArray(size: Int): Array<CardItem?> {
            return arrayOfNulls(size)
        }
    }

    private fun initDealineTime() : LocalDate {
        return LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}