package com.homecare.homecare

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isEmpty
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeCarectprofile : AppCompatActivity() {
    //defining widgets
    private lateinit var ctprofiletextview : TextView
    private lateinit var ctprofile_username: TextInputEditText
    private lateinit var ctprofile_contactno: TextInputEditText
    private lateinit var ctprofile_nicno: TextInputEditText
    private lateinit var ctprofile_dob: TextInputEditText
    private lateinit var ctprofile_gender: Spinner
    private lateinit var ctprofile_district: Spinner
    private lateinit var ctprofile_tos: Spinner
    private lateinit var ctprofile_description: EditText
    private lateinit var btn_ctprofile_cancel:Button
    private lateinit var btn_ctprofile_update:Button

    private lateinit var userName: String
    private lateinit var contactNumber: String
    private lateinit var nicNumber: String
    private lateinit var dob: String
    private lateinit var gender: String
    private lateinit var district: String
    private lateinit var tos: String
    private lateinit var description: String

    //defining variable
    private lateinit var string_gender: String
    private lateinit var string_district: String
    private lateinit var string_tos: String
    private lateinit var uid: String

    //Defining Firebase realtime database instance
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_carectprofile)

        //Assigning widgets
        ctprofiletextview = findViewById(R.id.ctprofiletextview)
        ctprofile_username = findViewById(R.id.ctprofile_username)
        ctprofile_contactno = findViewById(R.id.ctprofile_contactno)
        ctprofile_nicno = findViewById(R.id.ctprofile_nicno)
        ctprofile_dob = findViewById(R.id.ctprofile_dob)
        ctprofile_gender =  findViewById(R.id.ctprofile_gender)
        ctprofile_district = findViewById(R.id.ctprofile_district)
        ctprofile_tos = findViewById(R.id.ctprofile_tos)
        ctprofile_description = findViewById(R.id.ctprofile_description)
        btn_ctprofile_cancel = findViewById(R.id.ctprofile_cancel)
        btn_ctprofile_update = findViewById(R.id.ctprofile_update)

        ctprofile_tos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                string_tos = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                string_tos =""
            }
        }

        ctprofile_district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                string_district = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                string_district = ""
            }
        }

        ctprofile_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                string_gender = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                string_gender = ""
            }
        }

        //Assigning database reference to Firebase realtime database reference instance
        database = FirebaseDatabase.getInstance().getReference("HomeCare")

        //Assigning user id to uid
        uid = Firebase.auth.currentUser?.uid.toString()

        database.child("userCARETAKER").child(uid).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")

            val state_published = it.child("published").value.toString()
            userName = it.child("userName").value.toString()
            contactNumber = it.child("contactNumber").value.toString()
            nicNumber = it.child("nicNumber").value.toString()
            dob = it.child("dateofbirth").value.toString()
            gender = it.child("gender").value.toString()
            district = it.child("district").value.toString()
            tos = it.child("typeofservice").value.toString()
            description = it.child("description").value.toString()

            if(description.equals("null")){
                ctprofile_description.setText("")
            }else {
                ctprofile_description.setText(description)
            }
            if(userName.equals("null")){
                ctprofile_username.setText("")
            }else {
                ctprofile_username.setText(userName)
            }
            if(contactNumber.equals("null")){
                ctprofile_contactno.setText("")
            }else {
                ctprofile_contactno.setText(contactNumber)
            }
            if(nicNumber.equals("null")){
                ctprofile_nicno.setText("")
            }else {
                ctprofile_nicno.setText(nicNumber)
            }
            if(dob.equals("null")){
                ctprofile_dob.setText("")
            }else {
                ctprofile_dob.setText(dob)
            }

            setctprofile_gendervalue(gender)
            setctprofile_tosvalue(tos)
            setctprofile_districtvalue(district)

            if(state_published.equals("0")){

                ctprofiletextview.text = "Update your profile to publish"

            }else if(state_published.equals("1")){

                ctprofiletextview.text = "Your profile was published."

            }

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        btn_ctprofile_cancel.setOnClickListener{
            cancelbtn()
        }

        btn_ctprofile_update.setOnClickListener{
            updateprofile()
        }

    }

    private fun setctprofile_gendervalue(gender:String){
        when (gender) {
            "Male" -> ctprofile_gender.setSelection(1)
            "Female" -> ctprofile_gender.setSelection(2)
            else -> {
                ctprofile_gender.setSelection(0)
            }
        }
    }
    private fun setctprofile_tosvalue(tos:String){
        when (tos) {
            "Doctor" -> ctprofile_tos.setSelection(1)
            "Physio" -> ctprofile_tos.setSelection(2)
            "Nurse" -> ctprofile_tos.setSelection(3)
            "Care Taker" -> ctprofile_tos.setSelection(4)
            "Elder Care" -> ctprofile_tos.setSelection(5)
            "Post Natal Care" -> ctprofile_tos.setSelection(6)
            else -> {
                ctprofile_tos.setSelection(0)
            }
        }
    }
    private fun setctprofile_districtvalue(district:String){
        when (district) {
            "Colombo" -> ctprofile_district.setSelection(1)
            "Gampaha" -> ctprofile_district.setSelection(2)
            "Kalutara" -> ctprofile_district.setSelection(3)
            "Kandy" -> ctprofile_district.setSelection(4)
            "Matale" -> ctprofile_district.setSelection(5)
            "Nuwara Eliya" -> ctprofile_district.setSelection(6)
            "Galle" -> ctprofile_district.setSelection(7)
            "Matara" -> ctprofile_district.setSelection(8)
            "Hambantota" -> ctprofile_district.setSelection(9)
            "Jaffna" -> ctprofile_district.setSelection(10)
            "Kilinochchi" -> ctprofile_district.setSelection(11)
            "Mannar" -> ctprofile_district.setSelection(12)
            "Vavuniya" -> ctprofile_district.setSelection(13)
            "Mullaitivu" -> ctprofile_district.setSelection(14)
            "Batticaloa" -> ctprofile_district.setSelection(15)
            "Ampara" -> ctprofile_district.setSelection(16)
            "Trincomalee" -> ctprofile_district.setSelection(17)
            "Kurunegala" -> ctprofile_district.setSelection(18)
            "Puttalam" -> ctprofile_district.setSelection(19)
            "Anuradhapura" -> ctprofile_district.setSelection(20)
            "Polonnaruwa" -> ctprofile_district.setSelection(21)
            "Badulla" -> ctprofile_district.setSelection(22)
            "Moneragala" -> ctprofile_district.setSelection(23)
            "Ratnapura" -> ctprofile_district.setSelection(24)
            "Kegalle" -> ctprofile_district.setSelection(25)
            else -> {
                ctprofile_tos.setSelection(0)
            }
        }
    }
    private fun cancelbtn(){
        ctprofile_username.setText("")
        ctprofile_contactno.setText("")
        ctprofile_nicno.setText("")
        ctprofile_dob.setText("")
        ctprofile_description.setText("")
        var intent = Intent(this,HomeCaredashboardct::class.java)
        startActivity(intent)
    }
    private fun updateprofile(){
        //getting values
        val userName = ctprofile_username.text.toString()
        val contactNumber = ctprofile_contactno.text.toString()
        val nicNumber = ctprofile_nicno.text.toString()
        val dateofbirth = ctprofile_dob.text.toString()
        val description = ctprofile_description.text.toString()
        val gender = string_gender


        if(description.isEmpty()){
            ctprofile_description.error = "Describe about your proffessional skills"
        }else if(userName.isEmpty()){
            ctprofile_username.error = "Enter your User Name"
        }else if(contactNumber.isEmpty()){
            ctprofile_contactno.error = "Enter your contact number"
        }else if(nicNumber.isEmpty()){
            ctprofile_nicno.error = "Enter your National Identity number"
        }else if(dateofbirth.isEmpty()){
            ctprofile_dob.error = "Enter your Date of birth"
        }

        //pass data to model
        val caretaker = HomeCarectuserModel(userName,contactNumber,nicNumber,dateofbirth,gender,"1","",description,string_district,string_tos)

        database.child("userCARETAKERCITY").child(district).child(uid).removeValue()
        database.child("userCARETAKERTOS").child(tos).child(uid).removeValue()

        database.child("userCARETAKERCITY").child(string_district).child(uid).setValue(uid)
        database.child("userCARETAKERTOS").child(string_tos).child(uid).setValue(uid)

        //pass data to firebase realtime database
        database.child("userCARETAKER").child(uid).setValue(caretaker)
            .addOnSuccessListener {
                Toast.makeText(this,"Registered Successfully", Toast.LENGTH_LONG).show()
                cancelbtn()

            }.addOnFailureListener{ err->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
            }


    }
}