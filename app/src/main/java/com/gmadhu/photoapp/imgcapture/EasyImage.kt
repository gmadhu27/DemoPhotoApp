package com.gmadhu.photoapp.imgcapture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.provider.MediaStore
import java.io.File


object EasyImage : EasyImageConfig {
    private const val KEY_TYPE = "pl.aprilapps.easyphotopicker.type"


    private fun createGalleryIntent(context: Context?, type: Int): Intent {
        storeType(context, type)
        return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    private fun storeType(context: Context?, type: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(KEY_TYPE, type)
            .commit()
    }

    private fun restoreType(context: Context): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(KEY_TYPE, 0)
    }



    fun openGallery(activity: Activity, type: Int) {
        val intent = createGalleryIntent(activity, type)
        activity.startActivityForResult(intent, EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY)
    }

    fun handleActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity,
        callbacks: Callbacks
    ) {
        if (requestCode == EasyImageConfig.REQ_SOURCE_CHOOSER || requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY || requestCode == EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS -> {
                        onPictureReturnedFromDocuments(data, activity, callbacks)
                    }
                    EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY -> {
                        onPictureReturnedFromGallery(data, activity, callbacks)
                    }
                    else -> {
                        onPictureReturnedFromDocuments(data, activity, callbacks)
                    }
                }
            } else {
                when (requestCode) {
                    EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS -> {
                        callbacks.onCanceled(ImageSource.DOCUMENTS, restoreType(activity))
                    }
                    EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY -> {
                        callbacks.onCanceled(ImageSource.GALLERY, restoreType(activity))
                    }
                    else -> {
                        callbacks.onCanceled(ImageSource.DOCUMENTS, restoreType(activity))
                    }
                }
            }
        }
    }

    private fun onPictureReturnedFromDocuments(
        data: Intent?,
        activity: Activity,
        callbacks: Callbacks
    ) {
        try {
            val photoPath = data!!.data
            val photoFile = photoPath?.let { EasyImageFiles.pickedExistingPicture(activity, it) }
            callbacks.onImagePicked(photoFile, ImageSource.DOCUMENTS, restoreType(activity))
        } catch (e: Exception) {
            e.printStackTrace()
            callbacks.onImagePickerError(e, ImageSource.DOCUMENTS, restoreType(activity))
        }
    }

    private fun onPictureReturnedFromGallery(
        data: Intent?,
        activity: Activity,
        callbacks: Callbacks
    ) {
        try {
            val photoPath = data!!.data
            val photoFile = photoPath?.let { EasyImageFiles.pickedExistingPicture(activity, it) }
            callbacks.onImagePicked(photoFile, ImageSource.GALLERY, restoreType(activity))
        } catch (e: Exception) {
            e.printStackTrace()
            callbacks.onImagePickerError(e, ImageSource.GALLERY, restoreType(activity))
        }
    }

    enum class ImageSource {
        GALLERY, DOCUMENTS
    }

    interface Callbacks {
        fun onImagePickerError(e: Exception?, source: ImageSource?, type: Int)
        fun onImagePicked(imageFile: File?, source: ImageSource?, type: Int)
        fun onCanceled(source: ImageSource?, type: Int)
    }
}