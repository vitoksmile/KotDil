package com.viktormykhailiv.kotdil

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.viktormykhailv.kotdil.inject
import com.viktormykhailv.kotdil.injectValue
import com.viktormykhailv.kotdil.module
import com.viktormykhailv.kotdil.single
import com.viktormykhailv.kotdil.startKotDil
import kotlinx.datetime.Clock
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

private const val AUTO_ID = "AUTO_ID"
private const val RANDOM_FAKE = "RANDOM_FAKE"

@Composable
@Preview
fun App() {
    val idModule = remember {
        module {
            var id = 1
            factory(name = AUTO_ID) {
                id++
            }
        }
    }
    val generatorModule = remember {
        module {
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
    }
    LaunchedEffect(Unit) {
        startKotDil {
            modules(idModule, generatorModule)
        }
    }

    MaterialTheme {
        val generator by remember { inject<RandomGenerator>() }
        var generatedLong by remember { mutableStateOf<Long?>(null) }

        var generatedUser by remember { mutableStateOf<User?>(null) }

        Column(
            modifier = Modifier
                .safeContentPadding()
                .padding(16.dp),
        ) {
            generatedLong?.let {
                Text("Generated long: $it")
            }
            Button(
                onClick = {
                    generatedLong = generator.generateLong()
                }
            ) {
                Text("Generate long")
            }

            generatedUser?.let {
                Text("Generated user: $it")
            }
            Button(
                onClick = {
                    generatedUser = User(injectValue(AUTO_ID), "John")
                }
            ) {
                Text("Generate user")
            }
        }
    }
}
