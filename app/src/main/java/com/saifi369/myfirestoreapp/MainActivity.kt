package com.saifi369.myfirestoreapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    //ui views
    private lateinit var et_name: EditText
    private lateinit var et_age: EditText
    private lateinit var tv_output: TextView
    private lateinit var btn_save: Button
    private lateinit var btn_read: Button

    private lateinit var firestoreDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        firestoreDatabase = FirebaseFirestore.getInstance()

        btn_save.setOnClickListener { saveData() }
        btn_read.setOnClickListener { readData() }
    }

    private fun saveData() {
        //this method saves data in firestore

        val name = et_name.text.toString().trim()
        val age = et_age.text.toString().toInt()

        var data = hashMapOf<String, Any>()

        data["name"] = name
        data["age"] = age

        firestoreDatabase.collection("users").document()
            .set(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    tv_output.text = "Data is saved"
                    Toast.makeText(MainActivity@ this, "Data is inserted", Toast.LENGTH_LONG).show()
                } else {
                    tv_output.text = task.exception!!.message.toString()
                }
            }

//        val docData = hashMapOf(
//            "stringExample" to "Hello world!",
//            "booleanExample" to true,
//            "numberExample" to 3.14159265,
//            "dateExample" to Timestamp(Date()),
//            "listExample" to arrayListOf(1, 2, 3),
//            "nullExample" to null
//        )
//
//        val nestedData = hashMapOf(
//            "a" to 5,
//            "b" to true
//        )
//
//        docData["objectExample"] = nestedData
//
//        firestoreDatabase.collection("test").document("data")
//            .set(docData)
//            .addOnSuccessListener {
//                tv_output.text="Data is inserted"
//            }

    }

    private fun readData() {
        //this method reads data from firestore

    }

    private fun initViews() {
        //this method initializes ui views
        et_name = findViewById(R.id.et_name)
        et_age = findViewById(R.id.et_age)
        tv_output = findViewById(R.id.tv_output)
        btn_save = findViewById(R.id.btn_save_data)
        btn_read = findViewById(R.id.btn_read_data)
    }
}
