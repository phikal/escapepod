/*
 * FileHelper.kt
 * Implements the FileHelper class
 * A FileHelper is provides helper methods for reading and writing files from and to  device storage
 *
 * This file is part of
 * ESCAPEPODS - Free and Open Podcast App
 *
 * Copyright (c) 2018 - Y20K.org
 * Licensed under the MIT-License
 * http://opensource.org/licenses/MIT
 */


package org.y20k.escapepods.helpers

import android.content.Context
import android.net.Uri
import java.io.*
import java.text.NumberFormat


/**
 * FileHelper class
 */
class FileHelper {

    /* Define log tag */
    private val TAG : String = LogHelper.makeLogTag(DownloadHelper::class.java.name)


    /* Reads InputStream from file uri and returns it as String*/
    fun readTextFile(context : Context, uri: Uri) : String {
        // todo read https://commonsware.com/blog/2016/03/15/how-consume-content-uri.html
        val stream : InputStream = context.getContentResolver().openInputStream(uri)
        val reader : BufferedReader = BufferedReader(InputStreamReader(stream))
        val builder : StringBuilder = StringBuilder()

        // read until last line reached
        reader.forEachLine {
            builder.append(it)
            builder.append("\n") }
        stream.close()

        return builder.toString()
    }


    /* Create nomedia file in given folder to prevent media scanning */
    fun createNomediaFile(folder : File) {
        val noMediaOutStream : FileOutputStream = FileOutputStream(getNoMediaFile(folder))
        noMediaOutStream.write(0)
    }


    /* Delete nomedia file in given folder */
    fun deleteNoMediaFile(folder: File) {
        getNoMediaFile(folder).delete()
    }


    /* Converts byte value into a human readable format */
    // Source: https://programming.guide/java/formatting-byte-size-to-human-readable-format.html
    fun getReadableByteCount(bytes: Long, si: Boolean): String {

        // check if Decimal prefix symbol (SI) or Binary prefix symbol (IEC) requested
        val unit : Long = if (si) 1000L else 1024L

        // just return bytes if file size is smaller than requested unit
        if (bytes < unit) return bytes.toString() + " B"

        // calculate exp
        val exp : Int = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()

        // determine prefix symbol
        val prefix : String = ((if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i")

        // calculate result and set number format
        val result : Double = bytes / Math.pow(unit.toDouble(), exp.toDouble())
        val numberFormat = NumberFormat.getNumberInstance()
        numberFormat.maximumFractionDigits = 1

        return numberFormat.format(result) + " " + prefix + "B"
    }


    /* Returns a nomedia file object */
    private fun getNoMediaFile(folder : File) : File {
        return File(folder, ".nomedia")
    }

}