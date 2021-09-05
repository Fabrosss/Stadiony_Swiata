package com.example.satdiony_wiata

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.satdiony_wiata.models.Place
import com.example.satdiony_wiata.models.Stadiums

const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
const val REQUEST_CODE = 1234
class MainActivity : AppCompatActivity() {
    private lateinit var stadiums: Stadiums
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ModelPreferencesManager.with(this)
        val stadium = ModelPreferencesManager.get<Stadiums>("KEY_STADIUMS")
        if( stadium == null){
            stadiums = generateSampleData()
        }else stadiums = stadium
        setContentView(R.layout.activity_main)
        ModelPreferencesManager.put(stadiums, "KEY_STADIUMS")


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val UserMap = data?.getSerializableExtra(EXTRA_USER_MAP) as Stadiums
            stadiums = UserMap

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun generateSampleData(): Stadiums {
        return Stadiums(
                "Stadiony Swiata",
                listOf(
                    Place("SP5", "Zajęte", 52.39725475542571, 17.061742106399546),
                    Place("Boisko - Raczyńskiego", "Wolne", 52.39983300290023, 17.059500541835085),
                    Place("Boisko Unia", "Wolne", 52.41196352189312, 17.068579725267572),
                    Place("Santiago Łowecin ", "Wolne" , 52.412814, 17.116830 ),
                    Place("Szkoła Podstawowa nr 2 w Zalesewie","Wolne" , 52.384532177370836, 17.066423922410788)
                )
            )

    }

    fun nextClick(view: android.view.View) {
            val intent = Intent(this@MainActivity, DisplayMap::class.java)
            intent.putExtra(EXTRA_USER_MAP, stadiums)
            startActivity(intent)



    }
}