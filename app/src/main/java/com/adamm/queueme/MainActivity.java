package com.adamm.queueme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.adamm.queueme.entities.User;
import com.adamm.queueme.fragments.FavoritesFragment;
import com.adamm.queueme.fragments.ProfileFragment;
import com.adamm.queueme.fragments.StoresFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignInActivity";
    private FirebaseUser currUser;
    private Fragment profileFrag, myQueuesFrag, favoritesFrag, storesFrag;

    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        initializeDrawer(toolbar);

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(currUser.getUid());
       // if (response.isNewUser())
            userRef.set(new User(currUser.getUid(), currUser.getDisplayName(), currUser.getPhoneNumber(), currUser.getEmail()));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        profileFrag = ProfileFragment.newInstance();
        myQueuesFrag= null;
        favoritesFrag = FavoritesFragment.newInstance();
        storesFrag = StoresFragment.newInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            finish();
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initializeDrawer(Toolbar toolbar) {
        // Create the AccountHeader
        // S profile = currUser.getProviderId();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_qme).withSelectionListEnabled(false).withProfileImagesVisible(false)
                .addProfiles(new ProfileDrawerItem().withName(currUser.getDisplayName()).withEmail(currUser.getProviderId().equals(PhoneAuthProvider.PROVIDER_ID) ? currUser.getPhoneNumber() : currUser.getEmail()))
                .build();

        PrimaryDrawerItem profile = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.profile).withIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_person).color(Color.rgb(203, 84, 39)));
        PrimaryDrawerItem queues = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.my_queues).withIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_queue).color(Color.rgb(203, 84, 39)));
        PrimaryDrawerItem favoriteStores = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.favorite_stores).withIcon(GoogleMaterial.Icon.gmd_favorite).withIconColor(Color.rgb(203, 84, 39));
        PrimaryDrawerItem stores = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.stores).withIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_store).color(Color.rgb(203, 84, 39)));
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.logout).withIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_exit_to_app).color(Color.rgb(203, 84, 39)));


        //create the drawer and remember the `Drawer` result object
        new DrawerBuilder().withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(profile, new DividerDrawerItem(), queues, favoriteStores, stores, logout).withCloseOnClick(true)
                .withOnDrawerItemClickListener(drawerListener).withSelectedItem(2)
                .build();
    }

    private Drawer.OnDrawerItemClickListener drawerListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            Fragment fragment = null;
            Class fragmentClass = null;
            switch ((int) drawerItem.getIdentifier()) {
                case 1:
                    fragmentClass = ProfileFragment.class;
                    fragment = profileFrag;
                    break;
                case 2:

                    break;
                case 3:
                    fragmentClass = FavoritesFragment.class;
                    fragment = favoritesFrag;
                    break;
                case 4:
                    fragmentClass = StoresFragment.class;
                    fragment = storesFrag;
                    break;
                case 5:
                               /* AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getApplicationContext())
                                        .setTitle("Title")
                                        .setMessage("Message")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {*/
                    AuthUI.getInstance()
                            .signOut(getApplicationContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                    finish();
                                }
                            });
                                           /* }
                                        });
                                builder.show();*/
                    break;
            }
            try {
               // fragment = (Fragment) fragmentClass.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
            return true;
        }
    };
}
