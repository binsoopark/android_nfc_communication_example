package com.soobinpark.example.nfccommunication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onLaunchNfcReaderClicked(view: View) {
        val intent = Intent(this, NfcActivity::class.java)
        startActivity(intent)
    }
}