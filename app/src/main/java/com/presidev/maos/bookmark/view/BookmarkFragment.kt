package com.presidev.maos.bookmark.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.presidev.maos.R
import com.presidev.maos.bookmark.model.Bookmark
import com.presidev.maos.catatanku.CatatanKuActivity
import com.presidev.maos.mitramanagement.model.Book
import kotlinx.android.synthetic.main.layout_bookmark.view.*
import java.util.*

class BookmarkFragment : Fragment(), BookmarkCallback {
    private lateinit var recyclerView: RecyclerView
    private var favoriteViewModel: BookmarkViewModel? = null
    private var firebaseUser: FirebaseUser? = null
    private var adapter: BookmarkAdapter? = null
    private var database: FirebaseFirestore? = null
    private var imgBookmark: ImageView? = null
    private var txtEmptyBookmark: TextView? = null
    private var listBookId: List<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        favoriteViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)
        database = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_bookmark)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
        adapter = BookmarkAdapter(requireActivity())
        adapter!!.notifyDataSetChanged()
        recyclerView.adapter = adapter
        imgBookmark = view.findViewById(R.id.img_bookmark)
        txtEmptyBookmark = view.findViewById(R.id.txt_bookmark_empty)

        favoriteViewModel?.data?.observe(viewLifecycleOwner, { favorite: Bookmark ->
            // Kalo daftar favorit masih sama, jangan dimuat lagi
            if (listBookId != favorite.listBookId) {
                listBookId = favorite.listBookId
                // Muat semua produk menurut id yang difavoritkan
                loadBookById()
            }
        })

        view.sv_mitra_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter!!.filter.filter(newText)
                return false
            }
        })

        view.btn_catatanku.setOnClickListener {
            val intent = Intent(context, CatatanKuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadBookById() {
        val listItem = ArrayList<Book>()
        if (listBookId.isEmpty()) onFinish(listItem)
        else {
            val iterator = listBookId.iterator()
            while (iterator.hasNext()) {
                database!!.collection("book").document(iterator.next())
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val product = task.result!!.toObject(Book::class.java)
                            if (product != null) listItem.add(product)
                            if (!iterator.hasNext()) onFinish(listItem) // Pemuatan id terakhir
                        }
                    }
            }
        }
    }

    override fun onFinish(listItem: ArrayList<Book>) {
        adapter!!.data = listItem
        if (adapter!!.itemCount > 0) {
            txtEmptyBookmark!!.visibility = View.INVISIBLE
            imgBookmark!!.visibility = View.INVISIBLE
        } else {
            txtEmptyBookmark!!.visibility = View.VISIBLE
            imgBookmark!!.visibility = View.VISIBLE
        }
        Log.d(TAG, "onFinish: $listItem")
    }

    override fun onStart() {
        super.onStart()
        favoriteViewModel?.loadData(firebaseUser?.uid ?: "-1")
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel?.loadData(firebaseUser?.uid ?: "-1")
    }

    companion object {
        private const val TAG = "BookmarkFragment"
    }
}