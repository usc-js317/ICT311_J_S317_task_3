package com.org.workout.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.org.workout.R
import com.org.workout.activity.MainActivity
import com.org.workout.controller.ItemController
import com.org.workout.databinding.FragmentItemUiBinding
import com.org.workout.model.DBHelper
import com.org.workout.model.Item
import java.text.SimpleDateFormat
import java.util.*

class ItemUI : Fragment(), View.OnClickListener {
    lateinit var bind: FragmentItemUiBinding
    lateinit var calendar : Calendar
    lateinit var controller : ItemController
    var mode : Int = 0
    var model : Item? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bundle = arguments
        if (bundle != null) {
            mode = bundle.getInt("mode")
            if(mode == 1){
                model = bundle.getParcelable<Item>("model")!!
            }
        }
        bind = FragmentItemUiBinding.inflate(inflater, container, false)
        initView()
        return bind.root
    }
    private fun initView(){
        controller = ItemController(DBHelper(requireContext()), model, this)

        calendar = Calendar.getInstance()
        if(mode == 1){
            loadModel();
            bind.addBtn.text = getString(R.string.update)
        }
        bind.addBtn.setOnClickListener(this)
        bind.cancelBtn.setOnClickListener(this)
        bind.date.setOnClickListener(this)
        bind.startTime.setOnClickListener(this)
        bind.endTime.setOnClickListener(this)
    }
    fun getUI() : FragmentItemUiBinding {
        return bind
    }
    private fun loadModel(){
        controller.loadModel()
    }
    override fun onClick(view: View) {
        when(view){
            bind.addBtn->{
                if(mode == 0){
                    addItem()
                }else{
                    updateConfirm()
                }
            }
            bind.cancelBtn->{
                (activity as MainActivity?)!!.onBackPressed()
            }
            bind.date->{
                showDatePicker(bind.date)
            }
            bind.startTime->{
                showTimePicker(bind.startTime)
            }
            bind.endTime->{
                showTimePicker(bind.endTime)
            }
        }
    }
    private fun addItem(){
        if(controller.addItem(mode)){
            if(mode == 0){
                showToast("Success to save workout")
                (activity as MainActivity?)!!.loadListUI()
            }else{
                showToast("Success to update workout")
                (activity as MainActivity?)!!.loadListUI()
            }
        }else{
            if(mode == 0){
                showToast("Fail to save workout")
            }else{
                showToast("Fail to update workout")
            }
        }
    }
    private fun updateConfirm(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to update item ?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                addItem()
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
                (activity as MainActivity?)!!.onBackPressed()
            }
        val alert = builder.create()
        alert.show()
    }
    private fun showDatePicker(dateView : TextView){
        DatePickerDialog(requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val  ft = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
                dateView.text = ft.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
    private fun showTimePicker(timeView : TextView){
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            timeView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
        }
        TimePickerDialog(requireContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }
    private fun showToast(message : String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}