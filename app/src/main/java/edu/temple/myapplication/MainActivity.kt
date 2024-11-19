package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var timerTextView: TextView
    lateinit var button: Button

    private var timeBinder : TimerService.TimerBinder? = null

    val timerHandler = Handler(Looper.getMainLooper()) {
        timerTextView.text = it.what.toString()
        true
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            timeBinder = service as TimerService.TimerBinder
            timeBinder?.setHandler(timerHandler)

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timeBinder = null

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.textView)

        button = findViewById(R.id.startButton)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )


           button.setOnClickListener {



                if(timeBinder?.isRunning == false && timeBinder?.paused == false){

                    timeBinder?.start(100)
                    button.text= "pause"

                }
                else {
                    timeBinder?.pause()
                    timeBinder?.run {
                        if (paused) {
                            button.text= "unPaused"
                        }
                        else{
                            button.text= "Pause"
                        }
                    }

                }


            }

            findViewById<Button>(R.id.stopButton).setOnClickListener {

                    timeBinder?.stop()
                    button.text="start"

            }

        }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }


    }

