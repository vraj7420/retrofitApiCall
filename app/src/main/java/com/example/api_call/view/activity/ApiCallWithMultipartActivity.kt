package com.example.api_call.view.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.api_call.R
import com.example.api_call.network.BaseService
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_api_call_with_multipart.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ApiCallWithMultipartActivity : AppCompatActivity() {
    private lateinit var mediaPath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_call_with_multipart)
        setListener()
    }

    private fun setListener() {
        btnSelectImage.setOnClickListener {
            requestMultiplePermissions()
        }

        btnUploadImage.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val file1 = File(mediaPath)
        val requestBody1 = RequestBody.create(MediaType.parse("image/*"), file1)
        val fileToUpload1 = MultipartBody.Part.createFormData("file", file1.name, requestBody1)
        val filename1 = RequestBody.create(MediaType.parse("text/plain"), file1.name)
        BaseService().postImageBaseApi().uploadImage(fileToUpload1, filename1)
            .enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    Toast.makeText(
                        this@ApiCallWithMultipartActivity,
                        "Successfully Post",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Toast.makeText(
                        this@ApiCallWithMultipartActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })
    }

    fun selectImage() {
        val intentSelectImage = Intent()
        intentSelectImage.type = "image/*"
        intentSelectImage.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intentSelectImage, 101)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            try {
                val selectedImage = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                mediaPath = saveImage(bitmap)
                imgSelected.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    private fun requestMultiplePermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,

                )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        selectImage()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(applicationContext, "Some Error! ", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()
    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory(), "")
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val f =
                File(wallpaperDirectory, Calendar.getInstance().timeInMillis.toString() + ".jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

}