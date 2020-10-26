package com.egnize.mylibrary.task

import android.util.Log
import com.egnize.mylibrary.constant.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by VINAY'S on 25/10/20.
 * v6kr@outlook.com
 */
abstract class CoroutinesAsyncTask<Params, Progress, Result> {

    var status: Constant.Status = Constant.Status.PENDING
    abstract fun doInBackground(vararg params: Params?): Result
    open fun onProgressUpdate(vararg values: Progress?) {}
    open fun onPostExecute(result: Result?) {}
    open fun onPreExecute() {}
    open fun onCancelled(result: Result?) {}
    protected var isCancelled = false

    fun execute(vararg params: Params) {

        if (status != Constant.Status.PENDING) {
            when (status) {
                Constant.Status.RUNNING -> throw IllegalStateException("Cannot execute task:" + " the task is already running.")
                Constant.Status.FINISHED -> throw IllegalStateException(
                    "Cannot execute task:"
                            + " the task has already been executed "
                            + "(a task can be executed only once)"
                )
            }
        }

        status = Constant.Status.RUNNING

        // it can be used to setup UI - it should have access to Main Thread
        GlobalScope.launch(Dispatchers.Main) {
            onPreExecute()
        }

        // doInBackground works on background thread(default)
        GlobalScope.launch(Dispatchers.Default) {
            val result = doInBackground(*params)
            status = Constant.Status.FINISHED
            withContext(Dispatchers.Main) {
                // onPostExecute works on main thread to show output
                Log.d("Alpha", "after do in back " + status.name + "--" + isCancelled)
                if (!isCancelled) {
                    onPostExecute(result)
                }
            }
        }
    }

    fun cancel(mayInterruptIfRunning: Boolean) {
        isCancelled = true
        status = Constant.Status.FINISHED
        GlobalScope.launch(Dispatchers.Main) {
            // onPostExecute works on main thread to show output
            Log.d("Alpha", "after cancel " + status.name + "--" + isCancelled)
            onPostExecute(null)
        }
    }

    fun publishProgress(vararg progress: Progress) {
        //need to update main thread
        GlobalScope.launch(Dispatchers.Main) {
            if (!isCancelled) {
                onProgressUpdate(*progress)
            }
        }
    }
}