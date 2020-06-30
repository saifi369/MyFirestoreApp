package com.saifi369.myfirestoreapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var firestoreDatabase: FirebaseFirestore
    private lateinit var userDocumentRef: DocumentReference

    private val KEY_NAME = "key_name"
    private val KEY_AGE = "key_age"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestoreDatabase = FirebaseFirestore.getInstance()
        userDocumentRef = firestoreDatabase
            .collection("users").document("person1")

        btn_save_data.setOnClickListener { saveData() }
        btn_read_data.setOnClickListener { readData() }
        btn_update_data.setOnClickListener { updateData() }
        btn_delete_person.setOnClickListener { deletePerson() }
        btn_delete_field.setOnClickListener { deleteName() }
    }

    override fun onStart() {
        super.onStart()
        userDocumentRef.addSnapshotListener(this) { snapshot, e ->

            if (e != null) {
                Toast.makeText(MainActivity@ this, "Error while loading data", Toast.LENGTH_LONG)
                    .show()
                Log.d("MyTag", e.localizedMessage)
                return@addSnapshotListener
            } else {
                if (snapshot != null && snapshot.exists()) {

                    val person = snapshot.toObject(Person::class.java)
                    showOutput(person.toString())
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

        val person = Person(name, age)

        userDocumentRef.set(person)
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

                    val person = document.toObject(Person::class.java)

                    showOutput(person.toString())

                } else {
                    showOutput("Person does not exist")
                }
            }
            .addOnFailureListener {
                showOutput(it.localizedMessage)
            }

    }

    private fun updateData() {
        //this method updates data in firestore

        val name = et_name.text.toString().trim()
        val age = et_age.text.toString().toInt()

        val data: HashMap<String, Any> = hashMapOf(
            KEY_NAME to name,
            KEY_AGE to age
        )

//        userDocumentRef.set(data, SetOptions.merge())

//        userDocumentRef.update(KEY_NAME,name)

    }

    private fun deleteName() {
        //this method deletes a single field from any firestore document

//        val data: HashMap<String, Any> = hashMapOf(
//            KEY_NAME to FieldValue.delete()
//        )
//
//        userDocumentRef.update(data)

        userDocumentRef.update(KEY_NAME, FieldValue.delete())

    }

    private fun deletePerson() {
        //this method deletes a complete document from firestore database

        userDocumentRef.delete()

    }

    private fun showOutput(text: String) {
        if (tv_output.text == "Ready to Learn!")
            tv_output.text = ""

        tv_output.append("$text\n")
        scroll_view.post {
            scroll_view.fullScroll(View.FOCUS_DOWN)
        }

    }
}
