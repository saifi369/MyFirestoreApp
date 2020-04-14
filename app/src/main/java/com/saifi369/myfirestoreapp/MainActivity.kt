package com.saifi369.myfirestoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    //ui views
    private lateinit var et_name : EditText
    private lateinit var et_age : EditText
    private lateinit var tv_output : TextView
    private lateinit var btn_save : Button
    private lateinit var btn_read : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        btn_save.setOnClickListener { saveData() }
        btn_read.setOnClickListener { readData() }
    }

    private fun saveData() {
        //this method saves data in firestore

    }

    private fun readData() {
        //this method reads data from firestore

    }

    private fun initViews() {
        //this method initializes ui views
        et_name=findViewById(R.id.et_name)
        et_age=findViewById(R.id.et_age)
        tv_output=findViewById(R.id.tv_output)
        btn_save=findViewById(R.id.btn_save_data)
        btn_read=findViewById(R.id.btn_read_data)
    }
}
