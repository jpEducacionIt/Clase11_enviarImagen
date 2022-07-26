package com.example.clase11_enviarimagen

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermisos()
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)

        imageView.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentImage, 123)
        }

        button.setOnClickListener {
            val intentSend = Intent(Intent.ACTION_SEND)
            intentSend.type = "image/*"
            //intentSend.setPackage("com.whastsapp")

            if (uri != null) {
                intentSend.putExtra(Intent.EXTRA_STREAM, uri)

                try {
                    startActivity(intentSend)
                }catch (e: Exception) {
                    Toast.makeText(this, "error al enviar foto ${e.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "no se selecciono ninguna foto", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermisos() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            uri = data.data
            imageView.setImageURI(uri)
        }
    }
}