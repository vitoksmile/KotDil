# KotDil

Depencency injection framework for Kotlin developers.

[![](https://jitpack.io/v/vitoksmile/KotDil.svg)](https://jitpack.io/#vitoksmile/KotDil)

## Add to your project
The first step, add JitPack repository to your root build.gradle file (not module build.gradle file):
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
The second step, add the library to your module build.gradle:
```
dependencies {
	implementation 'com.github.vitoksmile:KotDil:1.0.0'
}
```

## How to use KotDil?
The first, create `BuilderContext` by the next code:
```
kotDil {
    // this will be your providers
}
```

The second, add providers:
```
kotDil {
        singleton { Cat() }
        factory { Dog(Random.nextInt()) }
}
```

The last one, call `inject` for your value:
```
class Cat
class Dog(val age: Int)

val dog1: Dog by inject()
val dog2: Dog by inject()
val dog3: Dog by inject()
println(dog1)
println(dog2)
println(dog3)

val cat1: Cat by inject()
val cat2: Cat by inject()
val cat3: Cat by inject()
println(cat1)
println(cat2)
println(cat3)
```
In console you will see, that `dog1`, `dog2` and `dog3` is not the same instance of class `Dog`, because we used provider `factory`, which create new instances of classes. 
> com.vitoksmile.kotdil.Dog@2f4d3709
> com.vitoksmile.kotdil.Dog@4e50df2e
> com.vitoksmile.kotdil.Dog@1d81eb93

But `cat1`, `cat2` and `cat3` is the same instance of class (injected by `singleton` provider).
> com.vitoksmile.kotdil.Cat@7291c18f
> com.vitoksmile.kotdil.Cat@7291c18f
> com.vitoksmile.kotdil.Cat@7291c18f


### Todos

 - Add named providers;
 - Inject as arguments in providers;
 - Add modules.