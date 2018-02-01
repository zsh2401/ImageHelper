package top.zsh2401.imagehelper.core.image

import android.util.Log
import top.zsh2401.imagehelper.core.su.SuManager

/**
 * Created by zsh24 on 02/01/2018.
 */

object ImageFinder {
    val TAG = "ImageFinder"
    @Throws(ImageNotFoundException::class)
    fun pathOf(img: Images):String{
        return methodOne(img) ?:
                methodTwo(img) ?:
                throw ImageNotFoundException()
    }
    private fun methodOne(img: Images):String?{
        if(!CommandHelper.haveFindCommand())return null
        var findResult =
                SuManager.execSuCommand(
                        "find /dev/ -name ${img.toString().toLowerCase()}")
        Log.d(TAG,"output: "+ findResult.output)
        if(findResult.exitValue != 0)return null
        var lines = findResult.output.split("\n")
        Log.d(TAG,"splited...")
        var line:String? = null
        for(i in lines.indices){
            Log.d(TAG,"finding...crt: " + lines[i])
            if(pathIsRight(lines[i])){
                line = lines[i]
                break
            }
        }
        Log.d(TAG,"method1 result: " + (line?:"null"))
        return line
    }
    private fun methodTwo(img: Images):String?{
        var maybePath1 =
                "/dev/block/platform/*/by-name/${img.toString().toLowerCase()}"
        var maybePath2 =
                "/dev/block/platform/soc*/*/by-name/${img.toString().toLowerCase()}"
        var result:String?= null
        if(pathIsRight(maybePath1)){
            result =   maybePath1
        }else if(pathIsRight(maybePath2)){
            result =  maybePath2
        }
        Log.d(TAG,"method2 result: " + (result?:"null"))
        return result
    }
    private fun pathIsRight(path:String):Boolean{
        if(path.isNullOrEmpty())return false
        return SuManager.execSuCommand("ls -l " + path).exitValue == 0
    }
}