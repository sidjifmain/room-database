package com.example.roomtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import com.example.roomtest.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var appDb : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)

        binding.addData.setOnClickListener{
                writeData()
        }

        binding.readData.setOnClickListener{
                readData()
        }

        binding.deleteAll.setOnClickListener{

            GlobalScope.launch {
                appDb.studentDao().deleteAll()

                runOnUiThread{
                    Toast.makeText(this@MainActivity , "All records are successfully deleted" , Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.update.setOnClickListener {
            updateData()
        }

    }

    private fun updateData() {
        val firstName = binding.inputFirstName.text.toString()
        val lastName = binding.inputLastName.text.toString()
        val rollNo = binding.inputRollNo.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {

            GlobalScope.launch(Dispatchers.IO){
                val updatedRows = appDb.studentDao().update(firstName , lastName , rollNo.toInt())
                runOnUiThread {
                    if (updatedRows !=0){
                        binding.inputFirstName.text.clear()
                        binding.inputLastName.text.clear()
                        binding.inputRollNo.text.clear()
                        Toast.makeText(this@MainActivity , "The data is successfully updated" , Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this@MainActivity , "Please enter correct data" , Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
        else{
            Toast.makeText(this , "Please enter the data" , Toast.LENGTH_LONG).show()
        }
    }




    private fun writeData(){

        val firstName = binding.inputFirstName.text.toString()
        val lastName = binding.inputLastName.text.toString()
        val rollNo = binding.inputRollNo.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()){
            val student = Student(null , firstName , lastName , rollNo.toInt())

            GlobalScope.launch(Dispatchers.IO){
                appDb.studentDao().insert(student)
            }
            binding.inputFirstName.text.clear()
            binding.inputLastName.text.clear()
            binding.inputRollNo.text.clear()
            Toast.makeText(this , "The data is successfully added" , Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this , "Please enter the data" , Toast.LENGTH_LONG).show()
        }
    }




    private fun readData(){

        val rollNo = binding.rollNo.text.toString()


        if (rollNo.isNotEmpty()){

                GlobalScope.launch {
                    val student = appDb.studentDao().findByRoll(rollNo.toInt())

                    if ( student != null){
                    binding.displayData.text = "${student.firstName}   ${student.lastName}"
                    }
                    else{
                        runOnUiThread{
                            Toast.makeText(this@MainActivity, "This student does not in the table", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
        else{
            Toast.makeText(this , "Please enter the RollNo" , Toast.LENGTH_LONG).show()
        }

    }

}