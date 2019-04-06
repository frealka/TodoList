package com.example.agata.todolist

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CardItem(val title: String,
               val content: String,
               val deadline: String,
               val taskPriority: Int,
               val type: Int) : Parcelable{
    var deadlineTime : LocalDate = initDeadlineTime()

    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        deadline = parcel.readString() ?: "",
        content = parcel.readString() ?: "",
        taskPriority = parcel.readInt(),
        type = parcel.readInt()
    ){
        deadlineTime = initDeadlineTime()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(deadline)
        parcel.writeInt(taskPriority)
        parcel.writeInt(type)
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

    private fun initDeadlineTime() : LocalDate {
        return LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}