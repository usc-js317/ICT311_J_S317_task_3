package com.org.workout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.org.workout.databinding.LayoutItemBinding
import com.org.workout.model.Item
import java.util.ArrayList

class WorkoutAdapter (private var dataSet: ArrayList<Item>, private var listener: Listener?) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {


    interface Listener {
        fun onRemove(data: Item, pos: Int)
        fun onUpdate(data: Item, pos: Int)
    }
    //initialize view item
    inner class ViewHolder(val binding: LayoutItemBinding) : RecyclerView.ViewHolder(binding.root){
        //dataBinding
        fun bind(data: Item, position: Int) {
            binding.data = data
            if(data.group == 0){
                binding.group.text = "Individual";
            }else{
                binding.group.text = "Group Activity";
            }
            binding.deleteBtn.setOnClickListener {
                if (listener != null) listener?.onRemove(data, position)
            }
            binding.updateBtn.setOnClickListener {
                if (listener != null) listener?.onUpdate(data, position)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = LayoutItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataSet[position], position)
    override fun getItemCount() = dataSet.size
}