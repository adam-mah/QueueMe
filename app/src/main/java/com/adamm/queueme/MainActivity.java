package com.adamm.queueme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.adamm.queueme.entities.User;
import com.adamm.queueme.fragments.FavoritesFragment;
import com.adamm.queueme.fragments.MyQueuesFragment;
import com.adamm.queueme.fragments.ProfileFragment;
import com.adamm.queueme.fragments.QueuesFragment;
import com.adamm.queueme.fragments.StoresFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


/**
KNOWN BUGS:
 Signing in and out into different users, introduces a query issues of previous user reference. MUST FIX
 */

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SignInActivity";
    public static FirebaseUser currUser;
    private Fragment profileFrag, queuesFrag, favoritesFrag, storesFrag;
    private Toolbar toolbar;

    public static Intent createIntent(@NonNull Context context, @Nullable IdpResponse response) {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        // toolbar.setTitleTextColor(getResources().);
        setSupportActionBar(toolbar);
        DrawerUtil.getDrawer(this, toolbar, drawerListener);

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(currUser.getUid());

        if (response != null)
            if (response.isNewUser())
                userRef.set(new User(currUser.getUid(), currUser.getDisplayName(), currUser.getPhoneNumber(), currUser.getEmail()));

        profileFrag = ProfileFragment.newInstance();
        queuesFrag = QueuesFragment.newInstance();
        favoritesFrag = FavoritesFragment.newInstance();
        storesFrag = StoresFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, storesFrag).commit();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

   /* @Override
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
    }*/

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
