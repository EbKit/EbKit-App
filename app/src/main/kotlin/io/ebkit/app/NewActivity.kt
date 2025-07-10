package io.ebkit.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class NewActivity : AppCompatActivity(), IEntry by EbEntry {

    override fun onCreate(savedInstanceState: Bundle?) = create {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }

    override fun onDestroy() = destroy {
        super.onDestroy()
    }
}