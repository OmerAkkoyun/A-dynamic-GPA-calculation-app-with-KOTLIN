package com.example.ortalamahesaplama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        //Animasyon dosyaları
        var asagidanYukari = AnimationUtils.loadAnimation(this, R.anim.asagidan_yukari)
        var bulananYerdenYukari = AnimationUtils.loadAnimation(this,R.anim.bulunanyerden_yukari)
        var yukaridanAsagi = AnimationUtils.loadAnimation(this,R.anim.yukaridan_asagi)
        var bulunanYerdenAsagi = AnimationUtils.loadAnimation(this ,R.anim.bulunanyerden_asagi)

        buttonAGNO.animation=asagidanYukari
        imageView.animation=yukaridanAsagi


        buttonAGNO.setOnClickListener {

            buttonAGNO.startAnimation(bulunanYerdenAsagi)
           imageView.startAnimation(bulananYerdenYukari)

            //Animasyon bitmeden 1sn sonra MainActivity açılsın
            object : CountDownTimer(1000,1000){
                override fun onFinish() {
                    var intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onTick(p0: Long) {
                    //işimiz yok burasıyla
                }

            }.start()



        }



    }
}
