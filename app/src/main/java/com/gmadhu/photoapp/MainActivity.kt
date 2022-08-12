package com.gmadhu.photoapp

import ImageAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gmadhu.photoapp.adaptor.ImageData
import com.gmadhu.photoapp.imgcapture.DefaultCallback
import com.gmadhu.photoapp.imgcapture.EasyImage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    //request identifiers
    private val imgFirst = 123
    private val imgSecond = 321
    //files
    var imgFirstFile: File? = null
    var imgSecondFile: File? = null

    var arrayList: ArrayList<ImageData> = ArrayList<ImageData>()
    val intArray = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnUploadDocument.setOnClickListener {
            if(etIdNumber.editText?.text.toString().isEmpty()){
                Toast.makeText(this,"please add count to apply",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            removeItems()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            check(etIdNumber.editText?.text.toString())
            imgRec.layoutManager = GridLayoutManager(this, 3)
            val operatorListAdapterTop = ImageAdapter(this, arrayList)
            operatorListAdapterTop.notifyDataSetChanged()
            imgRec.adapter = operatorListAdapterTop
        }
        btnChooseImage.setOnClickListener { openImageSelection(imgFirst) }
        btnChooseSecond.setOnClickListener { openImageSelection(imgSecond) }
    }

    private fun openImageSelection(imgData: Int) {
        EasyImage.openGallery(this, imgData)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {

                override fun onImagePicked(
                    imageFile: File?,
                    source: EasyImage.ImageSource?,
                    type: Int
                ) {
                    Log.e("activityresult", "yes")
                    if (imageFile != null) {

                        when (type) {
                            imgFirst -> {
                                imgFirstFile = imageFile
                                Glide.with(this@MainActivity).load(Uri.fromFile(imgFirstFile))
                                    .into(imgFirstPic)
                            }
                            imgSecond -> {
                                imgSecondFile = imageFile

                                Glide.with(this@MainActivity).load(Uri.fromFile(imgSecondFile))
                                    .into(imgSecondPic)
                            }
                        }

                    } else {
                        Log.e("onactivityresult", "yes")

                    }
                }

                override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                    super.onCanceled(source, type)
                    // Cancel handling, you might wanna remove taken photo if it was canceled

                }
            })

    }
    fun removeItems() {
        arrayList.clear()
        intArray.clear()
    }
    private fun check(value: String) {
        val N = value.toInt()
        var sum = 0
        val A = (sqrt(2 * N + 0.25) - 0.5).toInt()
        for (i in 0 until A + 1) {
            sum += i
            if (sum != 0) {
                intArray.add(sum)
            }
        }
        for (i in 0 until N) {
            var isContain = false
            for (n in intArray) {
                if (n - 1 == i) {
                    isContain = true
                }
            }
            if (isContain) {
                arrayList.add(
                    ImageData(
                        imgFirstFile,
                        true
                    )
                )
            } else {
                arrayList.add(
                    ImageData(
                        imgSecondFile,
                        false
                    )
                )
            }
        }

    }
}
