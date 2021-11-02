package com.org.workout.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.org.workout.activity.MainActivity
import com.org.workout.adapter.WorkoutAdapter
import com.org.workout.controller.ListController
import com.org.workout.databinding.FragmentListUiBinding
import com.org.workout.model.DBHelper
import com.org.workout.model.Item
import java.util.ArrayList

class ListUI : Fragment() {
    lateinit var bind: FragmentListUiBinding
    lateinit var adapter : WorkoutAdapter
    private val dataList = ArrayList<Item>()
    lateinit var  controller : ListController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentListUiBinding.inflate(inflater, container, false)
        initView()
        return bind.root
    }
    private fun initView(){
        controller = ListController(DBHelper(requireContext()), this)

        adapter = WorkoutAdapter(dataList, object : WorkoutAdapter.Listener {
            override fun onRemove(data: Item, pos: Int) {
                deleteConfirm(data, pos)
            }
            override fun onUpdate(data: Item, pos: Int) {
                (activity as MainActivity?)!!.loadUpdateItemUI(data)
            }
        })
        val layoutManager = LinearLayoutManager(requireContext())
        bind.itemList.layoutManager = layoutManager
        bind.itemList.itemAnimator = DefaultItemAnimator()
        bind.itemList.adapter = adapter
        //load database
        loadItems();
    }
    fun getModelList() : ArrayList<Item> {
        return dataList;
    }
    fun getWorkoutAdapter() : WorkoutAdapter{
        return adapter;
    }
    private fun loadItems(){
        controller.loadItems()
    }
    fun deleteConfirm(data : Item, pos : Int){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete item ?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                controller.deleteItem(data, pos)
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
    private fun showToast(message : String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}