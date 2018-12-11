package com.knighting.lazylogger

import androidx.annotation.IntDef


/**
 * Create by KNightING on 2018/12/11
 */

object LazyLog {

    object Level {
        const val DEBUG = 0
        const val INFO = 1
        const val WARN = 2
        const val ERROR = 3
    }

    @IntDef(Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class LevelAnnotation
}
