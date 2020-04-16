package com.adamm.queueme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.adamm.queueme.entities.Store;
import com.adamm.queueme.entities.User;
import com.adamm.queueme.fragments.FavoritesFragment;
import com.adamm.queueme.fragments.MyQueuesFragment;
import com.adamm.queueme.fragments.ProfileFragment;
import com.adamm.queueme.fragments.StoresFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;


/**
 * KNOWN BUGS:
 * Signing in and out into different users, introduces a query issues of previous user reference. MUST FIX
 * <p>
 * TODO:
 * Disable past times in a new queue
 * Cancelling a queue - Done
 * Favorites MUST store, store IDs only and get data from stores collection
 * Add open/closed store states;
 */

public class MainActivity extends AppCompatActivity {
    public static Context appContext;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignInActivity";
    public static FirebaseUser currUser;
    private Fragment profileFrag, queuesFrag, favoritesFrag, storesFrag;
    private Toolbar toolbar;
    ArrayList<String> storesIdList;
    ArrayList<Store> storesList;

    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = MainActivity.this;
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        // toolbar.setTitleTextColor(getResources().);
        setSupportActionBar(toolbar);
        DrawerUtil.getDrawer(this, toolbar, drawerListener);

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(currUser.getUid());

        if (response != null) {
            if (response.isNewUser())
                userRef.set(new User(currUser.getUid(), currUser.getDisplayName(), currUser.getPhoneNumber(), currUser.getEmail()));
        }

        /**Initialize owners drawer view*/
        CollectionReference storeCollection = FirebaseFirestore.getInstance().collection("stores");
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    storesIdList = (ArrayList<String>) task.getResult().get("storesList");
                    if (storesIdList != null) {
                        storesList = new ArrayList<>();
                        storeCollection.whereIn(FieldPath.documentId(), storesIdList).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        storesList.add(document.toObject(Store.class));
                                        Log.d("storeList", document.getData().toString());
                                    }
                                    for (Store store : storesList) {
                                        PrimaryDrawerItem storeItem = new PrimaryDrawerItem().withName(store.getStoreName()).withIcon(new IconicsDrawable(MainActivity.appContext).icon(GoogleMaterial.Icon.gmd_store).color(Color.rgb(203, 84, 39)));
                                        DrawerUtil.getDrawerRef().addItemAtPosition(storeItem, 7);
                                    }
                                } else {
                                    Log.d("storeList", task.getResult().toString());
                                }
                            }
                        });
                        DrawerUtil.getDrawerRef().addItemAtPosition(new DividerDrawerItem(), 6);
                        DrawerUtil.getDrawerRef().addItemAtPosition(new DividerDrawerItem(), storesList.size() + 6);
                    }
                }
            }
        });


        profileFrag = ProfileFragment.newInstance();
        queuesFrag = MyQueuesFragment.newInstance();
        favoritesFrag = FavoritesFragment.newInstance();
        storesFrag = StoresFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, storesFrag).commit();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private Drawer.OnDrawerItemClickListener drawerListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            Fragment fragment = null;
            switch ((int) drawerItem.getIdentifier()) {
                case 1:
                    toolbar.setTitle(R.string.profile);
                    fragment = profileFrag;
                    DrawerUtil.drawer.closeDrawer();
                    break;
                case 2:
                    toolbar.setTitle(R.string.my_queues);
                    fragment = queuesFrag;
                    DrawerUtil.drawer.closeDrawer();
                    break;
                case 3:
                    toolbar.setTitle(R.string.favorite_stores);
                    fragment = favoritesFrag;
                    DrawerUtil.drawer.closeDrawer();
                    break;
                case 4:
                    toolbar.setTitle(R.string.stores);
                    fragment = storesFrag;
                    DrawerUtil.drawer.closeDrawer();
                    break;
                case 5:
                    AlertDialog.Builder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Logout")
                            .setMessage("Are you sure you want to logout?")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AuthUI.getInstance()
                                            .signOut(getApplicationContext())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    // user is now signed out
                                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                                    finish();
                                                }
                                            });
                                }
                            });
                    builder.show();
                    break;
            }
            try {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    };
}
