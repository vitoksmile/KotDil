package com.vitoksmile.kotdil

import java.util.concurrent.CopyOnWriteArraySet

interface KotDil {
    /**
     * Register these [module].
     *
     * @see Module
     */
    fun modules(vararg module: Module)

    /**
     * Provide an instance of [T] from other modules.
     */
    fun <T> get(name: String): T
}

object KotDilImpl : KotDil {
    internal val modules = CopyOnWriteArraySet<Module>()

    override fun modules(vararg module: Module) {
        this.modules.addAll(module)
    }

    override fun <T> get(name: String): T {
        return modules.firstOrNull { it.containsProvider(name) }?.get(name)
            ?: throw UnregisteredProviderException(name)
    }
}

/**
 * Run KotDil lifecycle.
 *
 * @see KotDil
 */
fun startKotDil(builder: KotDil.() -> Unit) = builder(KotDilImpl)

internal fun resetKotDil() {
    KotDilImpl.modules.clear()
}

/**
 * Inject an instance of [T] with [Lazy] delegate.
 */
inline fun <reified T> inject(name: String? = null) = lazy {
    injectValue<T>(name)
}

/**
 * Inject an instance of [T].
 */
inline fun <reified T> injectValue(name: String? = null): T =
    KotDilImpl.get(name ?: T::class.java.name)
