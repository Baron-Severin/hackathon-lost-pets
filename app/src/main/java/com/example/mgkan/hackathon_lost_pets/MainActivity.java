package com.example.mgkan.hackathon_lost_pets;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mgkan.hackathon_lost_pets.Model.Pet;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {

  List<Pet> pets;
  private Context mContext;
  ButtonAnimationHandler animationHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mContext = this;

    verifyStoragePermissions(this);

    ImageView ivKitty = (ImageView) findViewById(R.id.kitty);
    Ion.with(ivKitty)
      .placeholder(R.drawable.kitty)
      .error(R.drawable.kitty)
      .load("faulty url")
      .withBitmapInfo();


    final Button lostAndFound = (Button) findViewById(R.id.lostAndFoundButton);
    final Button petProfile = (Button) findViewById(R.id.petProfileButton);
    final Button petInfo = (Button) findViewById(R.id.petInfoButton);
    final LinearLayout buttonHolder = (LinearLayout) findViewById(R.id.buttonHolder);

    List<Button> buttons = new ArrayList<Button>();
    buttons.add(lostAndFound);
    buttons.add(petProfile);
    buttons.add(petInfo);
    animationHandler = new ButtonAnimationHandler(buttons, getBaseContext());

    lostAndFound.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final Intent i = new Intent(MainActivity.this, SecondScreenActivity.class);
        startAnimation(i);
//        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
//
//        ButtonAnimationHandler animationHandler = animationHandler;
//        animationHandler.setIntent(i);
//
//        animationHandler.execute();
//        overridePendingTransition(0, 0);
//        pushRight.setAnimationListener(new Animation.AnimationListener() {
//          @Override
//          public void onAnimationStart(Animation animation) {}
//
//          @Override
//          public void onAnimationEnd(Animation animation) {
//            startActivity(i);
//          }
//
//          @Override
//          public void onAnimationRepeat(Animation animation) {}
//        });
//
////        petProfile.startAnimation(pushRight);
////        petInfo.startAnimation(pushRight);
////        lostAndFound.startAnimation(pushRight);
//        buttonHolder.startAnimation(pushRight);
////        startActivity(i);
//        overridePendingTransition(0, 0);
      }


    });

    petInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(MainActivity.this, PetInfoActivity.class);
        startAnimation(i);

//        startActivity(i);
//        overridePendingTransition(0, 0);
//        Animation pushRight = AnimationUtils.loadAnimation(mContext, R.anim.push_out_right);
//
////        petProfile.startAnimation(pushRight);
////        petInfo.startAnimation(pushRight);
////        lostAndFound.startAnimation(pushRight);
//        buttonHolder.startAnimation(pushRight);
      }
    });

    petProfile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(MainActivity.this, PetProfileActivity.class);
        startAnimation(i);
//        startActivity(i);
//        overridePendingTransition(0, 0);
//        Animation pushRight = AnimationUtils.loadAnimation(mContext, R.anim.push_out_right);
//
////        petProfile.startAnimation(pushRight);
////        petInfo.startAnimation(pushRight);
////        lostAndFound.startAnimation(pushRight);
//        buttonHolder.startAnimation(pushRight);
      }
    });

  }

  private void startAnimation(Intent i) {
    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
    Animation pushRight = AnimationUtils.loadAnimation(mContext, R.anim.push_out_right);
    animationHandler.setAnimation(pushRight);

    animationHandler.setIntent(i);

    animationHandler.execute();
    overridePendingTransition(0, 0);
  }

  @Override
  public void onResume() {
    super.onResume();
    final Button lostAndFound = (Button) findViewById(R.id.lostAndFoundButton);
    final Button petProfile = (Button) findViewById(R.id.petProfileButton);
    final Button petInfo = (Button) findViewById(R.id.petInfoButton);
    final LinearLayout buttonHolder = (LinearLayout) findViewById(R.id.buttonHolder);

    Animation pullRight = AnimationUtils.loadAnimation(mContext, R.anim.pull_right);
    animationHandler.setAnimation(pullRight);

    if (animationHandler.hasButtons()) {
      animationHandler.execute();
    }

//    petProfile.startAnimation(pullRight);
//    petInfo.startAnimation(pullRight);
//    lostAndFound.startAnimation(pullRight);
//    buttonHolder.startAnimation(pullRight);
  }


  private static final int REQUEST_PERMISSIONS = 1;
  private static String[] PERMISSIONS_INTERNET = {
          Manifest.permission.INTERNET,
  };

  public static void verifyStoragePermissions(Activity activity) {
    // Check if we have write permission
    int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);

    if (permission != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(
              activity,
              PERMISSIONS_INTERNET,
              REQUEST_PERMISSIONS
      );
    }
  }
}
