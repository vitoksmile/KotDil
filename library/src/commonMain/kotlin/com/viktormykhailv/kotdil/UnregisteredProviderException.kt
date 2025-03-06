package com.viktormykhailv.kotdil

class UnregisteredProviderException(name: String, cause: Throwable? = null) :
    IllegalStateException("Provider wasn't registered for name='$name'.", cause)
