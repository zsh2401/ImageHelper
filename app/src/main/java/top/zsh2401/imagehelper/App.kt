package top.zsh2401.imagehelper

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import top.zsh2401.imagehelper.core.image.ImageExtractor
import top.zsh2401.imagehelper.core.image.ImageFinder
import top.zsh2401.imagehelper.core.image.Images

/**
 * Created by zsh24 on 02/01/2018.
 */
class App : Application() {
    companion object {
        val TAG = "App"
        val current:Application get() = _current
        lateinit var _current:Application

        lateinit var gConfig:Configuration
        val context: Context
        get() =_context
        private lateinit var _context:Context
    }
    override fun onCreate() {
        super.onCreate()
        val config = resources.configuration
        _current = this
        gConfig = config
        _context = this.applicationContext
    }
}