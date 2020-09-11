package jhocker.de.geburtstag

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val myDatabase = this.openOrCreateDatabase("birthday", Context.MODE_PRIVATE, null)
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS dates (name VARCHAR, month INT(2), day INT(2), id INTEGER PRIMARY KEY)")
            //input
            //myDatabase.execSQL("INSERT INTO dates (name, day, month) VALUES ('Babba',17,09)");
            //myDatabase.execSQL("INSERT INTO dates (name, day, month) VALUES ('Alex',27,09)");
            //myDatabase.execSQL("INSERT INTO dates (name, day, month) VALUES ('Alisa',18,04)");
        } catch (e: Exception) {
            if (BuildConfig.DEBUG && e == null) {
                error("Assertion failed")
            }
            e.printStackTrace()
        }

        val datePicker = findViewById<DatePicker>(R.id.datePicker1)
        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            //val month = month + 1
            //val msg = "You Selected: $day/$month/$year"
            //Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun clickButton() {
        val actualMonth = SimpleDateFormat("MM").format(Calendar.getInstance().time)
        val actualDay = SimpleDateFormat("dd").format(Calendar.getInstance().time)
        try {
            val myDatabase = this.openOrCreateDatabase("birthday", Context.MODE_PRIVATE, null)
            val c = myDatabase.rawQuery("SELECT * FROM  dates WHERE month = $actualMonth", null)
            val nameIndex = c.getColumnIndex("name")
            val monthIndex = c.getColumnIndex("month")
            val dayIndex = c.getColumnIndex("day")
            if (c.moveToFirst()) {
                do {
                    val name = c.getString(nameIndex)
                    val month = c.getString(monthIndex)
                    val day = c.getString(dayIndex)
                    //String id = ("id", c.getString(idIndex));
                    Toast.makeText(this, name + " hat am " + day + "." + month +
                            ". Geburtstag." + actualDay + "." + actualMonth, Toast.LENGTH_LONG).show()
                } while (c.moveToNext())
            }
        } catch (e: Exception) {
            assert(e != null)
            e.printStackTrace()
        }
    }

    fun AddContent(view: View?) {
        val dateinput = findViewById<DatePicker>(R.id.datePicker1)
        val monthinput = dateinput.month +1
        val dayinput = dateinput.dayOfMonth
        val fieldnameinput = findViewById<View>(R.id.editText2) as EditText
        val nameinput = fieldnameinput.text.toString()
        try {
            val myDatabase = this.openOrCreateDatabase("birthday", Context.MODE_PRIVATE, null)
            myDatabase.execSQL("INSERT INTO dates (name, day, month) VALUES ('$nameinput', $dayinput, $monthinput)") //TODO add the values given before
            val msg = "$nameinput, $dayinput, $monthinput hinzugefügt"
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            assert(e != null)
            e.printStackTrace()
        }
    }
}