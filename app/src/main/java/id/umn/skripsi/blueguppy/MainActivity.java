package id.umn.skripsi.blueguppy;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.isEnabled()){
//            adapter.disable();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Bluetooth");
            builder.setMessage("Enable Bluetooth?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    adapter.enable();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

        Button startAppBtn = (Button) findViewById(R.id.button_startApp);
        startAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Peripheral.class);
                intent.putExtra("MARKED", false);
                startActivity(intent);
            }
        });

        Button startAppBtnMark = (Button) findViewById(R.id.button_startAppMark);
        startAppBtnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Peripheral.class);
                intent.putExtra("MARKED", true);
                startActivity(intent);
            }
        });

        Button changeUsernameBtn = (Button) findViewById(R.id.button_usernameSettings);
        changeUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UsernameConfig.class));
            }
        });

    }
}
