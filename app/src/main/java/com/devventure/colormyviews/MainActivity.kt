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
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.devventure.colormyviews.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URI
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
    fun shareScreenScreenshot(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        } else {
            val rootView = window.decorView.findViewById<View>(android.R.id.content)
            val bitmap = takeScreenshot(rootView)
            if (bitmap != null) {
                storeScreenshot(bitmap, "Screenshot1")
            }
        }
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

    private fun storeScreenshot(bitmap: Bitmap, filename: String) {
        var dirPath = applicationContext.getExternalFilesDir(null).toString()
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dirPath, filename)
        try {
            File(dirPath, "$filename.png").writeBitmap(bitmap, Bitmap.CompressFormat.PNG, 85)
            shareScreenshot(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareScreenshot(file: File) {
        val uri = FileProvider.getUriForFile(
            applicationContext,
            application.applicationContext.packageName + ".provider",
            file
        )
        val intent = Intent()

        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_SUBJECT, "My art")
        intent.putExtra(Intent.EXTRA_TEXT, "my beautiful drawing")
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No app Avaliable", Toast.LENGTH_SHORT).show()
        }

    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }
}
