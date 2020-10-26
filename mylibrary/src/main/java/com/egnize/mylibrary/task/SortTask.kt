package com.egnize.mylibrary.task

import com.egnize.mylibrary.interfaces.SortListener
import com.egnize.mylibrary.objects.AppData
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by VINAY'S on 25/10/20.
 * v6kr@outlook.com
 */
class SortTask(
    private val appDataList: List<AppData>?,
    private val sortBy: Int,
    private val inOrder: Int,
    private val uniqueIdentifier: Int,
    private val sortListener: WeakReference<SortListener>
) : CoroutinesAsyncTask<Void, Void, List<AppData>?>() {
    override fun doInBackground(vararg voids: Void?): List<AppData>? {
        if (sortBy == 0 && inOrder == 0) {
            Collections.sort(
                appDataList
            ) { t0, t1 -> t0.name!!.compareTo(t1.name!!) }
        } else if (sortBy == 0 && inOrder == 1) {
            Collections.sort(
                appDataList
            ) { t0, t1 -> t1.name!!.compareTo(t0.name!!) }
        } else if (sortBy == 1 && inOrder == 0) {
            Collections.sort(
                appDataList
            ) { t0, t1 -> t0.packageName.compareTo(t1.packageName) }
        } else if (sortBy == 1 && inOrder == 1) {
            Collections.sort(
                appDataList
            ) { t0, t1 -> t1.packageName.compareTo(t0.packageName) }
        } else if (sortBy == 2 && inOrder == 0) {
            Collections.sort(
                appDataList
            ) { t0, t1 -> t0.name!!.toLowerCase().compareTo(t1.name!!.toLowerCase()) }
        } else if (sortBy == 2 && inOrder == 1) {
            Collections.sort(
                appDataList
            ) { t0, t1 -> t1.name!!.toLowerCase().compareTo(t0.name!!.toLowerCase()) }
        }
        return appDataList
    }

    override fun onPostExecute(appDataList: List<AppData>?) {
        val listener = sortListener.get()
        listener?.sortListener(appDataList, sortBy, inOrder, uniqueIdentifier)
    }

}