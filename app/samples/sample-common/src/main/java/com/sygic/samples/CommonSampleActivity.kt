package com.sygic.samples

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sygic.samples.common.BuildConfig
import com.sygic.samples.common.R
import com.sygic.ui.common.extensions.openUrl

abstract class CommonSampleActivity : AppCompatActivity() {

    abstract val wikiModulePath: String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.source_code) {
            openUrl(BuildConfig.GITHUB_WIKI + wikiModulePath)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}