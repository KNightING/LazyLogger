package com.knighting.lazylogger

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.Exception

/**
 * Create by KNightING on 2018/12/6
 */

class LazyLogger private constructor(private val TAG: String) {

    interface ILazyLogger {
        val logger: LazyLogger
    }

    companion object {
        fun get(TAG: String) = lazy { LazyLogger(TAG) }
    }

    private val logManager = LazyLoggerManager.instance

    fun d(msg: String) {
        d(msg, getTraceElement())
    }

    internal fun d(msg: String?, trace: StackTraceElement?) {
        logManager.debug(TAG, msg ?: "log message is null", trace)
    }

    fun w(msg: String) {
        w(msg, getTraceElement())
    }

    internal fun w(msg: String?, trace: StackTraceElement?) {
        logManager.warn(TAG, msg ?: "log message is null", trace)
    }

    fun i(msg: String) {
        i(msg, getTraceElement())
    }

    internal fun i(msg: String?, trace: StackTraceElement?) {
        logManager.info(TAG, msg ?: "log message is null", trace)
    }

    fun e(msg: String?) {
        e(msg, getTraceElement())
    }

    fun e(ex: Exception) {
        e(ex, getTraceElement())
    }

    internal fun e(msg: String?, trace: StackTraceElement?) {
        logManager.error(TAG, msg ?: "log message is null", trace)
    }

    internal fun e(ex: Exception?, trace: StackTraceElement?) {
        val outputStream = ByteArrayOutputStream()
        val print = PrintStream(outputStream)
        ex?.printStackTrace(print)
        e(outputStream.toString("UTF-8"), trace)
    }

    private fun getTraceElement(): StackTraceElement? {
        return Throwable().stackTrace.run {
            return@run if (size >= 2) get(2) else get(0)
        }
    }
}