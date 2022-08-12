package com.gmadhu.photoapp.imgcapture

import com.gmadhu.photoapp.imgcapture.EasyImage.ImageSource

abstract class DefaultCallback : EasyImage.Callbacks {
    override fun onImagePickerError(e: Exception?, source: ImageSource?, type: Int) {}
    override fun onCanceled(source: ImageSource?, type: Int) {}
}