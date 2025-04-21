package com.example.tutorm6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.tutorm6.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
Local Storage :D
Digunakan untuk menyimpan data layaknya sebuah database dalam aplikasi Android.
Data yang disimpan tidak akan hilang walaupun aplikasi ditutup
Kalo diuninstall ya ilang :D
Ada 3 hal yang harus diurus ketika mengimplementkan local storage:
1. AppDatabase
2. DAO
3. Entity Model

Sebelum itu, pastikan sudah nambahi keempat implementation ini di build.gradle dependencies
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

Tambahkan ini pada plugin
id("kotlin-kapt")

===================================== DB DEBUG =====================================
Jika mau melihat isi dari DB, kalian bisa ikuti langkah berikut
1. Buka App Inspection pada bagian bawah (sebelah Logcat)
2. Pilih Database Inspector.
3. Pilih tabel mana yang mau dilihat
4. Centang Live Update jika mau melihat perubahan isi database secara langsung

 */

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val viewModel by viewModels<UserViewModel>()

    //Deklarasi variabel AppDatabase, jika tidak memakai ViewModel (tidak disarankan)
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.init()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //cara pertama inisiasi instance
//        db = Room.databaseBuilder(baseContext, AppDatabase::class.java, "prakm7").fallbackToDestructiveMigration().build()
        // .fallbackToDestructiveMigration() berguna jika kita menaikan versiond dari db
        // kita tidak perlu melakukan migrate. Biasanya akan diminta untuk migrate jika menaikkan version db

        //cara kedua, akan menggunakan class App sebagai tempat dimana viewModel dapat mengakses db langsung tanpa terikat dengan Activity

        //==============================================

        val userAdapter = UserAdapter()

        userAdapter.onEditClickListener = { user ->
            viewModel.setActiveUser(user.username)
        }

        userAdapter.onDeleteClickListener = { user ->
            viewModel.deleteStudent(user)
        }

        binding.rvUser.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvUser.adapter = userAdapter

        binding.btnSave.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassword.text.toString()
            val name = binding.txtName.text.toString()
            val gender = binding.etGender.text.toString()

            if (name == "" || username == "" || password == "" || gender == "") {
                Toast.makeText(this, "All field is required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.putUser(UserEntity(username, password, name, gender))
        }

        val usersObserver = Observer<List<UserEntity>> {
            userAdapter.submitList(it)
        }
        viewModel.users.observe(this, usersObserver)

        val modeForm = Observer<UserEntity> {
            binding.btnSave.text = if (it.username == "") "Insert" else "Update"
            binding.txtUsername.isEnabled = it.username == ""
        }
        viewModel.activeUser.observe(this, modeForm)
    }
}