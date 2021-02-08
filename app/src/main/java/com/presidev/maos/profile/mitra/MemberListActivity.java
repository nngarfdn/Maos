package com.presidev.maos.profile.mitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.presidev.maos.R;
import com.presidev.maos.subscribe.viewmodel.MemberCardViewModel;

public class MemberListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        RecyclerView recyclerView = findViewById(R.id.rv_member_list_ml);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MemberListAdapter adapter = new MemberListAdapter(this);
        recyclerView.setAdapter(adapter);

        MemberCardViewModel memberCardViewModel = new ViewModelProvider(this).get(MemberCardViewModel.class);
        memberCardViewModel.getMemberCardListLiveData().observe(this, adapter::setData);
        memberCardViewModel.queryByMitraId("rSAszlHMDjgarYuvZgbQmzZnrM52");
    }
}