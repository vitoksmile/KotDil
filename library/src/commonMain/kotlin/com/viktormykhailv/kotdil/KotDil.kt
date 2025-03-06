package com.viktormykhailv.kotdil

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

    internal val modules = mutableListOf<Module>()

    override fun modules(vararg module: Module) {
        synchronized(this) {
            modules.addAll(module)
        }
    }

    @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_AGAINST_NOT_NOTHING_EXPECTED_TYPE")
    override fun <T> get(name: String): T = synchronized(this) {
        modules.firstOrNull { it.containsProvider(name) }?.get(name)
            ?: throw UnregisteredProviderException(name)
    }
}

/**
 * Run KotDil lifecycle.
 *
 * @see KotDil
 */
fun startKotDil(builder: KotDil.() -> Unit) {
    builder(KotDilImpl)
}

internal fun resetKotDil() {
    KotDilImpl.modules.clear()
}

/**
 * Inject an instance of [T] with [Lazy] delegate.
 */
inline fun <reified T> inject(name: String? = null): Lazy<T> = lazy {
    injectValue<T>(name)
}

/**
 * Inject an instance of [T].
 */
inline fun <reified T> injectValue(name: String? = null): T =
    KotDilImpl.get(name ?: T::class.toString())
