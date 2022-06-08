package com.homecare.homecare

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.net.PasswordAuthentication
import java.sql.DatabaseMetaData
import java.text.SimpleDateFormat
import java.util.*

class HomeCarerg : AppCompatActivity() {

    //Defining widgets
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var email: EditText
    private lateinit var mobileNo: EditText
    private lateinit var nic: EditText
    private lateinit var dob: EditText
    private lateinit var gender: Spinner
    private lateinit var district: Spinner
    private lateinit var tos: Spinner
    private lateinit var pw1: TextInputEditText
    private lateinit var pw2: TextInputEditText
    private lateinit var btn_reg: Button
    private lateinit var btn_datepicker: Button

    //defining variable
    private lateinit var string_gender: String
    private lateinit var string_district: String
    private lateinit var string_tos: String

    //Firebase database reference
    private lateinit var dref: DatabaseReference

    //Defining Firebase auth instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_carerg)

        //Assigning widgets
        firstName = findViewById(R.id.editTextfirstName)
        lastName = findViewById(R.id.editTextLastname)
        email = findViewById(R.id.editTextEmailAddress)
        mobileNo = findViewById(R.id.editTextPhoneno)
        nic = findViewById(R.id.editTextNic)
        dob = findViewById(R.id.editTextDob)
        gender = findViewById(R.id.spinnerGender)
        district = findViewById(R.id.spinnerDistrict)
        tos = findViewById(R.id.spinnerrgtos)
        pw1 =  findViewById(R.id.editTextpassowrd)
        pw2 = findViewById(R.id.editTextCpassword)
        btn_reg = findViewById(R.id.btn_register)
        btn_datepicker = findViewById(R.id.btn_datepicker)

        // Initialize Firebase Auth
        auth = Firebase.auth

        tos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                string_tos = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                string_tos =""
            }
        }

        district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                string_district = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                string_district = ""
            }
        }

        gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                string_gender = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                string_gender = ""
            }
        }

        //assigning calender into new instance
        var myCalender = Calendar.getInstance()

        //defining date picker instance
        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            myCalender.set(Calendar.YEAR, year)
            myCalender.set(Calendar.MONTH, month)
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updatelabel(myCalender)
        }

        btn_datepicker.setOnClickListener{
            DatePickerDialog(this,datePicker,myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH)).show()
        }

        //Defining Firebase database reference
        dref = FirebaseDatabase.getInstance("https://homecare-29a72-default-rtdb.firebaseio.com/").getReference("HomeCare")

        //registering user
        btn_reg.setOnClickListener{
            saveEmployeeData()
        }

        //starting HomeCarelg activity
        val btn_lgin = findViewById<Button>(R.id.btn_login)
        btn_lgin.setOnClickListener{
            val intent = Intent(this,HomeCarelg::class.java)
            startActivity(intent)
        }
    }

    //method for assigning date picker value into label
    private fun updatelabel(myCalender: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dob.setText(sdf.format(myCalender.time))


    }

    //method for registering ne user
    private fun saveEmployeeData(){
        //getting values
        val firstName_t = firstName.text.toString()
        val lastName_t = lastName.text.toString()
        val userName = firstName_t +" "+ lastName_t
        val emailAddress = email.text.toString()
        val contactNumber = mobileNo.text.toString()
        val nicNumber = nic.text.toString()
        val dateofbirth = dob.text.toString()
        val password1 = pw1.text.toString()
        val password2 =  pw2.text.toString()

        //textbox validation
        if(firstName_t.isEmpty()){
            firstName.error = "Please enter First Name"
        } else if(lastName_t.isEmpty()){
            lastName.error = "Please enter Last Name"
        } else if(emailAddress.isEmpty()){
            email.error = "Please enter Email"
        } else if(contactNumber.isEmpty()){
            mobileNo.error = "Please enter mobile number"
        } else if(nicNumber.isEmpty()){
            nic.error = "Please enter National Identity number"
        }else if(dateofbirth.isEmpty()){
            dob.error = "Plese enter your date of birth"
        }else if(string_district.equals("- District -")){
            string_district=""
        }else if (string_gender.equals("- Gender -")){
            string_gender=""
        }else if(string_tos.equals("- Select the type of your service -")){
            string_tos=""
        }else if(password1.isEmpty()){
            pw1.error = "Please enter your Password here"
        } else if(password2.isEmpty()){
            pw2.error = "Please enter your Password here"
        }else if(password1!=password2){
            pw1.error = "Your password not matched"
            pw2.error = "Your password not matched"
        } else{
            //Create user account using firebase Custom(Email and password Authentication)
            auth.createUserWithEmailAndPassword(emailAddress, password1)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        //Wtrite in database
                        val caretakerID = user?.uid.toString()

                        //pass data to model
                        val caretaker = HomeCarectuserModel(userName,contactNumber,nicNumber,dateofbirth,string_gender,"0","","",string_district,string_tos,password1,emailAddress)

                        dref.child("userCARETAKERCITY").child(string_district).child(caretakerID).setValue(caretakerID)

                        dref.child("userCARETAKERTOS").child(string_tos).child(caretakerID).setValue(caretakerID)

                        //pass data to firebase realtime database
                        dref.child("userCARETAKER").child(caretakerID).setValue(caretaker)
                            .addOnSuccessListener {
                                Toast.makeText(this,"Registered Successfully", Toast.LENGTH_LONG).show()
                                openlg()

                            }.addOnFailureListener{ err->
                                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "This Email has already Registered. Use defferent mail",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }
    private fun openlg(){
        val intent = Intent(this,HomeCarelg::class.java)
        startActivity(intent)
    }

}