package com.vitoksmile.kotdil

class BuilderContext {

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
     */
    fun <T : Any> singleton(provider: () -> T) {
        singletonsLazy[getKey(provider)] = provider
    }

    /**
     * Add provider to create instance of T every time in getting
     */
    fun <T : Any> factory(provider: () -> T) {
        factories[getKey(provider)] = provider
    }

    /**
     * @return instance of T by [key]
     */
    operator fun <T : Any> get(key: String): T {
        return when {
            // Create instance and store it to another map
            singletonsLazy.containsKey(key) -> {
                measureTime(key) {
                    singletons[key] = (singletonsLazy[key] as Function0<T>)()
                }
                singletonsLazy.remove(key)
                get(key)
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
    private fun <T : Any> getKey(provider: () -> T): String {
        return provider.toString().substringAfter("<").substringBefore(">")
    }

    /**
     * Measure time of creating instances of T
     */
    private fun <T : Any> measureTime(key: String, block: () -> T): T {
        if (!isLogging) return block()

        val start = System.currentTimeMillis()
        val value = block()
        println("Instance for '$key' was created after ${System.currentTimeMillis() - start} ms")
        return value
    }
}