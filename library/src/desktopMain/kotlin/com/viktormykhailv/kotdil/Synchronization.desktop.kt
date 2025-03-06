package com.viktormykhailv.kotdil

/**
 * Executes the given function [block] while holding the monitor of the given object [lock].
 */
actual fun <T> synchronized(lock: Any, block: () -> T): T {
    return kotlin.synchronized(lock, block)
}
