package Lab1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;
import java.io.IOException;

// Library to support normal pins using PWM
import com.leinardi.android.things.pio.SoftPwm;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    // Switch state to true to enable
    private boolean EnableBT1 = false;
    private boolean EnableBT2 = false;
    private boolean EnableBT3 = false;
    private boolean EnableBT4 = false;
    private boolean EnableBT5 = false;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler = new Handler();
    private Gpio mButtonGpio;
    private Gpio mRedLedGpio;
    private Gpio mGreenLedGpio;
    private Gpio mBlueLedGpio;
    private SoftPwm mRedPwmGpio;
    private SoftPwm mGreenPwmGpio;
    private SoftPwm mBluePwmGpio;
    static int State = 0;
    private static int stateRGB = 0;
    private static int stateR = 1;
    private static int stateG = 2;
    private static int stateB = 3;
    protected int Interval_2000ms = 2000;
    protected int Interval_1000ms = 1000;
    protected int Interval_500ms = 500;
    protected int Interval_100ms = 100;
    int mInterval = 2000;

    // Red (255, 0, 0)
    // Green (0, 255, 0)
    // Blue (0, 0, 255)
    // Yellow (255, 255, 0)
    // Purple (80, 0, 80)
    // Aqua (0, 255, 255)
    int[][] colorValues = {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}, {80, 0, 80}, {0, 255, 255}};
    String[] colorList = {"Red", "Green", "Blue", "Yellow", "Purple", "Aqua"};
    int index = 0;
    static final int freq = 120;
    static int duty_cycle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] pinName = BoardDefaults.getGPIOForRGBLED();

        if (EnableBT1) {
            try {
                Log.i(TAG, "Starting Blinking LED RGB");

                // Initiate pins of LED1
                mRedLedGpio = PeripheralManager.getInstance().openGpio(pinName[0]);
                mRedLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                mGreenLedGpio = PeripheralManager.getInstance().openGpio(pinName[1]);
                mGreenLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                mBlueLedGpio = PeripheralManager.getInstance().openGpio(pinName[2]);
                mBlueLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

                mHandler.post(BT1);
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to open pins");
            }
        } else if (EnableBT2) {
            try {
                // Initiate Button
                mButtonGpio = PeripheralManager.getInstance().openGpio(pinName[3]);
                mButtonGpio.setDirection(Gpio.DIRECTION_IN);
                mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);

                mButtonGpio.registerGpioCallback(new GpioCallback() {
                    @Override
                    public boolean onGpioEdge(Gpio gpio) {
                        Log.d(TAG, "button was pressed");
                        switch (mInterval) {
                            case 2000:
                                mHandler.removeCallbacks(BT2);
                                mInterval = Interval_1000ms;
                                mHandler.post(BT2);
                                break;
                            case 1000:
                                mHandler.removeCallbacks(BT2);
                                mInterval = Interval_500ms;
                                mHandler.post(BT2);
                                break;
                            case 500:
                                mHandler.removeCallbacks(BT2);
                                mInterval = Interval_100ms;
                                mHandler.post(BT2);
                                break;
                            case 100:
                                mHandler.removeCallbacks(BT2);
                                mInterval = Interval_2000ms;
                                mHandler.post(BT2);
                                break;
                            default:
                                mHandler.removeCallbacks(BT2);
                                mHandler.postDelayed(BT2, mInterval);
                                mInterval = Interval_2000ms;
                                break;
                        }
                        return true;
                    }
                });

                // Initiate LED1
                mRedLedGpio = PeripheralManager.getInstance().openGpio(pinName[0]);
                mRedLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                mGreenLedGpio = PeripheralManager.getInstance().openGpio(pinName[1]);
                mGreenLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                mBlueLedGpio = PeripheralManager.getInstance().openGpio(pinName[2]);
                mBlueLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

                mHandler.post(BT2);
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to open pins");
            }
        } else if (EnableBT3) {
            try {
                // Initiate LED1
                mRedLedGpio = PeripheralManager.getInstance().openGpio(pinName[0]);
                mRedLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                mGreenLedGpio = PeripheralManager.getInstance().openGpio(pinName[1]);
                mGreenLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                mBluePwmGpio = SoftPwm.openSoftPwm(pinName[2]);

                mHandler.post(BT3);
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to open pins");
            }
        } else if (EnableBT4) {
            try {

                // Initiate Button
                mButtonGpio = PeripheralManager.getInstance().openGpio(pinName[3]);
                mButtonGpio.setDirection(Gpio.DIRECTION_IN);
                mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);

                // Initiate LED1
                mRedPwmGpio = SoftPwm.openSoftPwm(pinName[0]);
                mGreenPwmGpio = SoftPwm.openSoftPwm(pinName[1]);
                mBluePwmGpio = SoftPwm.openSoftPwm(pinName[2]);
                mButtonGpio.registerGpioCallback(new GpioCallback() {
                    @Override
                    public boolean onGpioEdge(Gpio gpio) {
                        Log.d(TAG, "button was pressed");
                        duty_cycle = 0;
                        switch (State) {
                            case 0:
                                mHandler.removeCallbacks(mInitRGBPwm);
                                State = stateR;
                                mHandler.post(mInitRPwm);
                                break;
                            case 1:
                                mHandler.removeCallbacks(mInitRPwm);
                                State = stateG;
                                mHandler.post(mInitGPwm);
                                break;
                            case 2:
                                mHandler.removeCallbacks(mInitGPwm);
                                State = stateB;
                                mHandler.post(mInitBPwm);
                                break;
                            case 3:
                                mHandler.removeCallbacks(mInitBPwm);
                                State = stateRGB;
                                mHandler.post(mInitRGBPwm);
                                break;

                        }
                        return true;
                    }
                });
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to open pins");
            }
        } else if (EnableBT5) {
            try {
                // Initiate LED1, LED2 and LED3
                mRedLedGpio = PeripheralManager.getInstance().openGpio(pinName[0]);
                mRedLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                mGreenLedGpio = PeripheralManager.getInstance().openGpio(pinName[4]);
                mGreenLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                mBlueLedGpio = PeripheralManager.getInstance().openGpio(pinName[5]);
                mBlueLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

                mHandler.post(BT5);
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to open pins");
            }
        }
    }

    private Runnable BT1 = new Runnable() {
        @Override
        public void run() {
            if (mRedLedGpio == null | mGreenLedGpio == null | mBlueLedGpio == null){
                return;
            }
            if (index == 5) {
                index = 0;
            }
            else {
                index++;
            }

            LedColor led = new LedColor();
            led.setColor(mRedLedGpio,colorValues[index][0], mGreenLedGpio, colorValues[index][1],
                    mBlueLedGpio, colorValues[index][2]);

            mHandler.postDelayed(BT1, mInterval);
        }
    };

    private  Runnable BT2 = new Runnable() {
        @Override
        public void run() {
            if (mRedLedGpio==null | mGreenLedGpio ==null || mBlueLedGpio == null)
                return;
            if (index == 5) {
                index = 0;
            }
            else {
                index++;
            }

            LedColor led = new LedColor();
            led.setColor(mRedLedGpio,colorValues[index][0], mGreenLedGpio, colorValues[index][1],
                    mBlueLedGpio, colorValues[index][2]);
            mHandler.postDelayed(BT2, mInterval);

        }
    };

    private Runnable BT3 = new Runnable() {

        @Override

        public void run() {

            if(mRedLedGpio == null || mGreenLedGpio == null || mBluePwmGpio == null){
                return;
            }
            try {

                // Set value of Red and Green in LED1 to off state
                mRedLedGpio.setValue(true);
                mGreenLedGpio.setValue(true);

                // Set Blue pin of LED1 to PWM pin out with initiation of frequency and duty cycle
                mBluePwmGpio.setPwmFrequencyHz(freq);
                mBluePwmGpio.setPwmDutyCycle(duty_cycle);

                // Increase duty cycle with step = 25
                if(duty_cycle == 100){
                    Log.d(TAG," Max duty_cycle" + duty_cycle);
                    duty_cycle=0;
                }
                else {
                    duty_cycle+= 25;
                }
                Log.d(TAG," duty_cycle" + duty_cycle);

                // Enable the PWM
                mBluePwmGpio.setEnabled(true);

                mHandler.postDelayed(BT3, Interval_1000ms);

            } catch ( IOException e){
                Log.e(TAG, "unable to access PWM", e);

            }
        }
    };

    protected  Runnable mInitRGBPwm = new Runnable() {
        @Override

        public void run() {
            try {
                Log.d(TAG," RGBState");

                // Init RedPwm
                mRedPwmGpio.setPwmFrequencyHz(120);
                mRedPwmGpio.setPwmDutyCycle(duty_cycle);
                mRedPwmGpio.setEnabled(true);

                //Init GreenPwm
                mGreenPwmGpio.setPwmFrequencyHz(120);
                mGreenPwmGpio.setPwmDutyCycle(duty_cycle);
                mGreenPwmGpio.setEnabled(true);

                // Init BluePwm
                mBluePwmGpio.setPwmFrequencyHz(120);
                mBluePwmGpio.setPwmDutyCycle(duty_cycle);
                mBluePwmGpio.setEnabled(true);

                // Each step increases duty cycle 20%
                if (duty_cycle == 100) {
                    duty_cycle = 0;
                }
                else {
                    duty_cycle += 20;
                }

                mHandler.postDelayed(mInitRGBPwm, Interval_1000ms);


            } catch (IOException e){
                Log.e(TAG,"Unable to init soft pwm");

            }
        }
    };

    // Red PWM state
    protected  Runnable mInitRPwm = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG," Red State");

                // Init RedPwm
                mRedPwmGpio.setPwmFrequencyHz(120);
                mRedPwmGpio.setPwmDutyCycle(duty_cycle);
                mRedPwmGpio.setEnabled(true);

                // Cancel GreenPwm
                mGreenPwmGpio.setPwmDutyCycle(100);
                mGreenPwmGpio.setEnabled(true);
                // Cancel BluePwm
                mBluePwmGpio.setPwmDutyCycle(100);
                mBluePwmGpio.setEnabled(true);

                if (duty_cycle == 100) {
                    duty_cycle = 0;
                }
                else {
                    duty_cycle += 20;
                }

                mHandler.postDelayed(mInitRPwm, Interval_1000ms);

            } catch (IOException e){
                Log.e(TAG,"Unable to init soft pwm");
            }
        }
    };


    // Green PWM state
    protected  Runnable mInitGPwm = new Runnable() {
        @Override
        public void run() {
            try {

                Log.d(TAG," Green State");

                // Init GreenPwm
                mGreenPwmGpio.setPwmFrequencyHz(120);
                mGreenPwmGpio.setPwmDutyCycle(duty_cycle);
                mGreenPwmGpio.setEnabled(true);

                // Cancel RedPwm
                mRedPwmGpio.setPwmDutyCycle(100);
                mRedPwmGpio.setEnabled(true);
                // Cancel BluePwm
                mBluePwmGpio.setPwmDutyCycle(100);
                mBluePwmGpio.setEnabled(true);

                if (duty_cycle == 100) {
                    duty_cycle = 0;
                }
                else {
                    duty_cycle += 20;
                }

                mHandler.postDelayed(mInitGPwm, Interval_1000ms);

            } catch (IOException e){
                Log.e(TAG,"Unable to init soft pwm");
            }
        }
    };

    // Blue PWM state
    protected  Runnable mInitBPwm = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG," Blue State");

                // Init BluePwm
                mBluePwmGpio.setPwmFrequencyHz(120);
                mBluePwmGpio.setPwmDutyCycle(duty_cycle);
                mBluePwmGpio.setEnabled(true);

                // Cancel GreenPwm
                mGreenPwmGpio.setPwmDutyCycle(100);
                mGreenPwmGpio.setEnabled(true);
                // Cancel Red Pwm
                mRedPwmGpio.setPwmDutyCycle(100);
                mRedPwmGpio.setEnabled(false);

                if (duty_cycle == 100) {
                    duty_cycle = 0;
                }
                else {
                    duty_cycle += 20;
                }

                mHandler.postDelayed(mInitBPwm, Interval_1000ms);

            } catch (IOException e){
                Log.e(TAG,"Unable to init soft pwm");
            }
        }
    };

    private Runnable BT5 = new Runnable() {
        @Override
        public void run() {
            if (mRedLedGpio == null | mGreenLedGpio == null | mBlueLedGpio == null){
                return;
            }
            LedColor led = new LedColor();
            int stepCount = 0;
            int step = 250;

            // Each step is 250ms, use lowest common multiple to change state of LEDs
            while(true) {
                led.setColor(mRedLedGpio, 0, mGreenLedGpio, 0, mBlueLedGpio, 0);
                stepCount++;
                try {
                    // Display 3 LEDs - at 6th second
                    if (stepCount % 12 == 0){
                        mRedLedGpio.setValue(false);
                        mGreenLedGpio.setValue(false);
                        mBlueLedGpio.setValue(false);
                    }
                    // Display Red & Green LEDs - at 2nd second
                    else if(stepCount % 4 == 0){
                        mRedLedGpio.setValue(false);
                        mGreenLedGpio.setValue(false);
                        mBlueLedGpio.setValue(true);
                    }
                    // Display Red & Blue LEDs - at 3rd second
                    else if(stepCount % 6 == 0){
                        mRedLedGpio.setValue(false);
                        mGreenLedGpio.setValue(true);
                        mBlueLedGpio.setValue(false);
                    }
                    // Display Red LED - every 500ms
                    else {
                        mRedLedGpio.setValue(false);
                        mGreenLedGpio.setValue(true);
                        mBlueLedGpio.setValue(true);
                    }
                    sleep(step);

                    // Off all LEDs
                    mBlueLedGpio.setValue(true);
                    mGreenLedGpio.setValue(true);
                    mRedLedGpio.setValue(true);
                    sleep(step);
                }
                catch (IOException e) {
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }

            }
        }
    };
    private void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacksAndMessages(null);

        Log.i(TAG, "Closing LED GPIO pins");
        try {
            mButtonGpio.close();
            mRedLedGpio.close();
            mGreenLedGpio.close();
            mBlueLedGpio.close();
            mRedPwmGpio.close();
            mGreenPwmGpio.close();
            mBluePwmGpio.close();
        } catch (IOException e){
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mButtonGpio = null;
            mRedLedGpio = null;
            mGreenLedGpio = null;
            mBlueLedGpio = null;
            mRedPwmGpio = null;
            mGreenPwmGpio = null;
            mBluePwmGpio = null;
        }
    }
}
