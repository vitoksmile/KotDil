package com.vitoksmile.kotdil

/**
 * Main KotDil context
 */
private val builderContext: BuilderContext = BuilderContext()

/**
 * Launch KotDil
 */
@Suppress("FunctionName")
fun KotDil(logging: Boolean = false, builder: BuilderContext.() -> Unit) {
    builderContext.isLogging = logging
    builderContext.builder()
}

/**
 * Create new KotDil module
 */
fun module(builder: BuilderContext.() -> Unit): BuilderContext.Module {
    // Create new builder context
    val builderContext = BuilderContext()
    builderContext.builder()
    return BuilderContext.Module(builderContext)
}

/**
 * Lazy inject instance of T
 */
inline fun <reified T : Any> inject(name: String? = null) =
    lazy { get<T>(name, T::class.java.name) }

/**
 * Get instance of T
 */
inline fun <reified T : Any> get(name: String? = null) =
    get<T>(name, T::class.java.name)

/**
 * Get instance of T
 */
fun <T : Any> get(name: String? = null, className: String): T =
    builderContext[name, className]