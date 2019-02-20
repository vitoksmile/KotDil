package com.vitoksmile.kotdil

import com.vitoksmile.kotdil.Type.Factory
import com.vitoksmile.kotdil.Type.Singleton

@Suppress("UNCHECKED_CAST")
class BuilderContext {

    private companion object {

        private const val DELIMITER_NAME = "["
    }

    /**
     * Hold builder context for module
     */
    class Module(internal val builderContext: BuilderContext)

    /**
     * Hold providers
     */
    private val providers = mutableMapOf<String, Provider>()

    /**
     * Flag is enabled print logs to console
     */
    var isLogging: Boolean = false

    /**
     * Add modules
     */
    fun modules(vararg modules: Module) {
        modules.forEach { module ->
            providers.putAll(module.builderContext.providers)
        }
    }

    /**
     * Add provider to create instance of T every time in getting
     *
     * @param name Name of provider
     * @param provider T-provider
     */
    fun <T : Any> factory(name: String? = null, provider: () -> T) {
        provide(name, provider, isSingleton = false)
    }

    /**
     * Add provider to create one instance of T
     *
     * @param name Name of provider
     * @param provider T-provider
     */
    fun <T : Any> singleton(name: String? = null, provider: () -> T) {
        provide(name, provider, isSingleton = true)
    }

    /**
     * Add provider to holder
     *
     * @param name Name of provider
     * @param provider T-provider
     * @param isSingleton Is need to provide singleton instance
     */
    private fun <T : Any> provide(name: String? = null, provider: () -> T, isSingleton: Boolean) {
        providers[getKey(name, provider)] = Provider(
            provider = provider,
            type = if (isSingleton) Singleton else Factory
        )
    }

    /**
     * @return instance of T by [key]
     */
    operator fun <T : Any> get(name: String? = null, key: String): T {
        @Suppress("NAME_SHADOWING")
        val key = getKey(name, key)
        val provider = providers[key]
            ?: throw IllegalArgumentException("Value with key '$key' not found. Did you added provider?")

        return if (provider.isSingleton) {
            provider.provide()
        } else {
            measureTime(key) {
                provider.provide<T>()
            }
        }
    }

    /**
     * Generate key to store provider in map
     *
     * @return key for provider
     */
    private fun <T : Any> getKey(name: String? = null, provider: () -> T): String {
        return getKey(name, provider.toString().substringAfter("<").substringBefore(">"))
    }

    /**
     * Generate key based on [name] and base [key]
     */
    private fun getKey(name: String? = null, key: String): String {
        return key + (name?.let { "$DELIMITER_NAME$it" } ?: "")
    }

    /**
     * Measure time of creating instances of T
     */
    private fun <T : Any> measureTime(key: String, block: () -> T): T {
        if (!isLogging) return block()

        val start = System.currentTimeMillis()
        val value = block()

        // logging
        StringBuilder().apply {
            append("Instance for '${getKeyWithoutName(key)}' ")
            getNameWithoutKey(key)?.let { append("($it) ") }
            append("was created after ${System.currentTimeMillis() - start} ms")
            println(this)
        }

        return value
    }

    /**
     * Key transformer for logging
     */
    private fun getKeyWithoutName(key: String) = key.substringBefore(DELIMITER_NAME)

    /**
     * Key transformer for logging
     */
    private fun getNameWithoutKey(key: String): String? = if (key.indexOf(DELIMITER_NAME) >= 0)
        key.substringAfter(DELIMITER_NAME) else null
}