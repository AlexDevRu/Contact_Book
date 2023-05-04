package com.example.learning_android_contact_book_kulakov.ui.contact_details

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.learning_android_contact_book_kulakov.R
import com.example.learning_android_contact_book_kulakov.SharedPrefs
import com.example.learning_android_contact_book_kulakov.databinding.ActivityContactDetailsBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ContactDetailsActivity : AppCompatActivity(), View.OnClickListener, DialogInterface.OnClickListener {

    private lateinit var binding: ActivityContactDetailsBinding

    private val viewModel by viewModels<ContactDetailsViewModel>()

    private lateinit var uri: Uri

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            Glide.with(binding.ivPhoto)
                .load(uri)
                .into(binding.ivPhoto)
            viewModel.saveImage(uri)
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
            viewModel.saveImage(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvPhone.setOnClickListener(this)
        binding.tvEmail.setOnClickListener(this)
        binding.tvLink.setOnClickListener(this)
        binding.ivPhoto.setOnClickListener(this)

        observe()
    }

    private fun observe() {
        viewModel.contact.observe(this) {
            binding.tvNames.text = getString(R.string.contact_data_1, it.name, it.surname, it.patronymic)
            Glide.with(binding.ivPhoto)
                .load(it.imageUri)
                .error(R.drawable.person_black_24dp)
                .into(binding.ivPhoto)
            binding.tvPhone.text = it.phone
            binding.tvEmail.text = it.email
            binding.tvLink.text = it.link
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.tvPhone -> openDialer()
            binding.tvEmail -> openMailApp()
            binding.tvLink -> openLink()
            binding.ivPhoto -> {
                AlertDialog.Builder(this)
                    .setItems(R.array.select_photo, this)
                    .show()
            }
        }
    }

    override fun onClick(p0: DialogInterface?, position: Int) {
        when (position) {
            0 -> openCamera()
            1 -> openGallery()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(System.currentTimeMillis())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_",".jpg", storageDir)
        uri = FileProvider.getUriForFile(this, "com.example.learning_android_contact_book_kulakov.fileprovider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openDialer() {
        val phoneNumber = viewModel.contact.value?.phone ?: return
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }

    private fun openMailApp() {
        val email = viewModel.contact.value?.email ?: return
        val uri = Uri.parse("mailto:$email")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)
    }

    private fun openLink() {
        val link = viewModel.contact.value?.link ?: return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    companion object {
        const val CREATED = "CREATED"

        fun startActivity(context: Context, created: Long) {
            val intent = Intent(context, ContactDetailsActivity::class.java)
                .putExtra(CREATED, created)
            context.startActivity(intent)
        }
    }
}