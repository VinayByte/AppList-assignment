package com.egnize.applist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.egnize.applist.R
import com.egnize.mylibrary.objects.AppData
import kotlinx.android.synthetic.main.item_view.view.*
import java.util.ArrayList

class RecyclerViewAdapter(val items: MutableList<AppData>, val context: AppCompatActivity) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], context)

    override fun getItemCount(): Int = items.size
    fun setData(appDataList: List<AppData>?) {
        items.clear()
        if (!appDataList.isNullOrEmpty()) {
            items.addAll(appDataList)
            notifyDataSetChanged()
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: AppData,
            context: AppCompatActivity
        ) = with(itemView) {
            itemName.text = "App name:${item.name}"
            itemPackageName.text = "versionName:${item.versionName},\n" +
                    "versionCode:${item.versionCode},\n Pkg:${item.packageName},\n" +
                    "Main Activity class name:${item.activityName}"
            itemLogo.setImageDrawable(item.icon)
            itemView.setOnClickListener {
                val intent = context.packageManager.getLaunchIntentForPackage(item.packageName)
                intent?.let { context.startActivity(it) }
            }

        }
    }
}