package com.egnize.applist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.egnize.applist.adapter.RecyclerViewAdapter
import com.egnize.mylibrary.AppList
import com.egnize.mylibrary.interfaces.AppListener
import com.egnize.mylibrary.interfaces.SortListener
import com.egnize.mylibrary.objects.AppData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity(), AppListener, SortListener {

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private val appDataList: MutableList<AppData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        AppList.registerListeners(this, this)
        AppList.getAllApps(this, 0)
        progressBar.visibility = View.VISIBLE

        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerViewAdapter(appDataList, this)
        recyclerView.adapter = adapter

        searchView?.onActionViewExpanded()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (!query.isNullOrBlank()) {
                    filterApp(query)
                } else {
                    adapter.setData(appDataList)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length > 3) {
                    filterApp(newText)
                }
                if (newText.isEmpty()) {
                    AppList.getAllApps(this@MainActivity, 0)
//                    adapter.setData(appDataList)
                }

                return true
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun appListener(
        appDataList: List<AppData>?,
        applicationFlags: Int?,
        applicationFlagsMatch: Boolean?,
        permissions: Array<String?>?,
        matchPermissions: Boolean?,
        uniqueIdentifier: Int?
    ) {
        AppList.sort(
            appDataList,
            AppList.BY_APPNAME_IGNORE_CASE,
            AppList.IN_ASCENDING,
            uniqueIdentifier
        )
    }

    override fun sortListener(
        list: List<AppData>?,
        sortBy: Int?,
        inOrder: Int?,
        uniqueIdentifier: Int?
    ) {
        if (!list.isNullOrEmpty()) {
            appDataList.clear()
            appDataList.addAll(list)
            adapter.setData(list)
        }

        progressBar.visibility = View.GONE
    }

    private fun filterApp(query: String?) {
        val filteredValues = ArrayList<AppData>()
        for (i in appDataList.indices) {
            val item = appDataList[i]
            if (query?.toLowerCase()?.trim { it <= ' ' }?.let {
                    item.name?.toLowerCase()?.trim { it <= ' ' }?.contains(
                        it
                    )
                }!!) {
                filteredValues.add(item)
            }
        }
        adapter.setData(filteredValues)
    }


}