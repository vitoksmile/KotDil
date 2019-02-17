package com.vitoksmile.kotdil

val builderContext: BuilderContext = BuilderContext()

fun kotDil(logging: Boolean = false, builder: BuilderContext.() -> Unit) {
    builderContext.isLogging = logging
    builderContext.builder()
}

inline fun <reified T : Any> inject() = lazy {
    builderContext.get<T>(T::class.java.name)
}