package com.vitoksmile.kotdil

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

private const val AUTO_ID = "ID"
private const val RANDOM_FAKE = "RANDOM_FAKE"

internal class KotDilTest {
    @BeforeEach
    internal fun setUp() {
        resetKotDil()
    }

    @Test
    fun `factory builder`() {
        val idModule = module {
            val id = AtomicInteger(1)
            factory(name = AUTO_ID) {
                id.getAndIncrement()
            }
        }
        startKotDil {
            modules(idModule)
        }

        val userId1 = User(injectValue(AUTO_ID), "John")
        val userId2 = User(injectValue(AUTO_ID), "Mark")

        assertEquals(1, userId1.id)
        assertEquals(2, userId2.id)
    }

    @Test
    fun `single builder`() {
        val managerModule = module {
            single<RandomGenerator> {
                object : RandomGenerator {
                    override fun generateLong() = Random.nextLong()
                }
            }
            single<RandomGenerator>(name = RANDOM_FAKE) {
                object : RandomGenerator {
                    override fun generateLong() = System.currentTimeMillis()
                }
            }
        }
        startKotDil {
            modules(managerModule)
        }

        val generator by inject<RandomGenerator>()
        val generatorFake by inject<RandomGenerator>(name = RANDOM_FAKE)

        assertSame(generator, injectValue<RandomGenerator>())
        assertSame(injectValue<RandomGenerator>(), injectValue<RandomGenerator>())
        assertNotSame(generator, generatorFake)
    }

    @Test
    fun `unregistered provider`() {
        try {
            val generator by inject<RandomGenerator>()
            generator.generateLong()
            fail<Unit>("Injected an instance for unregistered provider.")
        } catch (error: UnregisteredProviderException) {
        }
    }
}

private data class User(
    val id: Int,
    val name: String
)

private interface RandomGenerator {
    fun generateLong(): Long
}
