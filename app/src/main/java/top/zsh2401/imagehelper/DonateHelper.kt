package top.zsh2401.imagehelper

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.widget.Toast

/**
 * Created by zsh24 on 02/01/2018.
 */
private val alipayClientPkgName = "com.eg.android.AlipayGphone"
private val wechatAccount = "Ryme2401"
private val alipayAccount = "zsh2401@163.com"
private val alipayRedpacketCode = ""
private val URL_FORMAT= "intent://platformapi/startapp?saId=10000007&" +
        "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2FFKX06104YFIEAHNXXKP7E2%3F_s" +
        "%3Dweb-other&_t=1472443966571#Intent;" +
        "scheme=alipayqr;package=com.eg.android.AlipayGphone;end"
private val clipboardManager:ClipboardManager =
        App.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

fun gotoAlipay():Boolean{
    if(isInstallAlipayClient()){
        App.context.startActivity(Intent.parseUri(URL_FORMAT,Intent.URI_INTENT_SCHEME))
        return true
    }
    return false
}
fun copyWechatAccount(){
    clipboardManager.text = wechatAccount
}
fun copyAlipayAccount(){
    clipboardManager.text = alipayAccount
}
fun copyAlipayRedpacketCode(){
    clipboardManager.text =  alipayRedpacketCode
}

private fun isInstallAlipayClient() :Boolean{
    try{
        return App.context.packageManager.getPackageInfo(alipayClientPkgName,0) != null
    }catch (ex: PackageManager.NameNotFoundException){
        return false
    }
}