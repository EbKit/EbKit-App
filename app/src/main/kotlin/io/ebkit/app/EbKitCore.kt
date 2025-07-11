package io.ebkit.app

import android.content.ContextWrapper
import org.koin.core.component.KoinComponent
import org.koin.dsl.module

val coreModule = module {
    single<EbKitCore>(
        createdAtStart = true
    ) {
        EbKitCore()
    }
}

internal class EbKitCore : ContextWrapper(null), KoinComponent {

    fun ok() {

    }
}