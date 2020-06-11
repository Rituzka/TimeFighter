package com.example.timefighter

import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    internal var score = 0

    internal lateinit var tapMeButton: Button
    internal lateinit var gameScoreLbl: TextView
    internal lateinit var timeLeftLbl: TextView

    internal var gameStarted = false

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 60000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "on Create called. Store = $score")

        tapMeButton = findViewById(R.id.btnTapMe)
        gameScoreLbl = findViewById(R.id.lblYourScore)
        timeLeftLbl = findViewById(R.id.lblTimeLeft)

        tapMeButton.setOnClickListener { view ->
            val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnim)
            incrementScore()
        }
        if(savedInstanceState != null){
            savedInstanceState.getInt(SCORE_KEY)
            savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        }else
          resetGame()
    }

    private fun restoreGame() {
        gameScoreLbl.text = getString(R.string.YourScore, score)

        val restoreTime = timeLeftOnTimer / 1000
        timeLeftLbl.text = getString(R.string.timeLeft, restoreTime)
        countDownTimer = object: CountDownTimer(timeLeftOnTimer, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftLbl.text = getString(R.string.timeLeft, timeLeft)
                TODO("Not yet implemented")
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanteState: Saving score:  $score & Time left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    private fun incrementScore() {
        if(!gameStarted){
            startGame()
        }
        score+=1
        val newScore = getString(R.string.YourScore, score)
        gameScoreLbl.text = newScore

    }

    private fun resetGame() {
        score = 0

        gameScoreLbl.text = getString(R.string.YourScore, score)
        val initialTimeLeft = initialCountDown / 1000
       timeLeftLbl.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftLbl.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false
    }
    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }
    private fun endGame() {
        Toast.makeText(this, getString(R.string.GameOverMessage,score), Toast.LENGTH_LONG).show()
        resetGame()
    }
}