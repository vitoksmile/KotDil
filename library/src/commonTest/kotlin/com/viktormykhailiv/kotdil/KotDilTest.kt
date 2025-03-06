package com.viktormykhailiv.kotdil

import com.viktormykhailv.kotdil.UnregisteredProviderException
import com.viktormykhailv.kotdil.inject
import com.viktormykhailv.kotdil.injectValue
import com.viktormykhailv.kotdil.module
import com.viktormykhailv.kotdil.resetKotDil
import com.viktormykhailv.kotdil.single
import com.viktormykhailv.kotdil.startKotDil
import kotlinx.datetime.Clock
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.fail

private const val AUTO_ID = "ID"
private const val RANDOM_FAKE = "RANDOM_FAKE"

internal class KotDilTest {

    @BeforeTest
    internal fun setUp() {
        resetKotDil()
    }

    @Test
    fun `factory builder`() {
        val idModule = module {
            var id = 1
            factory(name = AUTO_ID) {
                id++
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
                    override fun generateLong(): Long = Random.nextLong()
                }
            }
            single<RandomGenerator>(name = RANDOM_FAKE) {
                object : RandomGenerator {
                    override fun generateLong(): Long = Clock.System.now().toEpochMilliseconds()
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
            fail("Injected an instance for unregistered provider.")
        } catch (_: UnregisteredProviderException) {
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
