package com.knighting.lazylogger

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by KNightING on 2018/12/7
 */

class LazyLoggerManager {

    companion object {
        @JvmStatic
        internal val instance = LazyLoggerManager()

        @JvmStatic
        fun registerFile(vararg files: LazyLogFile) {
            files.forEach {
                val filePath = if (File(it.path).isDirectory) "${it.path}/${it.name}" else it.path
                val maxLevel = if (it.maxLevel < it.minLevel) it.minLevel else it.maxLevel

                instance.logFiles.add(
                    LazyLogFile(
                        it.name,
                        filePath,
                        it.minLevel,
                        maxLevel,
                        it.dateFormat,
                        it.format
                    )
                )
            }
        }

        @JvmStatic
        fun unregisterFile(vararg names: String) {
            names.forEach { name ->
                instance.logFiles.removeAll { it.name == name }
            }
        }

        @JvmStatic
        fun setLagcatTagTemplate(template: String) {
            instance.logcatTagTemplate = template
        }

        @JvmStatic
        fun setLogcatMsgTemplate(template: String) {
            instance.logcatMsgTemplate = template
        }
    }

    private val thread = HandlerThread("lazyLoggerThread")

    private val handler: Handler

    init {
        thread.start()
        handler = Handler(thread.looper)
    }

    private var logcatTagTemplate = "${LazyLogFormat.TAG} [${LazyLogFormat.METHOD}] [${LazyLogFormat.LINE}]"

    private var logcatMsgTemplate = "[${LazyLogFormat.THREAD_NAME}] ${LazyLogFormat.MSG}"

    private val logFiles = mutableListOf<LazyLogFile>()

    fun debug(tag: String, msg: String, trace: StackTraceElement?) {
        dispatchLog(LazyLog.Level.DEBUG, tag, msg, trace)
    }

    fun info(tag: String, msg: String, trace: StackTraceElement?) {
        dispatchLog(LazyLog.Level.INFO, tag, msg, trace)
    }

    fun warn(tag: String, msg: String, trace: StackTraceElement?) {
        dispatchLog(LazyLog.Level.WARN, tag, msg, trace)
    }

    fun error(tag: String, msg: String, trace: StackTraceElement?) {
        dispatchLog(LazyLog.Level.ERROR, tag, msg, trace)
    }

    private fun dispatchLog(
        @LazyLog.LevelAnnotation level: Int,
        tag: String,
        msg: String,
        trace: StackTraceElement?
    ) {
        val date = Date()
        val threadName = Thread.currentThread().name ?: ""
        val catTag = logcatTagTemplate.format(level, tag, msg, threadName, trace, date)
        val catMsg = logcatMsgTemplate.format(level, tag, msg, threadName, trace, date)
        when (level) {
            LazyLog.Level.DEBUG -> Log.d(catTag, catMsg)
            LazyLog.Level.INFO -> Log.i(catTag, catMsg)
            LazyLog.Level.WARN -> Log.w(catTag, catMsg)
            LazyLog.Level.ERROR -> Log.e(catTag, catMsg)
        }

        handler.post {
            writeToFile(level, tag, msg, threadName, trace, date)
        }
    }

    private fun writeToFile(
        level: Int,
        tag: String,
        msg: String,
        threadName: String,
        trace: StackTraceElement?,
        date: Date
    ) {
        logFiles.forEach {
            if (level in it.minLevel..it.maxLevel) {
                File(it.path).run {
                    existsOrNew()
                    appendText(it.format.format(level, tag, msg, threadName, trace, date, it.dateFormat) + "\n")
                }
            }
        }
    }

    private fun String.format(
        @LazyLog.LevelAnnotation level: Int,
        tag: String,
        msg: String,
        threadName: String?,
        trace: StackTraceElement?,
        date: Date,
        dateFormat: String = "yyyy/MM/dd HH:mm:ss.SSS"
    ): String {
        return replace(LazyLogFormat.METHOD, trace?.methodName ?: "")
            .replace(LazyLogFormat.LINE, trace?.lineNumber.toString())
            .replace(LazyLogFormat.THREAD_NAME, threadName ?: "")
            .replace(LazyLogFormat.LEVEL, getLevel(level))
            .replace(LazyLogFormat.DATE, date.toString(dateFormat) ?: "")
            .replace(LazyLogFormat.MSG, msg)
            .replace(LazyLogFormat.TAG, tag)
    }

    private fun getLevel(@LazyLog.LevelAnnotation level: Int): String {
        return when (level) {
            LazyLog.Level.DEBUG -> "D"
            LazyLog.Level.INFO -> "I"
            LazyLog.Level.WARN -> "W"
            LazyLog.Level.ERROR -> "E"
            else -> ""
        }
    }

    private fun File.new(): Boolean {
        return if (del())
            createNewFile()
        else
            false
    }

    private fun File.existsOrNew(): Boolean {
        if (exists()) return true
        return new()
    }

    private fun File.del(): Boolean {
        if (!exists()) return true
        return delete()
    }

    private fun Date.toString(dateFormat: String) = SimpleDateFormat(dateFormat, Locale.getDefault()).format(this)

}