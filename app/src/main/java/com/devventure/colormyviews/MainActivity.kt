package com.devventure.colormyviews

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.devventure.colormyviews.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private var pincelColor = R.color.grey
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("colors", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val boxes = arrayOf(
            binding.boxOne, binding.boxTwo, binding.boxThree,
            binding.boxFour, binding.boxFive
        )

        for (box in boxes) {
            findViewById<View>(box.id).setBackgroundResource(getColorBox(box.id.toString()))
        }

    }

    override fun onStop() {
        super.onStop()
        editor.apply()
    }

    fun setColor(view: View) {
        when (view.id) {
            R.id.btn_red -> pincelColor = R.color.red
            R.id.btn_yellow -> pincelColor = R.color.yellow
            R.id.btn_green -> pincelColor = R.color.green
        }
    }

    fun colorBox(view: View) {
        when (view.id) {
            R.id.boxOne -> {
                binding.boxOne.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxOne.id.toString(), pincelColor)
            }

            R.id.boxTwo -> {
                binding.boxTwo.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxTwo.id.toString(), pincelColor)
            }

            R.id.boxThree -> {
                binding.boxThree.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxThree.id.toString(), pincelColor)
            }

            R.id.boxFour -> {
                binding.boxFour.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxFour.id.toString(), pincelColor)
            }

            R.id.boxFive -> {
                binding.boxFive.setBackgroundResource(pincelColor)
                editor.putInt(binding.boxFive.id.toString(), pincelColor)
            }
        }
    }

    private fun getColorBox(idBox: String): Int {
        return sharedPreferences.getInt(idBox, R.color.grey)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun shareScreenScreenshot() {
        var path = ""
        if (checkAppPermissions()) {
            val rootView = window.decorView.findViewById<View>(android.R.id.content)
            val bitmap = takeScreenshot(rootView)
            if (bitmap != null) {
                path = storeScreenshot(bitmap)
            }
            shareScreenshot(path)
        } else {
            Toast.makeText(this, R.string.ask_permissions, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAppPermissions(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun takeScreenshot(rootView: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = rootView.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        rootView.draw(canvas)
        return bitmap
    }

    private fun storeScreenshot(bitmap: Bitmap): String {
        return MediaStore.Images.Media.insertImage(contentResolver, bitmap, imgName(), null)
    }

    private fun imgName(): String {
        return "Screenshot" + Date().time
    }

    private fun shareScreenshot(path: String) {
        val imgToShare: Uri = Uri.parse(path)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_SUBJECT, "My art")
        intent.putExtra(Intent.EXTRA_TEXT, "my beautiful drawing")
        intent.putExtra(Intent.EXTRA_STREAM, imgToShare)

        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No app Avaliable", Toast.LENGTH_SHORT).show()
        }
    }
}
