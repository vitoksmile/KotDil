# KotDil

The easy dependency injection framework for Kotlin developers.

[![Maven Central](https://img.shields.io/maven-central/v/com.viktormykhailiv/kotdil)](https://central.sonatype.com/search?namespace=com.viktormykhailiv&name=kotdil)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

## Quick Start

Add the dependency to your project:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.viktormykhailiv:kotdil:$kotdil_version")
}
```

## How to use KotDil?

1. Create your own modules to provide dependencies:

```kotlin
val idModule = module {
    val id = AtomicInteger(1)
    factory(name = AUTO_ID) {
        id.getAndIncrement()
    }
}
val generatorModule = module {
    single<RandomGenerator> {
        object : RandomGenerator {
            override fun generateLong() = Random.nextLong()
        }
    }
    single<RandomGenerator>(name = RANDOM_FAKE) {
        object : RandomGenerator {
            override fun generateLong() = System.currentTimeMillis()
        }
    }
}
```

2. Register all modules:

```kotlin
startKotDil {
    modules(idModule, generatorModule)
}
```

3. Call `inject` or `injectValue` to get required dependency:

```kotlin
val user = User(injectValue(AUTO_ID), "John")

val generator by inject<RandomGenerator>() // Lazy init
generator.generateLong()
```

Also you can find additional usage examples in the
unit-tests [package](https://github.com/vitoksmile/KotDil/tree/main/library/src/commonTest/kotlin/com/viktormykhailiv/kotlin/KotDilTest.kt)
