package com.aa.torch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private CameraManager manager;
    private String cameraID;
    boolean isLightOn;
    ImageView trigger;
    LinearLayout root;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isLightOn) {
                root.setBackgroundResource(R.drawable.torch_on_background);
                trigger.setImageDrawable(getResources().getDrawable(R.drawable.turnofftrigger));
            } else {
                root.setBackgroundResource(R.drawable.torch_off_background);
                trigger.setImageDrawable(getResources().getDrawable(R.drawable.trigger));

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoTrigger:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                infoDialogFragment dialogFragment = new infoDialogFragment();
                dialogFragment.show(ft, "tag");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trigger = (ImageView) findViewById(R.id.trigger);
        isLightOn = false;
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        root = (LinearLayout) findViewById(R.id.root);
        try {
            cameraID = manager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isLightOn) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            manager.setTorchMode(cameraID, false);
                            isLightOn = false;
                        }
                        isLightOn = false;
                        runOnUiThread(runnable);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            manager.setTorchMode(cameraID, true);
                        }
                        isLightOn = true;
                        runOnUiThread(runnable);

                    }
                } catch (CameraAccessException exc) {
                    exc.printStackTrace();
                }
            }
        });

    }
}