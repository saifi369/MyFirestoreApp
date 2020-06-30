package com.saifi369.myfirestoreapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var firestoreDatabase: FirebaseFirestore
    private lateinit var userCollectionRef: CollectionReference

    private val KEY_NAME = "key_name"
    private val KEY_AGE = "key_age"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestoreDatabase = FirebaseFirestore.getInstance()
        userCollectionRef = firestoreDatabase
            .collection("users")

        btn_save_data.setOnClickListener { saveData() }
        btn_read_data.setOnClickListener { readData() }
    }

    override fun onStart() {
        super.onStart()

        userCollectionRef.addSnapshotListener(this) { snapshot, e ->

            if (e != null) {
                Toast.makeText(MainActivity@ this, "Error while loading data", Toast.LENGTH_LONG)
                    .show()
                Log.d("MyTag", e.localizedMessage)
                return@addSnapshotListener
            } else {
                tv_output.text = ""
                for (document in snapshot!!) {
                    val person = document.toObject(Person::class.java)
                    person.personId = document.id
                    showOutput(person.toString())
                }
            }
        }

    }

    private fun saveData() {
        //this method saves data in firestore

        val name = et_name.text.toString().trim()
        val age = et_age.text.toString().toInt()

        val person = Person(name, age)

        userCollectionRef.document().set(person)
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

        userCollectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                tv_output.text = ""
                for (document in querySnapshot) {
                    val person = document.toObject(Person::class.java)
                    person.personId = document.id
                    showOutput(person.toString())
                }
            }
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
