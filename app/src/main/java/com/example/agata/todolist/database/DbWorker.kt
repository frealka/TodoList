package com.example.agata.todolist.database

import android.os.HandlerThread
import android.os.Handler

class DbWorker (threadName: String) : HandlerThread(threadName){
    // The Handler used to access the data base
    private lateinit var mWorkerThread: Handler
    // Used to make sure that the Handler was initialized
    private var initialized : Boolean = false

    /**
     * Initializes the Handler once the Looper of the HandlerThread is ready
     */
    override fun onLooperPrepared() {
        super.onLooperPrepared()
        mWorkerThread = Handler(looper)
        initialized = true
    }

    /**
     * Used to pass tasks to the handler
     *
     * @param task The task that will be passed
     */
    fun postTask(task: Runnable) {
        Thread {
            while (!initialized) { }
            mWorkerThread.post(task)
        }.start()
    }
}