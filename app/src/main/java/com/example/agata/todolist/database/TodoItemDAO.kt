package com.example.agata.todolist.database

import android.arch.persistence.room.*
import com.example.agata.todolist.recycler.CardItem

@Dao
interface TodoItemDAO {

    @Query("SELECT * FROM todoItems")
    fun getAllItems() : MutableList<CardItem>

    /**
     * Inserts an arbitrary number of transactions into the data base
     * If a transaction with the same ID as the one given already exists in the data base
     * it will be overridden
     *
     * @param items An arbitrary number of transactions
     * @return List containing IDs of all added entries
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: CardItem) : List<Long>

    /**
     * Deletes the specified transaction from the data base
     *
     * @param t The transaction to be deleted
     */
    @Delete
    fun delete(t: CardItem)

}