package com.homecare.homecare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //opening HomeCarehome activity after 2 sec
        Thread.sleep(2000)
        val intent = Intent(this,HomeCarehome::class.java)
        startActivity(intent)
        finish()
    }
}