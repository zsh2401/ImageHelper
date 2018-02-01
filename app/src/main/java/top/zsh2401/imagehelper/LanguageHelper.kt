package top.zsh2401.imagehelper

import java.util.*

/**
 * Created by zsh24 on 02/01/2018.
 */
object LanguageHelper{
    val languages:Array<String> = arrayOf(
            App.context.getString(R.string.l_auto),
            App.context.getString(R.string.l_zh_cn),
            App.context.getString(R.string.l_en_us))

    fun setLanguageBySerial(languageCode:Int){
        when(languageCode){
            1->{App.gConfig.locale = Locale.CHINESE}
            2->{App.gConfig.locale = Locale.ENGLISH
            }
            else->{App.gConfig.locale = Locale.getDefault()}
        }

    }
    fun setAuto(){
        setLanguageBySerial(0)
    }

    fun getCurrentLanguageSerial():Int{
        if(isSetAutoLanguage())return 0
        var crtLaguageName = App.context.getString(R.string.language_name)
        return languages.indexOf(crtLaguageName)
    }
    fun isSetAutoLanguage():Boolean{
        return true
    }
    private fun getSavedChoice():Int{
        return 1
    }
    private fun saveChoice(choice:Int){

    }
    fun initLanguage(){
        setLanguageBySerial(getSavedChoice())
    }
}
