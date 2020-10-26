package com.egnize.mylibrary.objects

import android.graphics.drawable.Drawable

/**
 * Created by VINAY'S on 25/10/20.
 * v6kr@outlook.com
 */
class AppData {
    var packageName = ""
    var name: String? = null
    var versionName: String? = null
    var versionCode: String? = null
    var icon: Drawable? = null
    var flags: Int? = null
    var activityName: String? = null
    var permissions: Array<String>? = null
    var obj: Any? = null

    override fun equals(o: Any?): Boolean {
        return if (o is AppData) {
            packageName == o.packageName
        } else false
    }
}