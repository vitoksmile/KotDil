package com.vitoksmile.kotdil

internal interface Builder<T> {
    fun build(): T
}

/**
 * Create a new instance of [T] every time.
 */
internal class FactoryBuilder<T>(
    private val builder: () -> T
) : Builder<T> {
    override fun build() = builder()
}

/**
 * Create only one instance of [T].
 */
internal class SingletonBuilder<T>(
    private val builder: () -> T
) : Builder<T> {
    private var value: T? = null

    override fun build(): T = synchronized(this) {
        if (value == null) value = builder()
        return value ?: throw IllegalStateException("Failed to init singleton value.")
    }
}
