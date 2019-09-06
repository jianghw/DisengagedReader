package android.app

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.magnificent.jianghw.jetpack.IMyAidlInterface
import java.util.*

class AlarmManagerService : Service() {

    private lateinit var myStub: MyStaub
    private var timer = Timer()


    private var alarmCount: Int = 0
    private var quite: Boolean = false

    inner class AlarmBinder : Binder() {
        val count: Int
            get() = this@AlarmManagerService.alarmCount
    }

    val mAlarmBinder = AlarmBinder()

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(this@AlarmManagerService::class.java.simpleName, this::onBind.name)
        return myStub
    }

    override fun onCreate() {
        super.onCreate()
        object : Thread() {
            override fun run() {
                while (!quite) {
                    sleep(1000)
                    this@AlarmManagerService.alarmCount++
                }
            }
        }.start()

        myStub = MyStaub()
        timer.schedule(object : TimerTask() {
            override fun run() {
                this@AlarmManagerService.alarmCount++
            }
        },0,800)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(this@AlarmManagerService::class.java.simpleName, this::onStartCommand.name)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        //return super.onUnbind(intent)
        Log.d(this@AlarmManagerService::class.java.simpleName, this::onUnbind.name)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(this@AlarmManagerService::class.java.simpleName, this::onDestroy.name)
        this.quite = true
    }

    /**
     * 服务端
     */
    inner class MyStaub : IMyAidlInterface.Stub() {

        override fun basicTypes(
            anInt: Int, aLong: Long, aBoolean: Boolean,
            aFloat: Float, aDouble: Double, aString: String?
        ) {
        }

        override fun setTime(millis: Long): Boolean {
            return true
        }

        override fun currentNetworkTimeMillis(): Long {
            return this@AlarmManagerService.alarmCount.toLong()
        }
    }
}