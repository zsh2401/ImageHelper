package top.zsh2401.imagehelper.core

import android.support.design.widget.Snackbar
import top.zsh2401.imagehelper.App

/**
 * Created by zsh24 on 01/31/2018.
 */
@Throws(NoSuException::class)
fun getSu():Process{
//    val su = Runtime.getRuntime().exec("su")
//    return su
//    SnackBar
//    Snackbar.make()
    throw  NotImplementedError()
}
fun findImage(img:Images):String{
    throw NotImplementedError()
}
fun copyFile(imgPath:String,savePath:String){
    throw NotImplementedError()
}

fun extractImage(img:Images,savePath: String){
    throw NotImplementedError()
}
fun flashImage(srcPath:String,img:Images){
    throw NotImplementedError()
}