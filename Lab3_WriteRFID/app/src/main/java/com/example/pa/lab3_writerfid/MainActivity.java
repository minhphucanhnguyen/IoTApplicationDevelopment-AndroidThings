package com.example.pa.lab3_writerfid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.galarzaa.androidthings.Rc522;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.SpiDevice;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Rc522 mRc522;
    RfidTask mRfidTask;
    private TextView mTagDetectedView;
    private TextView mTagUidView;
    private TextView mTagResultsView;
    private Button button;
    private EditText mEdit1;
    private EditText mEdit2;
    private EditText mEdit3;
    private String userName;
    private String userDOB;
    private String userID;

    private SpiDevice spiDevice;
    private Gpio gpioReset;

    private static final String SPI_PORT = "SPI0.0";
    private static final String PIN_RESET = "BCM25";

    String resultsText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagDetectedView = (TextView)findViewById(R.id.tag_read);
        mTagUidView = (TextView)findViewById(R.id.tag_uid);
        mTagResultsView = (TextView) findViewById(R.id.tag_results);
        mEdit1 = (EditText)findViewById(R.id.editName);
        mEdit2 = (EditText)findViewById(R.id.editDOB);
        mEdit3 = (EditText)findViewById(R.id.editID);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mEdit1.getText().toString();
                userDOB = mEdit2.getText().toString();
                userDOB = userDOB.replaceAll("/", "");
                userID = mEdit3.getText().toString();
                mRfidTask = new RfidTask(mRc522);
                mRfidTask.execute();
                ((Button)v).setText("Writing Data");
            }
        });


        PeripheralManager pioService = PeripheralManager.getInstance();
        try {
            spiDevice = pioService.openSpiDevice(SPI_PORT);
            gpioReset = pioService.openGpio(PIN_RESET);
            mRc522 = new Rc522(spiDevice, gpioReset);
            mRc522.setDebugging(true);
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(spiDevice != null){
                spiDevice.close();
            }
            if(gpioReset != null){
                gpioReset.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class RfidTask extends AsyncTask<Object, Object, Boolean> {
        private static final String TAG = "RfidTask";
        private Rc522 rc522;

        RfidTask(Rc522 rc522) {
            this.rc522 = rc522;
        }

        @Override
        protected void onPreExecute() {
            // rc522.
            button.setEnabled(false);
            mTagResultsView.setVisibility(View.GONE);
            mTagDetectedView.setVisibility(View.GONE);
            mTagUidView.setVisibility(View.GONE);
            resultsText = "";
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            rc522.stopCrypto();
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
                //Check if a RFID tag has been found
                if (!rc522.request()) {
                    continue;
                }
                //Check for collision errors
                if (!rc522.antiCollisionDetect()) {
                    continue;
                }
                byte[] uuid = rc522.getUid();
                return rc522.selectTag(uuid);
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                mTagResultsView.setText(R.string.unknown_error);
                return;
            }

            byte[] nameBytes = userName.getBytes();
            byte[] dobBytes = userDOB.getBytes();
            byte[] idBytes = userID.getBytes();

            int count = 0;
            int block = 0;
            int nameLength = nameBytes.length;

            // Mifare's card default key A and key B, the key may have been changed previously
            byte[] key = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
            // Each sector holds 16 bytes
            // In this case, Rc522.AUTH_A or Rc522.AUTH_B can be used

            // Write name to RFID card
            while (count < nameLength) {
                byte addressName = Rc522.getBlockAddress(1, block);
                //We need to authenticate the card, each sector can have a different key
                boolean result = rc522.authenticateCard(Rc522.AUTH_A, addressName, key);
                if (!result) {
                    mTagResultsView.setText(R.string.authetication_error);
                    return;
                }

                byte[] newData = new byte[16];
                if (nameLength - count < 16) {
                    int length = nameLength - count;
                    for (int index1 = 0; index1 < length; index1++) {
                        newData[index1] = nameBytes[count + index1];
                    }
                } else {
                    for (int index2 = 0; index2 < 16; index2++) {
                        newData[index2] = nameBytes[count + index2];
                    }
                }

                // Write blank data to current block
                byte[] blankData = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
                rc522.writeBlock(addressName, blankData);

                // Write new data to current block
                result = rc522.writeBlock(addressName, newData);
                if (!result) {
                    mTagResultsView.setText(R.string.write_error);
                    break;
                    //return;
                }
                resultsText += "\nSector written successfully";
                mTagResultsView.setText(resultsText);
                block++;
                count += 16;
            }

            // Write DOB and ID to RFID card
            byte addressDOBID = Rc522.getBlockAddress(1, 2);
            //We need to authenticate the card, each sector can have a different key
            boolean result = rc522.authenticateCard(Rc522.AUTH_A, addressDOBID, key);
            if (!result) {
                mTagResultsView.setText(R.string.authetication_error);
                return;
            }
            byte[] dataDobId = new byte[16];
            int index = 0;
            while (index < 8){
                dataDobId[index] = dobBytes[index];
                index++;
            }
            while (index < 15){
                dataDobId[index] = idBytes[index-8];
                index++;
            }

            result = rc522.writeBlock(addressDOBID, dataDobId);
            if (!result) {
                mTagResultsView.setText(R.string.write_error);
                return;
            }
            resultsText += "\nSector written successfully";
            mTagResultsView.setText(resultsText);

            rc522.stopCrypto();
            button.setEnabled(true);
            button.setText("Write Data");
            mTagUidView.setText("UID: " + rc522.getUidString());
            mTagResultsView.setVisibility(View.VISIBLE);
            mTagDetectedView.setVisibility(View.VISIBLE);
            mTagUidView.setVisibility(View.VISIBLE);

        }
    }
}