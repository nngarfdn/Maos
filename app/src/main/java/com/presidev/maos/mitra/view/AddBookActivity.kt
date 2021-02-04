package com.presidev.maos.mitra.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.presidev.maos.R
import com.presidev.maos.mitra.model.Book
import com.presidev.maos.mitra.view.viewmodel.BookViewModel

class AddBookActivity : AppCompatActivity() {

    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book_activity)

        bookViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookViewModel::class.java)

        val book = Book("bookid1","mitraId1", "Laskar Pelangi",true,"https://upload.wikimedia.org/wikipedia/id/1/17/Laskar_Pelangi_film.jpg",
        "Laskar Pelangi (2008) adalah sebuah film garapan sutradara Riri Riza yang dirilis pada 26 September 2008. Film Laskar Pelangi merupakan karya adaptasi dari buku Laskar Pelangi yang ditulis oleh Andrea Hirata. Skenarionya ditulis oleh Salman Aristo yang juga menulis naskah film Ayat-Ayat Cinta dibantu oleh Riri Riza dan Mira Lesmana. Hingga Maret 2009, Laskar Pelangi telah ditonton oleh 4,6 juta orang[1], menjadikannya film terbanyak ditonton di Indonesia keempat, setelah Jelangkung dengan 5,7 Juta, Pocong 2 dengan 5,1 Juta, dan Ada Apa Dengan Cinta dengan 4,9 Juta",
        "Sang Pemimpi merupakan film kedua yang diadaptasi dari novel karya Andrea Hirata. Mira sendiri tak berani memasang target bahwa film ini harus melampaui prestasi yang telah diraih film Laskar Pelangi, yang telah ditonton oleh 4,6 juta orang. \"Kita enggak berani memasang target, karena penonton kita memang sulit ditebak. Tapi, tetap kita akan mencoba berbuat yang terbaik,\" ujarnya. (EH)\n" +
                "\n" +
                "Untuk mencari pemeran tokoh-tokoh anggota Laskar Pelangi, Riri Riza melakukan casting di daerah Belitung dengan menggunakan pemeran-pemeran lokal dalam pembuatan film. Film ini juga diambil di lokasi yang sama, Pulau Belitung. Film ini memadukan 12 aktor Indonesia yang dikenal dengan kemampuan akting mereka dengan 12 anak-anak Belitung asli yang bertalenta akting.")

        bookViewModel.insert(book)
    }
}