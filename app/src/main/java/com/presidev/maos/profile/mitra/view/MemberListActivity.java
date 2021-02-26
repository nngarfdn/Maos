package com.presidev.maos.profile.mitra.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.presidev.maos.R;
import com.presidev.maos.membership.view.MemberCardViewModel;
import com.presidev.maos.profile.mitra.model.Mitra;

import static com.presidev.maos.utils.Constants.EXTRA_MITRA;

public class MemberListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.rv_member_list_ml);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MemberListAdapter adapter = new MemberListAdapter(this);
        recyclerView.setAdapter(adapter);

        MemberCardViewModel memberCardViewModel = new ViewModelProvider(this).get(MemberCardViewModel.class);
        memberCardViewModel.getMemberCardListLiveData().observe(this, adapter::setData);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MITRA)){
            Mitra mitra = intent.getParcelableExtra(EXTRA_MITRA);
            memberCardViewModel.queryByMitraId(mitra.getId());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}