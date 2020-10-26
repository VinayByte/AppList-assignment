package com.egnize.mylibrary

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.egnize.mylibrary.constant.Constant
import com.egnize.mylibrary.interfaces.AppListener
import com.egnize.mylibrary.interfaces.SortListener
import com.egnize.mylibrary.objects.AppData
import com.egnize.mylibrary.task.AppTask
import com.egnize.mylibrary.task.SortTask
import java.lang.ref.WeakReference

/**
 * Created by VINAY'S on 25/10/20.
 * v6kr@outlook.com
 */
class AppList : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != null && context != null) {
            if (action == Intent.ACTION_PACKAGE_ADDED) {
//
            }
        }
    }

    companion object {
        val intentFilter = IntentFilter()

        //Tasks
        private var appTask: AppTask? = null
        private var sortTask: SortTask? = null

        //Listeners - weakreferences
        private var appListener: WeakReference<AppListener>? = null
        private var sortListener: WeakReference<SortListener>? = null

        //Values
        const val BY_APPNAME = 0
        const val BY_PACKAGENAME = 1
        const val BY_APPNAME_IGNORE_CASE = 2
        const val IN_ASCENDING = 0
        const val IN_DESCENDING = 1
        fun registerListeners(appListener: AppListener, sortListener: SortListener) {
            Companion.appListener = WeakReference(appListener)
            Companion.sortListener = WeakReference(sortListener)
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            intentFilter.addDataScheme("package")
        }

        fun getAllApps(context: Context, uniqueIdentifier: Int?) {
            val context1 =
                WeakReference(context)
            appTask = AppTask(
                context1,
                null,
                true,
                null,
                false,
                uniqueIdentifier!!,
                appListener!!
            )
            appTask!!.execute()
        }

        fun sort(
            appDataList: List<AppData>?,
            sortBy: Int?,
            inOrder: Int?,
            uniqueIdentifier: Int?
        ) {
            sortTask = SortTask(
                appDataList,
                sortBy!!,
                inOrder!!,
                uniqueIdentifier!!,
                sortListener!!
            )
            sortTask!!.execute()
        }

        fun destroy() {
            if (appTask != null) {
                if (appTask!!.status !== Constant.Status.FINISHED) {
                    appTask!!.cancel(true)
                }
            }
            if (sortTask != null) {
                if (sortTask!!.status !== Constant.Status.FINISHED) {
                    sortTask!!.cancel(true)
                }
            }
        }
    }
}