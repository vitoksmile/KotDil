package com.vitoksmile.kotdil

import com.vitoksmile.kotdil.Type.Factory
import com.vitoksmile.kotdil.Type.Singleton

/**
 * Describe provider of instances
 *
 * @property provider Provider of instances
 * @property type Type of provider
 */
class Provider(
    private val provider: Any,
    private val type: Type
) {

    val isSingleton: Boolean
        get() = type == Singleton && value != null

    /**
     * Singleton value
     */
    private var value: Any? = null

    /**
     * @return provided value
     */
    fun <T : Any> provide(): T {
        return when {
            // Invoke every time provider to create new instance
            type == Factory -> (provider as Function0<T>).invoke()

            // Create singleton instance
            type == Singleton && value == null -> (provider as Function0<T>).invoke().also { value ->
                this.value = value
                return value
            }

            // Return created earlier singleton instance
            else -> value as T
        }
    }
}

/**
 * Describe type of provider
 */
sealed class Type {
    /**
     * Create instance every time in getting
     */
    object Factory : Type()

    /**
     * Lazy singleton will be replaced to singleton instance after the first getting
     */
    object Singleton : Type()
}