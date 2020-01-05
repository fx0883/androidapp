package com.colorgarden.app6.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.colorgarden.app6.R;
import com.colorgarden.app6.adapter.AdapterCreation;


import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.colorgarden.app6.constant.Constant.getAllCreationList;


public class CreationActivity extends AppCompatActivity implements AdapterCreation.RecClickInterface {

    @BindView(R.id.rec_my_creation)
    RecyclerView rec_my_creation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    List < String > strings;
    AdapterCreation myCreationAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);

        ButterKnife.bind(this);
        init();

    }



    private void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.lbl_creation_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        rec_my_creation.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        strings = getAllCreationList();
        if (strings.isEmpty()) {
            Toast.makeText(this, R.string.msg_creation_notFound, Toast.LENGTH_SHORT).show();

        } else {
            strings.size();
            Collections.reverse(strings);
            myCreationAdapter = new AdapterCreation(getApplicationContext(), strings);
            rec_my_creation.setAdapter(myCreationAdapter);
            myCreationAdapter.setInterface(this);
            myCreationAdapter.notifyDataSetChanged();
        }


    }


    @Override
    public void ItemClick(View view, int pos) {
        Intent intent = new Intent(getApplicationContext(), ActivityFullscreenCreation.class);
        intent.putExtra("position", pos);
        startActivity(intent);
    }

    @Override
    public void ItemDeleteClick(int pos, String path) {
        showDeleteDialog(path);
    }


    @Override
    public void ItemEditClick(int pos, String path) {
        Intent intent = new Intent(getApplicationContext(), PaintActivity.class);
        intent.putExtra("imgPath", path);
        startActivity(intent);
    }

    // Delete Image Dialog
    public void showDeleteDialog(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreationActivity.this);
        builder.setTitle(getString(R.string.msg_creation_deletePhoto));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File file = new File(s);
//                file.delete();
                boolean deleted = file.delete();
                Log.e("delete_file", "deleted: " + deleted);
                Intent intent = new Intent(getApplicationContext(), CreationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
