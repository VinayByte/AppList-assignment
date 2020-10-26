package com.egnize.mylibrary.task

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.egnize.mylibrary.interfaces.AppListener
import com.egnize.mylibrary.objects.AppData
import java.lang.ref.WeakReference
import java.util.*


/**
 * Created by VINAY'S on 25/10/20.
 * v6kr@outlook.com
 */
class AppTask(
    private val contextWeakReference: WeakReference<Context>,
    private val flags: Int?,
    private val match: Boolean,
    private val permissions: Array<String?>?,
    private val matchPermissions: Boolean,
    private val uniqueIdentifier: Int,
    private val allAppsListener: WeakReference<AppListener>
) : CoroutinesAsyncTask<Void, Void?, List<AppData>?>() {
    override fun doInBackground(vararg voids: Void?): List<AppData>? {
        val context1 = contextWeakReference.get()
        if (context1 != null) {
            val packageManager = context1.packageManager
            val applicationInfoList =
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val appDataList: MutableList<AppData> = ArrayList()
            for (applicationInfo in applicationInfoList) {
                val app = AppData()
                app.name = applicationInfo.loadLabel(packageManager).toString()
                app.packageName = applicationInfo.packageName
                app.icon = applicationInfo.loadIcon(packageManager)
                app.flags = applicationInfo.flags
                app.activityName =
                    getLauncherActivityName(packageManager, applicationInfo.packageName)
                val pInfo: PackageInfo =
                    packageManager.getPackageInfo(applicationInfo.packageName, 0)
                app.versionName = pInfo.versionName
                val versionCode =
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) pInfo?.versionCode?.toLong()
                    else pInfo?.longVersionCode
                app.versionCode = versionCode.toString()
                var containsPermission = false
                try {
                    val packageInfo = packageManager.getPackageInfo(
                        applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS
                    )
                    val requestedPermissions = packageInfo.requestedPermissions
                    if (permissions != null) {
                        if (requestedPermissions != null) {
                            for (requestedPermission in requestedPermissions) {
                                for (permission in permissions) {
                                    if (requestedPermission == permission) {
                                        containsPermission = true
                                        break
                                    }
                                }
                                if (containsPermission) break
                            }
                        }
                    } else {
                        containsPermission = matchPermissions
                    }
                    app.permissions = requestedPermissions
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    if (permissions == null) containsPermission = matchPermissions
                }
                if (containsPermission && matchPermissions || !containsPermission && !matchPermissions) {
                    if (match) {
                        if (flags == null || applicationInfo.flags and flags != 0) appDataList.add(
                            app
                        )
                    } else {
                        if (flags == null || applicationInfo.flags and flags == 0) appDataList.add(
                            app
                        )
                    }
                }
                if (isCancelled) break
            }
            return appDataList
        }
        return null
    }

    override fun onPostExecute(appDataList: List<AppData>?) {
        val listener = allAppsListener.get()
        listener?.appListener(
            appDataList,
            flags,
            match,
            permissions,
            matchPermissions,
            uniqueIdentifier
        )
    }

    private fun getLauncherActivityName(pm: PackageManager, packageName: String): String? {
        var activityName = ""
        val intent = pm.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            val activityList = pm.queryIntentActivities(intent, 0)
            if (activityList != null) {
//            activityName = activityList[0].activityInfo.name
                activityName = activityList[0].activityInfo.name.split(".").last()
            }
        }
        return activityName
    }

}