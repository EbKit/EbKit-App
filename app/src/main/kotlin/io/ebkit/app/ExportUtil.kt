package io.ebkit.app

import android.content.Context
import androidx.compose.runtime.Composable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

@DslMarker
internal annotation class ScopeMarker

@ScopeMarker
internal interface CreateScope {

    fun CreateScope.setContent(content: @Composable () -> Unit)
}

@ScopeMarker
internal interface DestroyScope

internal interface IEntry {

    fun <R> create(block: CreateScope.() -> R): R

    fun <R> destroy(block: DestroyScope.() -> R): R
}

internal object EbEntry : IEntry, KoinComponent {

    /** 核心 */
    private val core: EbKitCore by inject<EbKitCore>()

    /** 上下文 */
    private val context by inject<Context>()

    private val createInstance = object : CreateScope {

        override fun CreateScope.setContent(
            content: @Composable () -> Unit
        ) {

        }
    }

    private val destroyInstance = object : DestroyScope {

    }

    override fun <R> create(block: CreateScope.() -> R): R {
        return block.invoke(createInstance)
    }

    override fun <R> destroy(block: DestroyScope.() -> R): R {
        return block.invoke(destroyInstance)
    }
}