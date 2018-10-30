package com.example.pa.lab3_readrfid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
    private Handler mHandler = new Handler();
    RfidTask mRfidTask;
    private TextView mTagDetectedView;
    private TextView mTagUidView;
    private TextView mTagResultsView;
    private String[] groupID = {"1510068", "1512049", "1511434", "1513668"};

    private SpiDevice spiDevice;
    private Gpio gpioReset;
    private Gpio mRedGpio;
    private Gpio mGreenGpio;
    private Gpio mBlueGpio;

    private static final String SPI_PORT = "SPI0.0";
    private static final String PIN_RESET = "BCM25";
    private static final String RED_PIN = "BCM26";
    private static final String GREEN_PIN = "BCM19";
    private static final String BLUE_PIN = "BCM13";

    String resultsText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagDetectedView = (TextView)findViewById(R.id.tag_read);
        mTagUidView = (TextView)findViewById(R.id.tag_uid);
        mTagResultsView = (TextView) findViewById(R.id.tag_results);

        PeripheralManager pioService = PeripheralManager.getInstance();
        try {
            mRedGpio = PeripheralManager.getInstance().openGpio(RED_PIN);
            mGreenGpio = PeripheralManager.getInstance().openGpio(GREEN_PIN);
            mBlueGpio = PeripheralManager.getInstance().openGpio(BLUE_PIN);

            mRedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mGreenGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mBlueGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mRedGpio.setValue(true);
            mGreenGpio.setValue(true);
            mBlueGpio.setValue(false);
            spiDevice = pioService.openSpiDevice(SPI_PORT);
            gpioReset = pioService.openGpio(PIN_RESET);
            mRc522 = new Rc522(spiDevice, gpioReset);
            mRc522.setDebugging(true);
            mHandler.post(mRunnable);
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if (mRedGpio != null){
                mRedGpio.close();
            }
            if (mGreenGpio != null){
                mGreenGpio.close();
            }
            if (mBlueGpio != null){
                mBlueGpio.close();
            }
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

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mRfidTask = new RfidTask(mRc522);
            mRfidTask.execute();
        }
    };

    private class RfidTask extends AsyncTask<Object, Object, Boolean> {
        private static final String TAG = "RfidTask";
        private Rc522 rc522;

        RfidTask(Rc522 rc522) {
            this.rc522 = rc522;
        }

        @Override
        protected void onPreExecute() {
            resultsText = "";
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            int count=0;
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
                    if(count == 3) {
                        try {
                            mRedGpio.setValue(true);
                            mGreenGpio.setValue(true);
                            mBlueGpio.setValue(false);

                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                        count=0;
                    }
                    count++;
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
            boolean error = false;
            String name = "\nName: ";
            String dob = "\nDOB: ";
            String id = "";
            int sector = 1;
            if (!success) {
                mTagResultsView.setText(R.string.authetication_error);
                error = true;
                mHandler.postDelayed(mRunnable, 200);
                //return;
            }

            if (!error) {
                mTagUidView.setText("Tag UID: " + rc522.getUidString());
                mTagResultsView.setVisibility(View.VISIBLE);
                mTagDetectedView.setVisibility(View.VISIBLE);
                mTagUidView.setVisibility(View.VISIBLE);
                boolean authenOK = false;
                boolean result;
                for (int block = 0; block < 3; block++) {
                    // Try to avoid doing any non RC522 operations until you're done communicating with it.=
                    byte address = Rc522.getBlockAddress(sector, block);
                    // Mifare's card default key A and key B, the key may have been changed previously
                    byte[] key = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
                    // In this case, Rc522.AUTH_A or Rc522.AUTH_B can be used
                    //We need to authenticate the card, each sector can have a different key
                    if (!authenOK) {
                        result = rc522.authenticateCard(Rc522.AUTH_B, address, key);
                        if (!result) {
                            mTagResultsView.setText(R.string.authetication_error);
                            mHandler.postDelayed(mRunnable, 200);
                            break;
                        }
                        authenOK = true;
                    }

                    byte[] buffer = new byte[16];
                    //Since we're still using the same block, we don't need to authenticate again

                    // READ BLOCK
                    result = rc522.readBlock(address, buffer);
                    if (!result) {
                        mTagResultsView.setText(R.string.read_error);
                        mHandler.postDelayed(mRunnable, 200);
                        break;
                    }
                    resultsText += "\nSector " + sector + " - Block" + block + " read successfully: " + Rc522.dataToHexString(buffer);

                    String string = new String(buffer);
                    if (block == 0 | block == 1) {
                        name += string;
                    } else {
                        dob += string.substring(0, 2) + "/" + string.substring(2, 4) + "/" + string.substring(4, 8)+"\nID: ";
                        id += string.substring(8, 15);
                        id=id.trim();
                        for (int index = 0; index < 4; index++) {
                            if (id.equalsIgnoreCase(groupID[index])) {
                                try {
                                    mRedGpio.setValue(true);
                                    mGreenGpio.setValue(false);
                                    mBlueGpio.setValue(true);
                                } catch (java.io.IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            } else if (index == 3) {

                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                for (int ledIndex = 0; ledIndex < 5; ledIndex++) {
                                    try {
                                        mRedGpio.setValue(false);
                                        mGreenGpio.setValue(true);
                                        mBlueGpio.setValue(true);
                                        Thread.sleep(200);
                                        mRedGpio.setValue(true);
                                        mGreenGpio.setValue(true);
                                        mBlueGpio.setValue(true);
                                        if (ledIndex != 4) {
                                            Thread.sleep(200);
                                        }
                                    } catch (java.io.IOException e) {
                                        e.printStackTrace();
                                    } catch (java.lang.InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    mTagResultsView.setText(resultsText);

                }
                resultsText += name + dob + id;
                mTagResultsView.setText(resultsText);
            }
            rc522.stopCrypto();
            mHandler.postDelayed(mRunnable, 200);
        }
    }
}