package com.demotxt.droidsrce.welcomescreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Contact Details");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Lyrics + Melody + Vocal = Song / Jeewantha96")
                .setImage(R.drawable.presentation1)
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("kasunkodithuwakku96@gmail.com")
                .addWebsite("https://slnews96.blogspot.com/")
                .addFacebook("kasun.kodithuwakku.581")
                .addInstagram("jeewantha_t")
                .addGitHub("kasunjeewantha")
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrightString = String.format("Copyright %d by Jeewantha96", Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrightString);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setIcon(R.drawable.about_icon_copy_right);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this, copyrightString, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
}
