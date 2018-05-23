package com.example.android.popularmoviesstage2.classes;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.example.android.popularmoviesstage2.R;

public class MyNavigationDrawer {
    private MyNavigationDrawer(){}

    public static boolean onNavigationItemSelected(int id, DrawerLayout drawer){
        switch (id){
            case R.id.nav_results_language:
                break;

            case R.id.nav_image_quality:
                break;

            case R.id.nav_delete_favorites:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;}
}