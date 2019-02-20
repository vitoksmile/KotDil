package com.vitoksmile.kotdil.example

import com.vitoksmile.kotdil.get
import com.vitoksmile.kotdil.module
import kotlin.random.Random

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