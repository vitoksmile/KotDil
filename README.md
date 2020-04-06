# KotDil
The easy dependency injection framework for Kotlin developers.

[![](https://jitpack.io/v/vitoksmile/KotDil.svg)](https://jitpack.io/#vitoksmile/KotDil)
[![](https://jitci.com/gh/vitoksmile/KotDil/svg)](https://jitci.com/gh/vitoksmile/KotDil)

## Setup
Check that you have the `JitPack` repository.
```
// Add JitPack to your repositories if needed
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Pick `KotDil` dependency:
```
dependencies {
    implementation 'com.github.vitoksmile:KotDil:2.0.0'
}
```

## How to use KotDil?
The first, create your own modules to provide dependencies:
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

The second, register all modules:
```kotlin
startKotDil {
    modules(idModule, generatorModule)
}
```

The final step, invoke `inject` or `injectValue` to receive some dependency:
```kotlin
val user = User(injectValue(AUTO_ID), "John")

val generator by inject<RandomGenerator>() // Lazy init
generator.generateLong()
```

Also you can find additional usage examples in the unit-tests [package](https://github.com/vitoksmile/KotDil/tree/master/src/test/kotlin/com/vitoksmile/kotdil)
