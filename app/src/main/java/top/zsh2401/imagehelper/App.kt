package top.zsh2401.imagehelper

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Environment
import android.util.Log
import top.zsh2401.imagehelper.core.image.ImageExtractor
import top.zsh2401.imagehelper.core.image.ImageFinder
import top.zsh2401.imagehelper.core.image.Images
import top.zsh2401.imagehelper.ux.FileUtil
import android.provider.MediaStore



/**
 * Created by zsh24 on 02/01/2018.
 */
class App : Application() {
    companion object {
        val TAG = "App"

        val current:App get() = _current
        private lateinit var _current:App

        lateinit var gConfig:Configuration
        val context: Context
        get() =_context
        private lateinit var _context:Context
    }
    override fun onCreate() {
        super.onCreate()
        var storage = Environment.getExternalStorageDirectory().absolutePath
//        getRealPathFromURI("content://")
        Log.d(TAG,storage)
        val config = resources.configuration
        _current = this
        gConfig = config
        _context = this.applicationContext
    }

    fun getRealPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

}