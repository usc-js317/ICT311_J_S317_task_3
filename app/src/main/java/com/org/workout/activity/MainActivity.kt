package com.org.workout.activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.org.workout.R
import com.org.workout.databinding.ActivityMainBinding
import com.org.workout.fragment.ItemUI
import com.org.workout.fragment.ListUI
import com.org.workout.model.Item
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var bind : ActivityMainBinding
    //private var currentUI : Int = 0
    //private var selectedModel : Item? = null

    companion object {
        @JvmStatic var  currentUI : Int = 0
        @JvmStatic var selectedModel : Item? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(currentUI == 1){
            loadItemUI()
        }else if(currentUI == 2){
            selectedModel?.let { loadUpdateItemUI(it) }
        }else{
            loadListUI()
        }
    }
    private fun initView(){
        //set custom app bar
        setSupportActionBar(bind.toolbar)
        supportActionBar?.apply {
            title = "Workout App"
            elevation = 15F
            // toolbar button click listener
            bind.addItem.setOnClickListener {
                // change toolbar title
                loadItemUI()
            }
        }
        loadListUI()
    }
    private fun loadItemUI(){
        bind.addItem.visibility = View.GONE
        bind.toolbar.title = "Add Workout"
        val ui = ItemUI();
        val args = Bundle()
        args.putInt("mode", 0)
        args.putParcelable("model", null)
        ui.arguments = args

        loadFragment(ui);
        currentUI = 1
    }
    fun loadUpdateItemUI(model : Item){
        selectedModel = model
        bind.addItem.visibility = View.GONE
        bind.toolbar.title = "Update Workout"
        val ui = ItemUI();
        val args = Bundle()
        args.putInt("mode", 1)
        args.putParcelable("model", model)
        ui.arguments = args
        loadFragment(ui);
        currentUI = 2
    }
    fun loadListUI(){
        bind.addItem.visibility = View.VISIBLE
        bind.toolbar.title = "Workout App"
        val ui = ListUI();
        loadFragment(ui);
        currentUI = 0;
    }
    private fun loadFragment(fragment: Fragment?){
        if (fragment == null) return
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        tr.replace(bind.rootContainer.id, fragment)
        tr.commit()
    }
    override fun onBackPressed(){
        if(currentUI == 1){
            loadListUI()
        }else{
            super.onBackPressed()
        }
    }
}