package dora.widget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val progressBarOne = findViewById<DoraSportyProgressBar>(R.id.progressBarOne)
        val progressBarTwo = findViewById<DoraSportyProgressBar>(R.id.progressBarTwo)
        val progressBarThree = findViewById<DoraSportyProgressBar>(R.id.progressBarThree)
        val progressBarFour = findViewById<DoraSportyProgressBar>(R.id.progressBarFour)
        val progressBarFive = findViewById<DoraSportyProgressBar>(R.id.progressBarFive)
        progressBarOne.setPercentRate(0.2f)
        progressBarTwo.setPercentRate(0.4f)
        progressBarThree.setPercentRate(0.6f)
        progressBarFour.setPercentRate(0.8f)
        progressBarFive.setPercentRate(1f)
    }
}