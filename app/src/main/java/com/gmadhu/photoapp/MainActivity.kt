package com.gmadhu.photoapp

import ImageAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.gmadhu.photoapp.adaptor.ImageData
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    var arrayList: ArrayList<ImageData> = ArrayList<ImageData>()
    val intArray = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        check()

        imgRec.layoutManager = GridLayoutManager(this, 3)
        val operatorListAdapterTop = ImageAdapter(this, arrayList)
        operatorListAdapterTop.notifyDataSetChanged()
        imgRec.adapter = operatorListAdapterTop
    }


    fun check() {
        val N = 50
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
                        1,
                        "y"
                    )
                )
            } else {
                arrayList.add(
                    ImageData(
                        1,
                        "n"
                    )
                )
            }
        }

    }
}
