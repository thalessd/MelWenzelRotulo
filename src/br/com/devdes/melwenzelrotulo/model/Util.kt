package br.com.devdes.melwenzelrotulo.model

import java.security.MessageDigest

class Util {

    // PEGA PATH DO PACOTE JAVA
    fun pathPackage(dotDiv : Boolean = false) : String {
        val packageName : String = javaClass.`package`.name
        val pathMainView : String
        val pathSplited : List<String>
        val lenghtPath : Int


        pathSplited = packageName.split(".")

        lenghtPath = pathSplited.size

        pathMainView = pathSplited.joinToString(
                separator = if (dotDiv) "." else "/",
                prefix = if (dotDiv) "" else "/",
                limit = lenghtPath - 1,
                truncated = ""
        ).replace(
                if(dotDiv) Regex("[.]$")
                else Regex("/$"), ""
        )

        return pathMainView
    }

    // STRING PARA HASH MD5
    fun md5(string: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digested = md.digest(string.toByteArray())
        return digested.joinToString("") {
            String.format("%02x", it)
        }
    }

}