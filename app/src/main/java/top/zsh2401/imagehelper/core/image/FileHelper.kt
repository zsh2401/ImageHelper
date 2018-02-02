package top.zsh2401.imagehelper.core.image

import android.net.Uri
import top.zsh2401.imagehelper.App
import java.io.File
import java.io.FileOutputStream

/**
 * Created by zsh24 on 02/02/2018.
 */
fun getTempFile(uri: Uri):String{
    val tempPath = App.current.externalCacheDir.absolutePath + "/temp_file"
    val reader = App.current.contentResolver.openInputStream(uri)
    val writer = FileOutputStream(tempPath)
    val buffer = ByteArray(1024)
    var readSize = 0
    while(true){
        readSize = reader.read(buffer)
        if(readSize==-1){
            break
        }
        writer.write(buffer,0,readSize)
    }
    writer.flush()
    writer.close()
    reader.close()
    return tempPath
}