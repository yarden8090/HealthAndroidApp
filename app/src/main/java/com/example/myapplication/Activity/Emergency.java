package com.example.myapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ContactAdapter;
import com.example.myapplication.modal.Contact;

import java.util.ArrayList;
import java.util.List;

public class Emergency extends AppCompatActivity {

    private Button btnPolice, btnAmbulance, btnFire, btnSOS;
    private RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        btnPolice = findViewById(R.id.btnPolice);
        btnAmbulance = findViewById(R.id.btnAmbulance);
        btnFire = findViewById(R.id.btnFire);
        btnSOS = findViewById(R.id.btnSOS);

        // Initialize RecyclerView and Contact List
        rvContacts = findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(contactList);
        rvContacts.setAdapter(contactAdapter);

        // Load contacts into RecyclerView
        loadContacts();

        // Set up button click listeners
        btnPolice.setOnClickListener(view -> makeEmergencyCall("100"));
        btnAmbulance.setOnClickListener(view -> makeEmergencyCall("101"));
        btnFire.setOnClickListener(view -> makeEmergencyCall("102"));

        btnSOS.setOnClickListener(view -> {
            // Example action for SOS button
            Toast.makeText(Emergency.this, "SOS Alert Sent!", Toast.LENGTH_SHORT).show();
        });
    }

    private void makeEmergencyCall(String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    private void loadContacts() {
        // Example data
        contactList.add(new Contact("Poison Control", "04-7771900"));
        contactList.add(new Contact("Emergency Hospital", "03-5551234"));
        // Notify adapter about data changes" +
        contactAdapter.notifyDataSetChanged();


    }
}