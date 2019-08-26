package com.example.ortalamahesaplama

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.yeni_ders_layout.view.*
import kotlinx.android.synthetic.main.yeni_ders_layout.view.TextViewAuto
import kotlinx.android.synthetic.main.yeni_ders_layout.view.harfNotlarSpinner
import kotlinx.android.synthetic.main.yeni_ders_layout.view.kredilerSpinner
import android.widget.TextView
import com.shashank.sony.fancytoastlib.FancyToast


class MainActivity : AppCompatActivity() {

    //Otomatik gösterilecek ders isimleri
    private val DERSLER = arrayOf(
        "Matematik",
        "Türkçe",
        "ingilizce",
        "Tarih",
        "Kimya",
        "Fizik",
        "İstatistik",
        "Edebiyat",
        "Türk Dili",
        "Algoritma"
    )

    //DerslerClası şeklinde değerler alacak bu liste
    private var tumDersBilgisiArray:ArrayList<Dersler> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //İlk açılışta hesapla butonu gözükmesin
        btnHesapla.visibility = View.INVISIBLE

        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, DERSLER)
        autoCompleteTextView.setAdapter(adapter)


        //EKLE butonunda basılınca neler olacak
        buttonDersEkle.setOnClickListener {


            if (autoCompleteTextView.text.isNullOrEmpty()) {
                //eğer ders ismi girilmemiş ise uyarı verelim.
                val toast =
                    Toast.makeText(applicationContext, "Ders Adını Girin", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 180)
                toast.show()

            } else {


                klavyeGizle()

                //xml to java kodu için gerekli
                val inflater = LayoutInflater.from(this)

                //xml to java işlemi gerçekleşti
                var yenidersView = inflater.inflate(R.layout.yeni_ders_layout, null)

                //23.satırdaki adapteri bunada ekleyelim
                yenidersView.TextViewAuto.setAdapter(adapter)

                //Kullanıcının girdiği değerleri alalım sonra kullanıcaz.
                var dersAdi = autoCompleteTextView.text.toString()
                var dersharfNotu = spinnerHarfNotlar.selectedItemPosition
                var derskrediNotu = spinnerKrediler.selectedItemPosition


                //Girilen değerleri Layout eklenmden önce layouta aktarıp öyle gösterelim.
                yenidersView.TextViewAuto.setText(dersAdi)
                yenidersView.harfNotlarSpinner.setSelection(dersharfNotu)
                yenidersView.kredilerSpinner.setSelection(derskrediNotu)

                //ders eklendikten sonra degerler sıfırlansın Fonksiyon çalışsın.
                degerSifirla()


                //Sil Butonu için Kodlar
                yenidersView.buttonSil.setOnClickListener {
                    rootLayout.removeView(yenidersView)
                    if (rootLayout.childCount == 0) {
                        btnHesapla.visibility = View.INVISIBLE
                    } else {
                        btnHesapla.visibility = View.VISIBLE
                    }


                }



                rootLayout.addView(yenidersView)
                btnHesapla.visibility = View.VISIBLE


            }


        }


    }



    //Girilen Değerleri sıfırla
    fun degerSifirla() {
        autoCompleteTextView.setText("")
        spinnerKrediler.setSelection(0)
        spinnerHarfNotlar.setSelection(0)
    }


    //Klavye Kapatma Fonksiyonu
    fun klavyeGizle() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }




    //ORTALAMA HESAPLAMA -----------------------------------------
    fun ortalamaHesapla(view: View) {

        var toplamNot=0.0
        var toplamKredi =0.0

        for (i in 0..rootLayout.childCount -1){

            var tekSatir = rootLayout.getChildAt(i)

            var gelenDersAdi= tekSatir.TextViewAuto.text.toString()
            var gelenKredi = ((tekSatir.kredilerSpinner.selectedItemPosition)+1).toString()
            var gelenHarfNotu=tekSatir.harfNotlarSpinner.selectedItem.toString()

            var geciciDers=Dersler(gelenDersAdi,gelenKredi,gelenHarfNotu)


            //en yukarıda tanımladığımız Arraya bu listeyi atabiliriz.
            tumDersBilgisiArray.add(geciciDers)
        }


        //Girilen derslerin Kredileri ve Notlarını hesaplayalım
        for(oankiDers in tumDersBilgisiArray){

            toplamNot+= harfiNotaCevir(oankiDers.dersHarfNot) * (oankiDers.dersKredi.toDouble())
            toplamKredi+= oankiDers.dersKredi.toDouble()
            var harf= oankiDers.dersHarfNot

            println("\n\n toplam puan : "+toplamNot )
        }


        //Hesaplama Bitince bir TOAST MESAJI ile sonucu gösterelim.

        var sonuc = (toplamNot/toplamKredi).formatla().toString()

        FancyToast.makeText(this,"Ortalama Paun : "+sonuc,FancyToast.LENGTH_LONG,FancyToast.INFO,true).show()


        //DersListesi Sıfırlansın Hesaplamadan sonra
        tumDersBilgisiArray.clear()
    }


    //------------------------------------------------------------

    //HarfNotunu Sayıya Çevirme >double bir değer dönecek
    fun harfiNotaCevir(gelenharfDegeri:String):Double {
        var deger =0.0

        when(gelenharfDegeri){
            "AA" -> deger= 4.0
            "BA" -> deger= 3.5
            "BB" -> deger= 3.0
            "CB" -> deger= 2.5
            "CC" -> deger =2.0
            "DC" -> deger =1.5
            "DD" -> deger =1.0
            "FF" -> deger=0.0
        }

        return deger
    }

    //Double sayısını virgülden sonra sadece 2 basamak al
    fun Double.formatla()= java.lang.String.format("%.2f",this)



}
