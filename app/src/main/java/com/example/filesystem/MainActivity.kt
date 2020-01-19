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
import android.view.View.INVISIBLE
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private val requestCode = 100
    private var path = ""

    private var arr = arrayListOf<Files>()
    private var currentArr = arrayListOf<Files>()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycle.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycle.adapter = Adapter(arr) {
            listener(it)
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
                    arr.add(Files(file.name, if (file.isDirectory) 1 else 0))
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
                    currentArr.add(Files(x.name, if (x.isDirectory) 1 else 0))
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
        var file = File(path + "/" + if (currentArr.isEmpty()) arr[pos].name else currentArr[pos].name)
        if (file.isDirectory) {
            path = path + "/" + if (currentArr.isEmpty()) arr[pos].name else currentArr[pos].name
            val files = file.listFiles()
            currentArr.clear()
            for (x in files) {
                if (x != null) {
                    currentArr.add(Files(x.name, if (x.isDirectory) 1 else 0))
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

}
