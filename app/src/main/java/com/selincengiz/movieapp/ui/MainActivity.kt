package com.selincengiz.movieapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.selincengiz.movieapp.R
import com.selincengiz.movieapp.common.viewBinding
import com.selincengiz.movieapp.data.source.Database
import com.selincengiz.movieapp.databinding.ActivityMainBinding
import com.selincengiz.movieapp.databinding.AlertDialogBinding
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private var selectedImageBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        with(binding) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

            navHostFragment.navController.addOnDestinationChangedListener { controller, destination, _ ->
                if (destination.id == R.id.detailFragment) {
                    bottomNavigationView.visibility = View.GONE
                    floatingActionButton.visibility = View.GONE
                } else {
                    bottomNavigationView.visibility = View.VISIBLE
                    floatingActionButton.visibility = View.VISIBLE
                }
            }

            floatingActionButton.setOnClickListener {
                showAddDialog()
            }

            materialToolbar.title = "MovieApp"


        }
    }

    private fun showAddDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        val binding = AlertDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()

        var selectedSaveType = false
        val saveTypeList = listOf("Watch List", "Watched List")
        val saveTypeAdapter = ArrayAdapter(
            applicationContext,
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            saveTypeList
        )

        with(binding) {


            act.setAdapter(saveTypeAdapter)

            act.setOnItemClickListener { _, _, position, _ ->
                if (saveTypeList[position] == "Watch List") {
                    selectedSaveType = false
                } else if (saveTypeList[position] == "Watched List") {
                    selectedSaveType = true
                }
            }

            addBtn.setOnClickListener {
                val title = titleTextEdit.text.toString()
                val desc = descTextEdit.text.toString()
                val date = dateTextEdit.text.toString()

                if (title.isNotEmpty() && desc.isNotEmpty() && date.isNotEmpty() && selectedImageBitmap != null) {
                    Database.addFilm(title, desc, selectedSaveType, date, selectedImageBitmap)
                    dialog.dismiss()
                    selectedImageBitmap = null
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Fill The Blanks!!",
                        Toast.LENGTH_SHORT
                    ).show();
                }


            }

            cancelBtn.setOnClickListener {
                dialog.dismiss()
                selectedImageBitmap = null
            }

            image.setOnClickListener {
                openGallery()
                if (selectedImageBitmap != null) {
                    image.setImageBitmap(selectedImageBitmap)
                }
            }
        }

        dialog.show()
    }

    private val GALLERY_REQUEST_CODE = 100

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // İzin istemek için kullanıcıya açıklama gösterin ve izinleri isteyin
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                GALLERY_REQUEST_CODE
            )
        } else {
            // İzin zaten verilmişse galeriyi açın
            openGalleryIntent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzinler verildi, galeriyi açın
                openGalleryIntent()
            } else {
                // İzinler reddedildi veya iptal edildi, gerekli işlemi yapın (kullanıcıyı bilgilendirebilirsiniz)
                Toast.makeText(this, "Galeriye erişim izni reddedildi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageBitmap = getBitmapFromUri(selectedImageUri)


        }
    }

    private fun getBitmapFromUri(uri: Uri?): Bitmap? {
        uri?.let {
            try {
                val inputStream = contentResolver.openInputStream(it)
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        return null
    }


}