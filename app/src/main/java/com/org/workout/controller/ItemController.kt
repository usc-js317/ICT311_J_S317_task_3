package com.org.workout.controller

import com.org.workout.fragment.ItemUI
import com.org.workout.model.DBHelper
import com.org.workout.model.Item

class ItemController(private var db: DBHelper, private var model: Item?, private var view: ItemUI) {

    fun loadModel(){
        view.getUI().title.setText(model?.title)
        view.getUI().place.setText(model?.place)
        view.getUI().date.text = model?.date
        view.getUI().startTime.text = model?.startTime
        view.getUI().endTime.text = model?.endTime

        if(model?.group == 0){
            view.getUI().individual.isChecked = true;
        }else{
            view.getUI().group.isChecked = true;
        }
    }

    fun addItem(mode : Int) : Boolean{

        val title = view.getUI().title.text.toString().trim()
        val place = view.getUI().place.text.toString().trim()
        val date = view.getUI().date.text.toString();
        val startTime = view.getUI().startTime.text.toString()
        val endTime = view.getUI().endTime.text.toString()
        var group = 0;
        if(view.getUI().group.isChecked){
            group = 1;
        }
        if(title.isEmpty()){
            view.getUI().title.error = "Enter title"
            return false;
        }
        if(date.isEmpty()){
            view.getUI().date.error = "Select date"
            return false;
        }
        if(place.isEmpty()){
            view.getUI().place.error = "Enter place"
            return false;
        }
        if(startTime.isEmpty()){
            view.getUI().startTime.error = "Select start time"
            return false;
        }
        if(endTime.isEmpty()){
            view.getUI().endTime.error = "Select end time"
            return false;
        }
        val item = Item(0, title, date, place, startTime, endTime, group)

        if(mode == 0){
            //add item
            val id = db.addItem(item);
            return id >= 0
        }else if(mode == 1){
            //update item
            return model?.id?.let { db.updateItem(it, item) }!! > 0
        }
        return false
    }
}