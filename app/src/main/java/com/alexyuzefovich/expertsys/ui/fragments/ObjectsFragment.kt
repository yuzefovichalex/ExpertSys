package com.alexyuzefovich.expertsys.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.alexyuzefovich.expertsys.R
import com.alexyuzefovich.expertsys.util.WidgetUtil
import com.alexyuzefovich.expertsys.adapter.ObjectsAdapter
import com.alexyuzefovich.expertsys.itemtouchhelper.SwipeToDeleteCallback
import com.alexyuzefovich.expertsys.model.Object
import com.alexyuzefovich.expertsys.repository.ObjectsRepository
import kotlinx.android.synthetic.main.dialog_add_object.view.*
import kotlinx.android.synthetic.main.fragment_objects.*

class ObjectsFragment : Fragment() {

    private val adapter: ObjectsAdapter = ObjectsAdapter(object : ObjectsAdapter.OnItemUpdatedListener {
        override fun onRemoved(id: Long) {
            removeObject(id)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_objects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initClickListeners()
        initRecyclerView()
    }

    private fun initClickListeners() {
        addObject.setOnClickListener {
            createAddingObjectDialog()?.show()
        }
    }

    @SuppressLint("InflateParams")
    private fun createAddingObjectDialog(): AlertDialog? {
        val context = this.context ?: return null
        val dialogLayout = LayoutInflater.from(context)
            .inflate(R.layout.dialog_add_object, null)
        return AlertDialog.Builder(context)
            .setTitle(R.string.add_object)
            .setView(dialogLayout)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                val name = dialogLayout.name.text.toString()
                val characteristicsStr = dialogLayout.characteristics.text
                val characteristics = characteristicsStr?.split(',')?.filter { it.isNotEmpty() }
                    ?: emptyList()
                addNewObject(name, characteristics)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    private fun addNewObject(name: String, characteristics: List<String>) {
        val context = this.context ?: return
        if (name.isEmpty()) {
            WidgetUtil.showToast(
                context,
                getString(R.string.name_warning)
            )
            return
        }
        if (characteristics.isEmpty() || characteristics.all { it.isEmpty() }) {
            WidgetUtil.showToast(
                context,
                getString(R.string.characteristics_warning)
            )
            return
        }
        val id = System.currentTimeMillis()
        val obj = Object(id, name, characteristics)
        val objectList = ObjectsRepository.readObjects(context)
        val same = objectList.find { it.name.equals(obj.name, true) }
        if (same == null) {
            objectList.add(obj)
            ObjectsRepository.writeObjects(context, objectList)
            adapter.setObjects(objectList)
        } else {
            WidgetUtil.showToast(
                context,
                getString(R.string.already_exists_warning)
            )
        }
    }

    private fun removeObject(id: Long) {
        val context = this.context ?: return
        val objectList = ObjectsRepository.readObjects(context)
        val removingObj = objectList.find { it.id == id }
        objectList.remove(removingObj)
        ObjectsRepository.writeObjects(context, objectList)
    }

    private fun initRecyclerView() {
        val context = this.context ?: return
        objectListRV.adapter = adapter.apply {
            setObjects(ObjectsRepository.readObjects(context))
        }
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(objectListRV)
    }

}