package com.vitoksmile.kotdil.example

import com.vitoksmile.kotdil.KotDil
import com.vitoksmile.kotdil.inject

fun main() {
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

    val dog1: Dog by inject()
    val dog2: Dog by inject("neighbor")
    val dog3: Dog by inject("neighbor")
    val dog4: Dog by inject()
    println(dog1)
    println(dog2)
    println(dog3)
    println(dog4)
    println()

    val cat1: Cat by inject()
    val cat2: Cat by inject()
    val cat3: Cat by inject()
    println(cat1)
    println(cat2)
    println(cat3)
    println()

    val boy1: Boy by inject()
    val boy2: Boy by inject("neighbor")
    val boy3: Boy by inject("neighbor")
    println(boy1)
    println(boy2)
    println(boy3)
}