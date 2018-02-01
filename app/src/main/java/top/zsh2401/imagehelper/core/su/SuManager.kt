package top.zsh2401.imagehelper.core.su

import java.io.*



/**
 * Created by zsh24 on 02/01/2018.
 */
object SuManager {
    val TAG = "SuManager"
    fun deviceIsRoot():Boolean{
        try{
            return File("/system/bin/su").exists() ||
                    File("/system/xbin/su").exists()
        }catch (e:Exception){
            e.printStackTrace()
        }
        return false
    }
    fun haveRootPermission():Boolean{
        try{
            if(!deviceIsRoot())return false
            Runtime.getRuntime().exec("su").destroy()
            return true
        }catch (ex:Exception){
            ex.printStackTrace()
            return false
        }
    }
    @Throws(NoSuException::class)
    fun getSu():Su{
        suCheck()
        return Su(Runtime.getRuntime().exec("su"))
    }
    @Throws(NoSuException::class)
    fun execSuCommand(cmd:String): ExecResult {
        suCheck()
        val su = Runtime.getRuntime().exec("su")
        val writer = DataOutputStream(su.outputStream)
        val reader = BufferedReader(InputStreamReader(su.inputStream))
        //exe command and wait to exit...
        writer.writeBytes(cmd + "\n")
        writer.writeBytes("exit\n")
        writer.flush()
        su.waitFor()
//        reader.readText()
        return ExecResult(reader.readText(), su.exitValue())
    }
    fun suCheck(){
        if(!haveRootPermission()){
            throw NoSuException()
        }
    }
}