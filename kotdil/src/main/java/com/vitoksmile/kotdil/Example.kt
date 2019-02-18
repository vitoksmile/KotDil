package com.vitoksmile.kotdil

import kotlin.random.Random

fun main() {
    kotDil(logging = true) {
        singleton { Cat() }
        factory { Dog(Random.nextInt()) }
        factory("neighbor") { Dog(1) }
    }

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
}

class Cat {

    override fun toString(): String {
        val toString = javaClass.name + "@" + Integer.toHexString(hashCode())
        return "Cat($toString)"
    }
}

class Dog(val age: Int) {

    override fun toString(): String {
        val toString = javaClass.name + "@" + Integer.toHexString(hashCode())
        return "Dog($toString, age=$age)"
    }
}