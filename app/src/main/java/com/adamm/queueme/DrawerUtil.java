package com.adamm.queueme;

import android.app.Activity;
import android.graphics.Color;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

public class DrawerUtil {
    public static Drawer drawer;
    public static FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

    public static void getDrawer(final Activity activity, Toolbar toolbar, Drawer.OnDrawerItemClickListener drawerListener) {
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.ic_qme).withSelectionListEnabled(false).withProfileImagesVisible(false)
                .addProfiles(new ProfileDrawerItem().withName(currUser.getDisplayName()).withEmail(currUser.getProviderId().equals(PhoneAuthProvider.PROVIDER_ID) ? currUser.getPhoneNumber() : currUser.getEmail()))
                .build();
        PrimaryDrawerItem profile = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.profile).withIcon(new IconicsDrawable(activity)
                .icon(GoogleMaterial.Icon.gmd_person).color(Color.rgb(203, 84, 39)));
        PrimaryDrawerItem queues = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.my_queues).withIcon(new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_queue).color(Color.rgb(203, 84, 39)));
        // PrimaryDrawerItem favoriteStores = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.favorite_stores).withIcon(GoogleMaterial.Icon.gmd_favorite).withIconColor(Color.rgb(203, 84, 39));
        PrimaryDrawerItem favoriteStores = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.favorite_stores).withIcon(new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_favorite).color(Color.rgb(203, 84, 39)));
        PrimaryDrawerItem stores = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.stores).withIcon(new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_store).color(Color.rgb(203, 84, 39)));
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.logout).withSelectable(false).withIcon(new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_exit_to_app).color(Color.rgb(203, 84, 39)));


        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder().withAccountHeader(headerResult)
                .withActivity(activity)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withCloseOnClick(true)
                .withSelectedItem(4)
                .addDrawerItems(profile, new DividerDrawerItem(), queues, favoriteStores, stores, logout)
                .withOnDrawerItemClickListener(drawerListener)
                .build();
    }

    public static Drawer getDrawerRef() {
        return drawer;
    }
}
