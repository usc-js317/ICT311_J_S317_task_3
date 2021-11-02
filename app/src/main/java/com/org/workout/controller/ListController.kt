package com.org.workout.controller

import com.org.workout.fragment.ListUI
import com.org.workout.model.DBHelper
import com.org.workout.model.Item

class ListController (private var db : DBHelper, private var view :ListUI ){
    fun loadItems(){
        val items = db.readAll()
        view.getModelList().addAll(items)
        view.getWorkoutAdapter().notifyDataSetChanged()
    }
    fun deleteItem(model : Item, pos : Int){
        if(db.deleteItem(model.id) > 0){
            view.getModelList().remove(model)
            view.getWorkoutAdapter().notifyItemRemoved(pos)
        }
    }
}