package com.alexyuzefovich.expertsys.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexyuzefovich.expertsys.R
import com.alexyuzefovich.expertsys.model.Object
import kotlinx.android.synthetic.main.item_object.view.*

class ObjectsAdapter(
    var onItemUpdatedListener: OnItemUpdatedListener? = null
) : RecyclerView.Adapter<ObjectsAdapter.ViewHolder>() {

    interface OnItemUpdatedListener {
        fun onRemoved(id: Long)
    }

    private val objects: ArrayList<Object> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_object, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = objects.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = objects[position]
        holder.bind(obj)
    }

    fun setObjects(newObjects: List<Object>) {
        objects.clear()
        objects.addAll(newObjects)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        val removingObj = objects[position]
        objects.remove(removingObj)
        notifyItemRemoved(position)
        onItemUpdatedListener?.onRemoved(removingObj.id)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(obj: Object) {
            itemView.apply {
                name.text = obj.name
                characteristics.text = obj.characteristics.joinToString { it }
            }

        }

    }

}