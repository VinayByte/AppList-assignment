package com.egnize.mylibrary.interfaces

import com.egnize.mylibrary.objects.AppData

/**
 * Created by VINAY'S on 25/10/20.
 * v6kr@outlook.com
 */
interface SortListener {
    fun sortListener(
        appDataList: List<AppData>?,
        sortBy: Int?,
        inOrder: Int?,
        uniqueIdentifier: Int?
    )
}