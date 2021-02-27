package com.mindera.flickergallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.mindera.flickergallery.ui.FragmentGallery

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        val galleryFragment = supportFragmentManager.findFragmentById(R.id.content_frame)
        if (galleryFragment == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.content_frame,FragmentGallery())
            transaction.commit()
        }

    }

    private fun setupToolbar() {
        //val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = getText(R.string.app_name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
