package com.viktormykhailv.kotdil

/**
 * Executes the given function [block] while holding the monitor of the given object [lock].
 */
expect fun <T> synchronized(lock: Any, block: () -> T): T
