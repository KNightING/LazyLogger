package com.knighting.lazylogger

/**
 * Create by KNightING on 2018/12/11
 */

data class LazyLogFile(
    val name: String,
    val path: String,
    @LazyLog.LevelAnnotation val minLevel: Int = LazyLog.Level.DEBUG,
    @LazyLog.LevelAnnotation val maxLevel: Int = LazyLog.Level.ERROR,
    val dateFormat: String = "yyyy/MM/dd HH:mm:ss.SSS",
    val format: String = "${LazyLogFormat.DATE} [${LazyLogFormat.THREAD_NAME}] [${LazyLogFormat.LEVEL}] ${LazyLogFormat.TAG} [${LazyLogFormat.METHOD}] [${LazyLogFormat.LINE}] ${LazyLogFormat.MSG}"
)