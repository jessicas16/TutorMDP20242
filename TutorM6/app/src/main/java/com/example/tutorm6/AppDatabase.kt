package com.example.tutorm6

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
AppDatabase merupakan file yang digunakan untuk menginisiasi sebuah database dalam
local storage android.
File ini nyimpen variabel2 DAO yang kita buat ke depannya

Pertama, ada annotation(@)Database di mana di dalamnya ini kita isi array of entity.
Entity ini kayak sebuah tabel dalam database yang representasinya di kotlin bakal jadi sebuah
class .kt. Jadi semua Entity yang hendak digunakan dan disimpan dalam database ditaruh di
@Database
 */

@Database(entities = [UserEntity::class], version = 1)
// version itu penting karena nanti ketika kita update database kita bisa update versi database
// perubahan version menyebabkan database local di device user akan diupdate
abstract class AppDatabase: RoomDatabase(){
    // listkan semua DAO yg digunakan dlm project
    // jika menggunakan lebih dari 2 tabel, list semua Dao nya
    abstract fun userDao(): UserDao

    //ada dua cara inisiasi, cara pertama akan dijelaskan pada activity
    //cara kedua, menggunakan singleton pada instance database
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "prakm6"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
            /*
                Yang penting adalah 1 line di atas ini.
                Line di atas akan membuat sebuah database dengan nama "prakm7".
                Ketika aplikasi pertama kali dirun / diinstall dengan sebuah file AppDatabase,
                program bakal membuat sebuah database dengan tabel sebanyak entity yang dimasukkan
                dalam annotation @Database.

                Nah kalau misal salah buat tabel (misal column atau name)
                Ada dua cara:
                1. Ganti yang salah di source codenya. Uninstall aplikasinya di emulatornya.
                Reinstall aplikasi dengan database yang benar
                2. Ngganti yang salah di source codenya. Rerun dengan nama database SELAIN nama
                database sekarang.
                Misal: prakm7 -> prakm7_2
             */
        }



//        private var _database: AppDatabase? = null
//
//        fun build(context:Context?): AppDatabase {
//            if(_database == null){
//                _database = Room.databaseBuilder(context!!,AppDatabase::class.java,"prakm7").fallbackToDestructiveMigration().build()
//                /*
//                Yang penting adalah 1 line di atas ini.
//                Line di atas akan membuat sebuah database dengan nama "prakm7".
//                Ketika aplikasi pertama kali dirun / diinstall dengan sebuah file AppDatabase,
//                program bakal membuat sebuah database dengan tabel sebanyak entity yang dimasukkan
//                dalam annotation @Database.
//
//                Nah kalau misal salah buat tabel (misal column atau name)
//                Ada dua cara:
//                1. Ganti yang salah di source codenya. Uninstall aplikasinya di emulatornya.
//                Reinstall aplikasi dengan database yang benar
//                2. Ngganti yang salah di source codenya. Rerun dengan nama database SELAIN nama
//                database sekarang.
//                Misal: prakm7 -> prakm7_2
//                 */
//            }
//            return _database!!
//        }
    }
}