package com.vitoksmile.kotdil

val builderContext: BuilderContext = BuilderContext()

fun kotDil(logging: Boolean = false, builder: BuilderContext.() -> Unit) {
    builderContext.isLogging = logging
    builderContext.builder()
}

inline fun <reified T : Any> inject(name: String? = null) = lazy {
    builderContext.get<T>(name, T::class.java.name)
}