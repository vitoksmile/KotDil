package com.viktormykhailv.kotdil

import kotlinx.cinterop.ExperimentalForeignApi
import platform.objc.objc_sync_enter
import platform.objc.objc_sync_exit

/**
 * Executes the given function [block] while holding the monitor of the given object [lock].
 */
@OptIn(ExperimentalForeignApi::class)
actual fun <T> synchronized(lock: Any, block: () -> T): T {
    return kotlinx.cinterop.memScoped {
        objc_sync_enter(lock)
        try {
            block()
        } finally {
            objc_sync_exit(lock)
        }
    }
}
