package com.saifi369.myfirestoreapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Source

class MainActivity : AppCompatActivity() {

    //ui views
    private lateinit var et_name: EditText
    private lateinit var et_age: EditText
    private lateinit var tv_output: TextView
    private lateinit var btn_save: Button
    private lateinit var btn_read: Button
    private lateinit var scrollview: ScrollView

    private lateinit var firestoreDatabase: FirebaseFirestore
    private lateinit var userDocumentRef: DocumentReference

    private val KEY_NAME = "key_name"
    private val KEY_AGE = "key_age"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        firestoreDatabase = FirebaseFirestore.getInstance()
        userDocumentRef = firestoreDatabase
            .collection("users").document("person1")

        btn_save.setOnClickListener { saveData() }
        btn_read.setOnClickListener { readData() }
    }

    override fun onStart() {
        super.onStart()
        userDocumentRef.addSnapshotListener(this, MetadataChanges.INCLUDE) { snapshot, e ->

            if (e != null) {
                Toast.makeText(MainActivity@ this, "Error while loading data", Toast.LENGTH_LONG)
                    .show()
                Log.d("MyTag", e.localizedMessage)
                return@addSnapshotListener
            } else {
                if (snapshot != null && snapshot.exists()) {

                    val name = snapshot.getString(KEY_NAME)
                    val age = snapshot.getLong(KEY_AGE)

                    val source = if (snapshot.metadata.hasPendingWrites())
                        "Local"
                    else
                        "Server"

                    showOutput(name!!)
                    showOutput(age.toString())
                    showOutput(source)

                } else {
                    showOutput("Person does not exist")
                }
            }
        }

    }
    private fun saveData() {
        //this method saves data in firestore

        val name = et_name.text.toString().trim()
        val age = et_age.text.toString().toInt()

        var data = hashMapOf<String, Any>()

        data[KEY_NAME] = name
        data[KEY_AGE] = age

        userDocumentRef.set(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showOutput("Data is saved")
                    Toast.makeText(MainActivity@ this, "Data is inserted", Toast.LENGTH_LONG).show()
                } else {
                    showOutput(task.exception!!.message.toString())
                }
            }
    }

    private fun readData() {
        //this method reads data from firestore

        userDocumentRef.get(Source.DEFAULT)

            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val name = document.getString(KEY_NAME)
                    val age = document.getLong(KEY_AGE)

//                    val data = document.data
//
//                    val name=data?.get(KEY_NAME).toString()
//                    val age=data?.get(KEY_AGE).toString()

                    showOutput(name!!)
                    showOutput(age.toString())

                } else {
                    showOutput("Person does not exist")
                }
            }
            .addOnFailureListener {
                showOutput(it.localizedMessage)
            }

    }

    private fun showOutput(text: String) {
        if (tv_output.text == "Ready to Learn!")
            tv_output.text = ""

        tv_output.append("$text\n")
        scrollview.post {
            scrollview.fullScroll(View.FOCUS_DOWN)
        }

    }

    private fun initViews() {
        //this method initializes ui views
        et_name = findViewById(R.id.et_name)
        et_age = findViewById(R.id.et_age)
        tv_output = findViewById(R.id.tv_output)
        btn_save = findViewById(R.id.btn_save_data)
        btn_read = findViewById(R.id.btn_read_data)
        scrollview = findViewById(R.id.scroll_view)
    }
}
