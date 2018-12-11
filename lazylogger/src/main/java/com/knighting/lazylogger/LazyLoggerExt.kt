package com.knighting.lazylogger

/**
 * Create by KNightING on 2018/12/6
 */

fun LazyLogger.ILazyLogger.debug(msg: String) {
    logger.d(msg, getTraceElement())
}

fun LazyLogger.ILazyLogger.info(msg: String) {
    logger.i(msg, getTraceElement())
}

fun LazyLogger.ILazyLogger.warn(msg: String) {
    logger.w(msg, getTraceElement())
}

fun LazyLogger.ILazyLogger.error(msg: String) {
    logger.e(msg, getTraceElement())
}

fun LazyLogger.ILazyLogger.error(ex: Exception) {
    logger.e(ex, getTraceElement())
}

private fun getTraceElement(): StackTraceElement? {
    return Throwable().stackTrace.run {
        return@run if (size >= 2) get(2) else get(0)
    }
}


