package com.vitoksmile.kotdil.example

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