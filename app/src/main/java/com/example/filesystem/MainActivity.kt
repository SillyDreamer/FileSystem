package com.example.filesystem

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.content.Intent
import android.graphics.Color
import androidx.preference.PreferenceManager


class MainActivity : AppCompatActivity() {

    private val requestCode = 100
    private var path = ""

    private var arr = arrayListOf<Files>()
    private var currentArr = arrayListOf<Files>()
    var adapter : Adapter? = null

    private var flag = false
    private var flag_color = false

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = Adapter(arr) {
            listener(it)
        }
        recycle.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycle.adapter = adapter
        adapter!!.setTextSize(18)

        but_pref.setOnClickListener {
            val openSettings = Intent(this@MainActivity, PreferencesActivity::class.java)
            startActivity(openSettings)
        }
    }

    private fun listExternalStorage() {
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            listFiles(Environment.getExternalStorageDirectory())
        }
    }

    private fun listFiles(directory: File) {
        path = directory.absoluteFile.toString()
        println(path)
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file != null) {
                    arr.add(Files(if(!flag) file.name else file.absolutePath, if (file.isDirectory) 1 else 0))
                }
            }
            recycle.adapter!!.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == this.requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listExternalStorage()
            } else {
                Toast.makeText(this, getString(R.string.permission), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun back(view : View) {
            if (!path.equals(Environment.getExternalStorageDirectory().absoluteFile.toString())) {
                path = path.substringBeforeLast("/")
                var file = File(path)
                val files = file.listFiles()
                currentArr.clear()
                for (x in files) {
                    if (x != null) {
                        currentArr.add(
                            Files(
                                if (!flag) x.name else x.absolutePath,
                                if (x.isDirectory) 1 else 0
                            )
                        )
                    }
                }
                recycle.adapter!!.notifyDataSetChanged()
            }
    }

    fun list(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)
        } else {
            listExternalStorage()
        }
        btnList.visibility = GONE
    }

    fun listener(pos : Int) {
        var file : File
        if (!flag)
            file = File(path + "/" + if (currentArr.isEmpty()) arr[pos].name else currentArr[pos].name)
        else
            file = File(if (currentArr.isEmpty()) arr[pos].name else currentArr[pos].name)
        if (file.isDirectory) {
            if (!flag)
                path = path + "/" + if (currentArr.isEmpty()) arr[pos].name else currentArr[pos].name
            else
                path = if (currentArr.isEmpty()) arr[pos].name else currentArr[pos].name
            val files = file.listFiles()
            currentArr.clear()
            for (x in files) {
                if (x != null) {
                    currentArr.add(Files(if(!flag) x.name else x.absolutePath, if (x.isDirectory) 1 else 0))
                }
            }
            recycle.adapter = Adapter(currentArr) {
                listener(it)
            }
            recycle.adapter!!.notifyDataSetChanged()
        }
        else {
            Toast.makeText(this, "${path + "/" + if (currentArr.isEmpty()) arr[pos].name else currentArr[pos].name} its not a directory", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()


        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        val size = preferences.getString(getString(R.string.pref_text_size_key), "18" )
        if (size != null) {
            adapter!!.setTextSize(size.toString().toInt())
        }
        flag = preferences.getBoolean(getString(R.string.pref_path_key), false)
            val newfile = File(path)
            val files = newfile.listFiles()
            if (files != null) {
                currentArr.clear()
                for (file in files) {
                    if (file != null) {
                        currentArr.add(Files(if(!flag) file.name else file.absolutePath, if (file.isDirectory) 1 else 0))
                    }
                }
                var adapter = Adapter(currentArr) {
                    listener(it)
                }
                adapter.setTextSize(size.toString().toInt())
                recycle.adapter = adapter

            }



        flag_color = preferences.getBoolean(getString(R.string.pref_theme_key), false)
        if (flag_color) {
            main_layout.background = getDrawable(R.drawable.night)
            but_pref.background = getDrawable(R.drawable.night_button)
            but_pref.setTextColor(Color.WHITE)
            back.background = getDrawable(R.drawable.night_button)
            back.setTextColor(Color.WHITE)
            btnList.background = getDrawable(R.drawable.night_button)
            btnList.setTextColor(Color.WHITE)
        }
        else {
            main_layout.setBackgroundColor(Color.WHITE)
            but_pref.setBackgroundColor(Color.argb(100, 140,145,171))
            but_pref.setTextColor(Color.BLACK)
            back.setBackgroundColor(Color.argb(100, 140,145,171))
            back.setTextColor(Color.BLACK)
            btnList.setBackgroundColor(Color.argb(100, 140,145,171))
            btnList.setTextColor(Color.BLACK)
        }



    }
}
