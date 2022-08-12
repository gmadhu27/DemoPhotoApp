package com.gmadhu.photoapp.imgcapture

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.preference.PreferenceManager
import android.webkit.MimeTypeMap
import java.io.*
import java.util.*


internal object EasyImageFiles {
    var DEFAULT_FOLDER_NAME = "EasyImage"
    var TEMP_FOLDER_NAME = "Temp"

    private fun getFolderName(context: Context?): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(BundleKeys.FOLDER_NAME, DEFAULT_FOLDER_NAME)
    }

    private fun tempImageDirectory(context: Context): File {
        val publicTemp = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(BundleKeys.PUBLIC_TEMP, false)
        val dir = if (publicTemp) publicTempDir(context) else privateTempDir(context)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun publicAppExternalDir(context: Context): File? {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }

    private fun publicTempDir(context: Context): File {
        val cameraPicturesDir = File(getFolderLocation(context), getFolderName(context))
        val publicTempDir = File(cameraPicturesDir, TEMP_FOLDER_NAME)
        if (!publicTempDir.exists()) publicTempDir.mkdirs()
        return publicTempDir
    }

    private fun privateTempDir(context: Context): File {
        val privateTempDir = File(context.applicationContext.cacheDir, getFolderName(context))
        if (!privateTempDir.exists()) privateTempDir.mkdirs()
        return privateTempDir
    }


    private fun writeToFile(`in`: InputStream?, file: File?) {
        try {
            val out: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (`in`!!.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            out.close()
            `in`.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun pickedExistingPicture(context: Context, photoUri: Uri): File {
        val pictureInputStream = context.contentResolver.openInputStream(photoUri)
        val directory = tempImageDirectory(context)
        val photoFile =
            File(directory, UUID.randomUUID().toString() + "." + getMimeType(context, photoUri))
        photoFile.createNewFile()
        writeToFile(pictureInputStream, photoFile)
        return photoFile
    }

    /**
     * Default folder location will be inside app public directory. That way write permissions after SDK 18 aren't required and contents are deleted if app is uninstalled.
     *
     * @param context context
     */
    private fun getFolderLocation(context: Context): String? {
        val publicAppExternalDir = publicAppExternalDir(context)
        var defaultFolderLocation: String? = null
        if (publicAppExternalDir != null) {
            defaultFolderLocation = publicAppExternalDir.path
        }
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(BundleKeys.FOLDER_LOCATION, defaultFolderLocation)
    }

    private fun getMimeType(context: Context, uri: Uri): String? {

        //Check uri format to avoid null
        val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //If scheme is a content
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            MimeTypeMap.getFileExtensionFromUrl(
                Uri.fromFile(File(uri.path)).toString()
            )
        }
        return extension
    }
}