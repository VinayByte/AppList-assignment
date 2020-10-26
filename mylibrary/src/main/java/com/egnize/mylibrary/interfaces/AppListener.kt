package com.egnize.mylibrary.interfaces

import com.egnize.mylibrary.objects.AppData

/**
 * Created by VINAY'S on 25/10/20.
 * v6kr@outlook.com
 */
interface AppListener {
    fun appListener(
        appDataList: List<AppData>?,
        applicationFlags: Int?,
        applicationFlagsMatch: Boolean?,
        permissions: Array<String?>?,
        matchPermissions: Boolean?,
        uniqueIdentifier: Int?
    )
}