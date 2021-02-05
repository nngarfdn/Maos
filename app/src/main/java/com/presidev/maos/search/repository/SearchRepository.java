package com.presidev.maos.search.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.presidev.maos.mitramanagement.model.Book;
import com.presidev.maos.search.model.BookFilter;
import com.presidev.maos.model.Mitra;
import com.presidev.maos.search.model.MitraFilter;

import java.util.ArrayList;

public class SearchRepository {
    private final String TAG = getClass().getSimpleName();

    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private final MutableLiveData<ArrayList<Book>> bookLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Mitra>> mitraLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Book>> getBookLiveData() {
        return bookLiveData;
    }

    public MutableLiveData<ArrayList<Mitra>> getMitraLiveData(){
        return mitraLiveData;
    }

    public void query(MitraFilter filter) {
        Query query = database.collection("mitra");

        // Filter lokasi
        if (filter.isByProvince()) query = query.whereEqualTo("province", filter.getProvince());
        if (filter.isByRegency()) query = query.whereEqualTo("regency", filter.getRegency());
        if (filter.isByDistrict()) query = query.whereEqualTo("district", filter.getDistrict());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Mitra> listMitra = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();

                for (DocumentSnapshot snapshot : querySnapshot) {
                    Mitra mitra = snapshot.toObject(Mitra.class);
                    if (mitra.getName().toLowerCase().contains(filter.getKeyword().toLowerCase()) ||
                            mitra.getDescription().toLowerCase().contains(filter.getKeyword().toLowerCase())){
                        if (filter.isOnlyCOD()) if (!mitra.isCOD()) continue;
                        if (filter.isOnlyKirimLuarKota()) if (!mitra.isKirimLuarKota()) continue;
                        listMitra.add(mitra);
                    }
                    Log.d(TAG, "mitra.getName(): " + mitra.getName());
                }

                mitraLiveData.postValue(listMitra);
                Log.d(TAG, "Document was queried");
            } else Log.w(TAG, "Error querying document", task.getException());
        });
    }

    public void query(BookFilter filter) {
        Query query = database.collection("book")
                .whereEqualTo("mitraId", filter.getMitraId());

        if (filter.isOnlyAvailable()) query = query.whereEqualTo("ketersediaan", true);

        query.orderBy("ketersediaan").orderBy("title").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Book> listBook = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();

                for (DocumentSnapshot snapshot : querySnapshot) {
                    Book book = snapshot.toObject(Book.class);
                    if (book.getTitle().toLowerCase().contains(filter.getKeyword().toLowerCase()) ||
                            book.getDescription().toLowerCase().contains(filter.getKeyword().toLowerCase())){
                        listBook.add(book);
                    }
                    Log.d(TAG, "book.getTitle(): " + book.getTitle());
                }

                bookLiveData.postValue(listBook);
                Log.d(TAG, "Document was queried");
            } else Log.w(TAG, "Error querying document", task.getException());
        });
    }
}
