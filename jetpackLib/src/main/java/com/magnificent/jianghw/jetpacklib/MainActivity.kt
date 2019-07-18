package com.magnificent.jianghw.jetpacklib

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.common.Lifecycle
import androidx.lifecycle.common.LifecycleOwner
import androidx.lifecycle.runtime.LifecycleRegistry
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        const val TAG: String = "xxxx"
    }

    private lateinit var lifecycleRegistry: LifecycleRegistry
    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        lifecycleRegistry = LifecycleRegistry(this)
        val mainObserver = MainObserver(lifecycleRegistry, object : MainObserverCallback {
            override fun doPrintCreate() {
                Log.e(TAG, this::doPrintCreate.name)
            }

            override fun doPrintStart() {
                Log.e(TAG, this::doPrintStart.name)
            }

            override fun doPrintResume() {
                Log.e(TAG, this::doPrintResume.name)
            }

        })
        lifecycleRegistry.addObserver(mainObserver)

        lifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,this::onStart.name)
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        Log.i(TAG,this::onStart.name)
    }

    override fun onResume() {
        super.onResume()
        lifecycleRegistry.markState(Lifecycle.State.RESUMED)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}
