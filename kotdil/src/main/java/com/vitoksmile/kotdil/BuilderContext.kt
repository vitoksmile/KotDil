package com.vitoksmile.kotdil

class BuilderContext {

    private companion object {

        private const val DELIMITER_NAME = "["
    }

    /**
     * Store lazy providers to create one instance
     */
    private val singletonsLazy = mutableMapOf<String, Any>()

    /**
     * Store created instances
     */
    private val singletons = mutableMapOf<String, Any>()

    /**
     * Store providers to create instances every time
     */
    private val factories = mutableMapOf<String, Any>()

    /**
     * Flag is enabled print logs to console
     */
    var isLogging: Boolean = false

    /**
     * Add provider to create one instance of T
     *
     * @param name Name of provider
     * @param provider T-provider
     */
    fun <T : Any> singleton(name: String? = null, provider: () -> T) {
        singletonsLazy[getKey(name, provider)] = provider
    }

    /**
     * Add provider to create instance of T every time in getting
     *
     * @param name Name of provider
     * @param provider T-provider
     */
    fun <T : Any> factory(name: String? = null, provider: () -> T) {
        factories[getKey(name, provider)] = provider
    }

    /**
     * @return instance of T by [key]
     */
    operator fun <T : Any> get(name: String? = null, key: String): T {
        @Suppress("NAME_SHADOWING")
        val key = getKey(name, key)

        return when {
            // Create instance and store it to another map
            singletonsLazy.containsKey(key) -> {
                measureTime(key) {
                    singletons[key] = (singletonsLazy[key] as Function0<T>)()
                }
                singletonsLazy.remove(key)
                get(name, key)
            }

            // Return created early instance
            singletons.containsKey(key) -> singletons[key] as T

            // Invoke every time provider to create instance
            factories.containsKey(key) -> measureTime(key) {
                (factories[key] as Function0<T>)()
            }

            else -> throw IllegalArgumentException("Value with key '$key' not found")
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
        val name = getNameWithoutKey(key)?.let { "($it)" } ?: ""
        println("Instance for '${getKeyWithoutName(key)}' $name was created after ${System.currentTimeMillis() - start} ms")
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