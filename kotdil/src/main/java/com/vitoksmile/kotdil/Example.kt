package com.vitoksmile.kotdil

import kotlin.random.Random

fun main() {
    kotDil(logging = true) {
        singleton { Cat() }
        factory { Dog(Random.nextInt()) }
    }

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
}

class Cat
class Dog(val age: Int)