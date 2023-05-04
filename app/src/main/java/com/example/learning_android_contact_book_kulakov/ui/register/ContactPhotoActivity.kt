package com.example.learning_android_contact_book_kulakov.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.learning_android_contact_book_kulakov.R
import com.example.learning_android_contact_book_kulakov.SharedPrefs
import com.example.learning_android_contact_book_kulakov.databinding.ActivityContactPhotoBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ContactPhotoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityContactPhotoBinding

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            Glide.with(binding.ivPhoto)
                .load(SharedPrefs.uri)
                .into(binding.ivPhoto)
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult
            Glide.with(binding.ivPhoto)
                .load(uri)
                .into(binding.ivPhoto)
        }
    }

    private var name = ""
    private var surname = ""
    private var patronymic = ""
    private var phone = ""
    private var email = ""
    private var link = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra(AdditionalDataActivity.NAME) ?: SharedPrefs.name.orEmpty()
        surname = intent.getStringExtra(AdditionalDataActivity.SURNAME) ?: SharedPrefs.surname.orEmpty()
        patronymic = intent.getStringExtra(AdditionalDataActivity.PATRONYMIC) ?: SharedPrefs.patronymic.orEmpty()
        phone = intent.getStringExtra(PHONE) ?: SharedPrefs.phone.orEmpty()
        email = intent.getStringExtra(EMAIL) ?: SharedPrefs.email.orEmpty()
        link = intent.getStringExtra(LINK) ?: SharedPrefs.link.orEmpty()

        binding.tvContactData1.text = getString(R.string.contact_data_1, name, surname, patronymic)
        binding.tvContactData2.text = getString(R.string.contact_data_2, phone, email, link)

        SharedPrefs.uri?.let {
            Glide.with(binding.ivPhoto)
                .load(it)
                .into(binding.ivPhoto)
        }

        binding.btnNext.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnAddFromCamera.setOnClickListener(this)
        binding.btnAddFromGallery.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnBack -> finish()
            binding.btnNext -> goNext()
            binding.btnAddFromCamera -> openCamera()
            binding.btnAddFromGallery -> openGallery()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(System.currentTimeMillis())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_",".jpg", storageDir)
        val uri = FileProvider.getUriForFile(this, "com.example.learning_android_contact_book_kulakov.fileprovider", file)
        SharedPrefs.uri = uri.toString()
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun goNext() {
        val uri = SharedPrefs.uri.orEmpty()
        SummaryActivity.startActivity(this, name, surname, patronymic, phone, email, link, uri)
    }

    companion object {
        const val PHONE = "PHONE"
        const val EMAIL = "EMAIL"
        const val LINK = "LINK"

        fun startActivity(
            context: Context,
            name: String,
            surname: String,
            patronymic: String,
            phone: String,
            email: String,
            link: String
        ) {
            val intent = Intent(context, ContactPhotoActivity::class.java)
                .putExtra(AdditionalDataActivity.NAME, name)
                .putExtra(AdditionalDataActivity.SURNAME, surname)
                .putExtra(AdditionalDataActivity.PATRONYMIC, patronymic)
                .putExtra(PHONE, phone)
                .putExtra(EMAIL, email)
                .putExtra(LINK, link)
            context.startActivity(intent)
        }
    }

}