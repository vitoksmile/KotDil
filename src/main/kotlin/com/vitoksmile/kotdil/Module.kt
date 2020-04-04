package com.vitoksmile.kotdil

interface Module {
    /**
     * Register a provider to create a new instance of [T] every time.
     */
    fun <T> factory(name: String, builder: Module.() -> T)

    /**
     * Register a provider to create only one instance of [T].
     */
    fun <T> single(name: String, builder: Module.() -> T)

    /**
     * Provide an instance of [T].
     */
    fun <T> get(name: String): T
}

private class ModuleImpl : Module {
    internal val providers = mutableMapOf<String, Builder<*>>()

    override fun <T> factory(name: String, builder: Module.() -> T) {
        providers[name] = FactoryBuilder { builder() }
    }

    override fun <T> single(name: String, builder: Module.() -> T) {
        providers[name] = SingletonBuilder { builder() }
    }

    override fun <T> get(name: String): T {
        return try {
            @Suppress("UNCHECKED_CAST")
            providers.getValue(name).build() as T
        } catch (error: Throwable) {
            throw UnregisteredProviderException(name, error)
        }
    }
}

/**
 * Register a provider with default name to create a new instance of [T] every time.
 */
inline fun <reified T> Module.factory(noinline builder: Module.() -> T) {
    factory(T::class.java.name, builder)
}

/**
 * Register a provider with default name to create only one instance of [T].
 */
inline fun <reified T> Module.single(noinline builder: Module.() -> T) {
    single(T::class.java.name, builder)
}

/**
 * Provide an instance of [T] with default name.
 */
inline fun <reified T> Module.get(): T {
    return get(T::class.java.name)
}

/**
 * Check if the module has a provider for the [name].
 */
internal fun Module.containsProvider(name: String): Boolean =
    (this as ModuleImpl).providers.containsKey(name)

/**
 * Create new module.
 *
 * @see Module
 */
fun module(block: Module.() -> Unit): Module = ModuleImpl().apply(block)
