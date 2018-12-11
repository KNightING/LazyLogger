package com.knighting.lazyloggerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.knighting.lazylogger.*

class MainActivity : AppCompatActivity(), LazyLogger.ILazyLogger {

    val TAG by lazy { this::class.java.simpleName }

    private val allLogFilePath by lazy { application.getExternalFilesDir("log")?.absolutePath + "/allLog.log" }

    private val infoLogFilePath by lazy { application.getExternalFilesDir("log")?.absolutePath + "/info.log" }

    private val myFormatLog by lazy {
        application.getExternalFilesDir("log")?.absolutePath + "/myFormatLog.log"
    }

    override val logger by LazyLogger.get(TAG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LazyLoggerManager.registerFile(
            LazyLogFile("AllLog", allLogFilePath),
            LazyLogFile("InfoLog", infoLogFilePath, LazyLog.Level.INFO, LazyLog.Level.INFO),
            LazyLogFile(
                "myFormatLog",
                myFormatLog,
                dateFormat = "yyyyMMddHHmmss",
                format = "${LazyLogFormat.DATE} ${LazyLogFormat.TAG} : ${LazyLogFormat.MSG}"
            )
        )

        var count = 0
        findViewById<TextView>(R.id.btnWriteLog).setOnClickListener {
            count++
            val msg = "click $count times"
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()

            logger.d(msg)
            logger.i(msg)
            logger.w(msg)
            logger.e(msg)

            //if you implement LazyLogger.ILazyLogger than you can
            debug(msg)
            info(msg)
            warn(msg)
            error(msg)

            try {
                throw Exception("test error")
            } catch (ex: Exception) {
                error(ex)
                logger.e(ex)
            }
        }

    }
}
