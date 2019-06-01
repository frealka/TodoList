package com.example.agata.todolist.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.agata.todolist.recycler.CardItem

@Database(entities = [CardItem::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun todoItemsDAO() : TodoItemDAO

    companion object{
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    if(INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "todoList.db"
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE as AppDatabase
        }
    }

    fun destroyInstance(){
        INSTANCE = null
    }
}