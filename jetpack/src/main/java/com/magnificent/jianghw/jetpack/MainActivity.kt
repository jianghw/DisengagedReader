package com.magnificent.jianghw.jetpack

import android.app.AlarmManagerService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        const val LOG_TAG: String = "MainActivity"
    }

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    textMessage.setText(R.string.title_home)
                    //bindService(bindIntent, conn, Context.BIND_AUTO_CREATE)
                    startService(bindIntent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    textMessage.setText(R.string.title_dashboard)
                    //unbindService(conn)
                    stopService(bindIntent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    //textMessage.setText(R.string.title_notifications)
                    //textMessage.setText("count::${binderStub.currentNetworkTimeMillis()}")
                    binder.removeMessage()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private lateinit var bindIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


        bindIntent = Intent(this, AlarmManagerService::class.java)

    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, this::onStart.name)
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, this::onResume.name)
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, this::onPause.name)
    }

    class MainObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun doStart() {
            Log.d(LOG_TAG, this::doStart.name)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun doResume() {
            Log.d(LOG_TAG, this::doResume.name)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun doPause() {
            Log.d(LOG_TAG, this::doPause.name)
        }
    }


    internal class CustomLocationListener(
        private val context: Context, private val callback: (Location) -> Unit
    ) {}


    private lateinit var binder: AlarmManagerService.AlarmBinder
    private lateinit var binderStub: IMyAidlInterface

    private val conn = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(this@MainActivity::class.java.simpleName, this::onServiceDisconnected.name)
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d(this@MainActivity::class.java.simpleName, this::onServiceConnected.name)
            binder = p1 as AlarmManagerService.AlarmBinder
            //binderStub=IMyAidlInterface.Stub.asInterface(p1)
        }
    }
}
