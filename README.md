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
	implementation 'com.github.vitoksmile:KotDil:1.0.4'
}
```

## How to use KotDil?
The first, create `BuilderContext` by the next code:
```
KotDil {
        // this will be your providers
}
```

The second, add providers (you also cat create `modules`):
```
val boysModule = module {
    // boys providers
    factory {
        Boy(get(), get())
    }
    factory(name = "neighbor") {
        Boy(get(), get(name = "neighbor"))
    }
}

val dogsModule = module {
    // dogs providers
    factory {
        Dog(Random.nextInt())
    }
    factory(name = "neighbor") {
        Dog(1)
    }
}

KotDil(logging = true) {
        modules(
            boysModule,
            dogsModule
        )

        // cat provider
        singleton {
            Cat()
        }
    }
```

The last one, call `inject` for your value:
```
class Cat
class Dog(val age: Int)
class Boy(val cat: Cat, val dog: Dog)

val dog1: Dog by inject()
val dog2: Dog by inject("neighbor")
val dog3: Dog by inject("neighbor")
val dog4: Dog by inject()
println(dog1)
println(dog2)
println(dog3)
println(dog4)

val cat1: Cat by inject()
val cat2: Cat by inject()
val cat3: Cat by inject()
println(cat1)
println(cat2)
println(cat3)

val boy1: Boy by inject()
val boy2: Boy by inject("neighbor")
val boy3: Boy by inject("neighbor")
println(boy1)
println(boy2)
println(boy3)
```
In console you will see, that `dog1`, `dog2`, `dog3` and `dog4` is not the same instance of class `Dog`, because we used provider `factory`, which create new instances of classes.  But `dog2` and `dog3` was created with the same parameters, because we added `name` to `provider`.
> Dog(com.vitoksmile.kotdil.Dog@**2f4d3709**, age=**1084410569**)
>
> Dog(com.vitoksmile.kotdil.Dog@**4e50df2e**, age=**1**)
>
> Dog(com.vitoksmile.kotdil.Dog@**1d81eb93**, age=**1**)
>
> Dog(com.vitoksmile.kotdil.Dog@**7291c18f**, age=**615375305**)

But `cat1`, `cat2` and `cat3` is the same instance of class (injected by `singleton` provider).
> Cat(com.vitoksmile.kotdil.Cat@**34a245ab**)
>
> Cat(com.vitoksmile.kotdil.Cat@**34a245ab**)
>
> Cat(com.vitoksmile.kotdil.Cat@**34a245ab**)

We injected parameters to constuctor `Boy` by method `get()`, which is the same as `inject()`, but without lazy injection. 
In console you will see, that KotDil injected the same singleton value of class `Cat` to constructor of class `Boy`. Also `age` in `Dog` is the same for `boy2` and `boy3`, because we also used named provider.
> Boy(com.vitoksmile.kotdil.Boy@7cc355be, cat=Cat(com.vitoksmile.kotdil.Cat@**34a245ab**), dog=Dog(com.vitoksmile.kotdil.Dog@6e8cf4c6, age=320059173))
>
> Boy(com.vitoksmile.kotdil.Boy@12edcd21, cat=Cat(com.vitoksmile.kotdil.Cat@**34a245ab**), dog=Dog(com.vitoksmile.kotdil.Dog@34c45dca, age=**1**))
>
> Boy(com.vitoksmile.kotdil.Boy@52cc8049, cat=Cat(com.vitoksmile.kotdil.Cat@**34a245ab**), dog=Dog(com.vitoksmile.kotdil.Dog@5b6f7412, age=**1**))

### Todos

 - ~~Add named providers;~~ (done in v.1.0.1);
 - ~~Inject as arguments in providers;~~ (done in v.1.0.3);
 - ~~Add modules.~~ (done in v.1.0.4).