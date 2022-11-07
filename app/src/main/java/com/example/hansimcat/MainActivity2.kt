package com.example.hansimcat

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import java.io.InputStream

class MainActivity2 : AppCompatActivity() {

    private val FROM_ALBUM = 1

    private val localModel = LocalModel.Builder()
        .setAssetFilePath("model.tflite")
        .build()

    private val options = CustomImageLabelerOptions.Builder(localModel)
        .setConfidenceThreshold(0.7f)
        .setMaxResultCount(5)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
//        startActivityForResult(intent, FROM_ALBUM)
        btn2()
    }

    private fun btn2() {
        val btn2: Button = findViewById(R.id.btnGallery)
        btn2.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*" // 이미지만
            intent.action = Intent.ACTION_PICK
            startActivityForResult(intent, FROM_ALBUM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val img: ImageView = findViewById(R.id.imageToLabel)
        if (requestCode == FROM_ALBUM) {
//        try {
//            val batchNum = 0
            val buf: InputStream? = data?.getData()?.let { contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(buf)
            buf?.close()
            bitmap.apply {
                img.setImageBitmap(this)
            }
            img.setImageBitmap(bitmap);

            val txtOutput: TextView = findViewById(R.id.txtOutput)
            val btn: Button = findViewById(R.id.btnTest)
            btn.setOnClickListener {
//            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                val labeler = ImageLabeling.getClient(options)
                val image = InputImage.fromBitmap(bitmap!!, 0)
                var outputText = ""
                labeler.process(image)
                    .addOnSuccessListener { labels ->
                        // Task completed successfully
                        for (label in labels) {
                            val text = label.text
                            val confidence = label.confidence
                            outputText += "$text : $confidence\n"
                            //val index = label.index
                        }
                        txtOutput.text = outputText
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                    }
            }
        }
        return;
    }
}