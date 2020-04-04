package com.vitoksmile.kotdil

class UnregisteredProviderException(name: String, cause: Throwable? = null) :
    IllegalStateException("Provider wasn't registered for name='$name'.", cause)
