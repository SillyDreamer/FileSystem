package com.example.filesystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.root_layout, PreferencesFragment())
            .commit()
    }
}
