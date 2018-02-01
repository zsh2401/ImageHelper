package top.zsh2401.imagehelper.core.su

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader

/**
 * Created by zsh24 on 02/01/2018.
 */
class Su(val process:Process) {
    var writer:DataOutputStream =
            DataOutputStream(process.outputStream)
    var reader:BufferedReader =
            BufferedReader(InputStreamReader(process.inputStream))

    fun setCommandAndExecute(vararg commands:String){
        for(cmd in commands){
            writer.writeBytes(cmd + "\n")
        }
        writer.writeBytes("exit\n")
        writer.flush()
    }
    fun getOutput():String{
        var sb = StringBuilder()
        var line:String? = null
        while(true){
            line = reader.readLine()
            if(line.isNullOrEmpty())break
            sb.append(line + "\n")
        }
        return sb.toString()
    }
    fun waitFor():Int{
        return process.waitFor()
    }
    fun stop(){
        process.destroy()
    }
}