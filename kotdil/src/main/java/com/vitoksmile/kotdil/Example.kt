package com.vitoksmile.kotdil

import kotlin.random.Random

fun main() {
    kotDil(logging = true) {
        // boys providers
        factory {
            Boy(get(), get())
        }
        factory(name = "neighbor") {
            Boy(get(), get(name = "neighbor"))
        }

        // cat provider
        singleton {
            Cat()
        }

        // dogs providers
        factory {
            Dog(Random.nextInt())
        }
        factory(name = "neighbor") {
            Dog(1)
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

class Cat {

    override fun toString(): String {
        val toString = javaClass.name + "@" + Integer.toHexString(hashCode())
        return "Cat($toString)"
    }
}

class Dog(
    private val age: Int
) {

    override fun toString(): String {
        val toString = javaClass.name + "@" + Integer.toHexString(hashCode())
        return "Dog($toString, age=$age)"
    }
}

class Boy(
    private val cat: Cat,
    private val dog: Dog
) {

    override fun toString(): String {
        val toString = javaClass.name + "@" + Integer.toHexString(hashCode())
        return "Boy($toString, cat=$cat, dog=$dog)"
    }
}