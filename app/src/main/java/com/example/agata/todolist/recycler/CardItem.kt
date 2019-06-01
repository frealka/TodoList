package com.example.agata.todolist.recycler

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "todoItems")
class CardItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id : Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "deadline") val deadline: String,
    @ColumnInfo(name = "taskPriority") val taskPriority: Int,
    @ColumnInfo(name = "type") val type: Int
) : Parcelable{
    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        content = parcel.readString() ?: "",
        deadline = parcel.readString() ?: "",
        taskPriority = parcel.readInt(),
        type = parcel.readInt()
    )

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

    fun getDeadlineTime() : LocalDate {
        return LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}