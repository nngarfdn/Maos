package com.presidev.maos.bookmark.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.presidev.maos.R;
import com.presidev.maos.bookmark.model.Bookmark;
import com.presidev.maos.borrowbook.PeminjamanActivity;
import com.presidev.maos.login.view.LoginActivity;
import com.presidev.maos.mitrabookcatalog.view.MitraBookCatalogAdapter;
import com.presidev.maos.mitramanagement.model.Book;
import com.presidev.maos.mitramanagement.view.BookAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.presidev.maos.bookdetail.BookDetailActivity.EXTRA_BOOK;

public class BookmarkFragment extends Fragment implements BookmarkCallback {

    RecyclerView recyclerView;
    private static final String TAG = "BookmarkFragment";
    private BookmarkViewModel favoriteViewModel;
    private FirebaseUser firebaseUser;
    private BookmarkAdapter adapter;
    private FirebaseFirestore database;
    private List<String> listBookId = new ArrayList<>();

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        favoriteViewModel = ViewModelProviders.of(this).get(BookmarkViewModel.class);
        database = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_bookmark);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        adapter = new BookmarkAdapter(getActivity());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        favoriteViewModel.getData().observe(getViewLifecycleOwner(), favorite -> {
            // Kalo daftar favorit masih sama, jangan dimuat lagi
            if (!listBookId.equals(favorite.getListBookId())){
                listBookId = favorite.getListBookId();
                // Muat semua produk menurut id yang difavoritkan
                loadBookById();
            }
        });
    }

    private void loadBookById() {
        ArrayList<Book> listItem = new ArrayList<>();
        Iterator<String> iterator = listBookId.iterator();
        while (iterator.hasNext()){
            database.collection("book").document(iterator.next())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                Book product = task.getResult().toObject(Book.class);
                                if (product != null) listItem.add(product);
                                if (!iterator.hasNext()) onFinish(listItem); // Pemuatan id terakhir
                            }
                        }
                    });
        }
    }

    @Override
    public void onFinish(ArrayList<Book> listItem) {
        adapter.setData(listItem);
        Log.d(TAG, "onFinish: " + listItem);
    }

    @Override
    public void onStart() {
        super.onStart();
        favoriteViewModel.loadData(firebaseUser.getUid());
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteViewModel.loadData(firebaseUser.getUid());
    }


}

interface BookmarkCallback{
    void onFinish(ArrayList<Book> listItem);
}