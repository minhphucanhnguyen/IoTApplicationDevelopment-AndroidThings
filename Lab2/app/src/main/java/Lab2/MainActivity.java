package Lab2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;
import java.io.IOException;
import com.google.android.things.pio.UartDevice;

// Library to support normal pins using PWM
import com.google.android.things.pio.UartDeviceCallback;
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

    private boolean isReady = false;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler = new Handler();
    private int runningExercise = 0;
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
    protected int mInterval = 2000;
    protected int stepCount = 0;

    // UART Configuration Parameters
    private static final int BAUD_RATE = 115200;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;

    private static final int CHUNK_SIZE = 512;

    private HandlerThread mInputThread;
    private Handler mInputHandler;

    private UartDevice mLoopbackDevice;

    // Red (255, 0, 0)
    // Green (0, 255, 0)
    // Blue (0, 0, 255)
    // Yellow (255, 255, 0)
    // Purple (80, 0, 80)
    // Aqua (0, 255, 255)
    int[][] colorValues = {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}, {80, 0, 80}, {0, 255, 255}};
    String[] colorList = {"Red", "Green", "Blue", "Yellow", "Purple", "Aqua"};
    int i = 0;
    static final int freq = 120;
    static int duty_cycle = 0;

    private Runnable mTransferUartRunnable = new Runnable() {
        @Override
        public void run() {
            processUartData();
        }
    };

    private Runnable BT1Init = new Runnable(){
        @Override
        public void run() {
            String[] pinName = BoardDefaults.getGPIOForRGBLED();
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
             //   Log.e(TAG, "Unable to open pins");
            }
        }
    };

    private Runnable BT2Init = new Runnable(){
        @Override
        public void run() {
            String[] pinName = BoardDefaults.getGPIOForRGBLED();
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
        }
    };

    private Runnable BT3Init = new Runnable() {
        @Override
        public void run() {
            try {
                String[] pinName = BoardDefaults.getGPIOForRGBLED();
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
        }
    };

    private Runnable BT4Init = new Runnable() {
        @Override
        public void run() {
            try {
                String[] pinName = BoardDefaults.getGPIOForRGBLED();
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
        }
    };

    private Runnable BT5Init = new Runnable() {
        @Override
        public void run() {
            String[] pinName = BoardDefaults.getGPIOForRGBLED();
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] pinName = BoardDefaults.getGPIOForRGBLED();

        // Create a background looper thread for I/O
        mInputThread = new HandlerThread("InputThread");
        mInputThread.start();
        mInputHandler = new Handler(mInputThread.getLooper());

        // Attempt to access the UART device
        try {
            openUart(pinName[6], BAUD_RATE);
            // Read any initially buffered data
            mInputHandler.post(mTransferUartRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open UART device", e);
        }
    }

    private Runnable BT1 = new Runnable() {
        @Override
        public void run() {

            if (mRedLedGpio == null | mGreenLedGpio == null | mBlueLedGpio == null){
                return;
            }
            if (i == 5) {
                i = 0;
            }
            else {
                i++;
            }

            LedColor led = new LedColor();
            led.setColor(mRedLedGpio,colorValues[i][0], mGreenLedGpio, colorValues[i][1],
                    mBlueLedGpio, colorValues[i][2]);
            mHandler.postDelayed(BT1, Interval_2000ms);
        }
    };

    private  Runnable BT2 = new Runnable() {
        @Override
        public void run() {
            if (mRedLedGpio==null | mGreenLedGpio ==null || mBlueLedGpio == null)
                return;
            if (i == 5) {
                i = 0;
            }
            else {
                i++;
            }

            LedColor led = new LedColor();
            led.setColor(mRedLedGpio,colorValues[i][0], mGreenLedGpio, colorValues[i][1],
                    mBlueLedGpio, colorValues[i][2]);
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
                    duty_cycle+= 20;
                }
                Log.d(TAG," duty_cycle" + duty_cycle);

                // Enable the PWM
                mBluePwmGpio.setEnabled(true);

                mHandler.postDelayed(BT3, Interval_1000ms);

            } catch ( IOException e){
                Log.e(TAG, "Unable to access PWM", e);

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
                Log.e(TAG,"Unable to access pwm");

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
                Log.e(TAG,"Unable to init PWM");
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
                Log.e(TAG,"Unable to init PWM");
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
                Log.e(TAG,"Unable to init PWM");
            }
        }
    };

    private Runnable BT5 = new Runnable() {
        @Override
        public void run() {
            if (mRedLedGpio == null | mGreenLedGpio == null | mBlueLedGpio == null) {
                return;
            }
            LedColor led = new LedColor();
            int step = 250;

            // Each step is 250ms, use lowest common multiple to change state of LEDs
            stepCount++;
            // Display 3 LEDs - at 6th second
            if (stepCount % 24 == 0) {
                led.setColor(mRedLedGpio, 255, mGreenLedGpio, 255, mBlueLedGpio, 255);
            }
            // Display Red & Green LEDs - at 2nd second
            else if (stepCount % 8 == 0) {
                led.setColor(mRedLedGpio, 255, mGreenLedGpio, 255, mBlueLedGpio, 0);
            }
            // Display Red & Blue LEDs - at 3rd second
            else if (stepCount % 12 == 0) {
                led.setColor(mRedLedGpio, 255, mGreenLedGpio, 0, mBlueLedGpio, 255);
            }
            // Display Red LED - every 500ms
            else if (stepCount % 2 == 0) {
                led.setColor(mRedLedGpio, 255, mGreenLedGpio, 0, mBlueLedGpio, 0);
            } else {
                // Off all LEDs
                led.setColor(mRedLedGpio, 0, mGreenLedGpio, 0, mBlueLedGpio, 0);
            }

            mHandler.postDelayed(BT5, step);

        }
    };

    /**
     * Callback invoked when UART receives new incoming data.
     */
    private UartDeviceCallback mCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            // Queue up a data transfer
            processUartData();
            //Continue listening for more interrupts
            return true;
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            Log.w(TAG, uart + ": Error event " + error);
        }
    };

    /* Private Helper Methods */

    /**
     * Access and configure the requested UART device for 8N1.
     *
     * @param name Name of the UART peripheral device to open.
     * @param baudRate Data transfer rate. Should be a standard UART baud,
     *                 such as 9600, 19200, 38400, 57600, 115200, etc.
     *
     * @throws IOException if an error occurs opening the UART port.
     */
    private void openUart(String name, int baudRate) throws IOException {
        mLoopbackDevice = PeripheralManager.getInstance().openUartDevice(name);
        // Configure the UART
        mLoopbackDevice.setBaudrate(baudRate);
        mLoopbackDevice.setDataSize(DATA_BITS);
        mLoopbackDevice.setParity(UartDevice.PARITY_NONE);
        mLoopbackDevice.setStopBits(STOP_BITS);

        mLoopbackDevice.registerUartDeviceCallback(mInputHandler, mCallback);
    }

    /**
     * Close the UART device connection, if it exists
     */
    private void closeUart() throws IOException {
        if (mLoopbackDevice != null) {
            mLoopbackDevice.unregisterUartDeviceCallback(mCallback);
            try {
                mLoopbackDevice.close();
            } finally {
                mLoopbackDevice = null;
            }
        }
    }

    /**
     * Receive data from UART to control the program
     * Potentially long-running operation. Call from a worker thread.
     */
    private void processUartData() {
        if (mLoopbackDevice != null) {
            // Loop until there is no more data in the RX buffer.
            try {
                byte[] buffer = new byte[CHUNK_SIZE];
                int read;
                while ((read = mLoopbackDevice.read(buffer, buffer.length)) > 0) {
                    int key = (int) buffer[0];
                    char c = ((char) key);
                    Log.d(TAG, "Received: " + c);
                    int d = c;
                    Log.d(TAG,"ascii "+d );
                    switch (d) {
                        case 79:
                            Log.d(TAG, "App starts ready to receive commands");
                            isReady = true;
                            break;
                        case 49:
                            if (isReady) {
                                Log.d(TAG, " Running exercise 1");
                                pinDestructor(runningExercise);
                                runningExercise = 1;
                                mHandler.post(BT1Init);
                            }
                            break;
                        case 50:
                            if (isReady) {
                                Log.d(TAG, " Running exercise 2");
                                pinDestructor(runningExercise);
                                runningExercise = 2;
                                mHandler.post(BT2Init);
                            }
                            break;
                        case 51:
                            if (isReady) {
                                Log.d(TAG, " Running exercise 3");
                                pinDestructor(runningExercise);
                                runningExercise = 3;
                                mHandler.post(BT3Init);
                            }
                            break;
                        case 52:
                            if (isReady) {
                                Log.d(TAG, " Running exercise 4");
                                pinDestructor(runningExercise);
                                runningExercise = 4;
                                mHandler.post(BT4Init);
                            }
                            break;
                        case 53:
                            if (isReady) {
                                Log.d(TAG, " Running exercise 5");
                                pinDestructor(runningExercise);
                                runningExercise = 5;
                                mHandler.post(BT5Init);
                            }
                            break;
                        case 70:
                            pinDestructor(runningExercise);
                            runningExercise = 0;
                            isReady = false;
                            break;
                        default:
                            Log.d(TAG,"Unexpected character ");
                            break;
                    }
                }
            } catch (IOException e) {
                Log.w(TAG, "Unable to transfer data over UART", e);
            }
        }
    }

    protected void pinDestructor(int currentRunnable){
        Log.d(TAG," current Runnable = "+ currentRunnable);
        mHandler.removeCallbacksAndMessages(null);
        switch (currentRunnable){
            case 1:
                try {
                    mRedLedGpio.setValue(true);
                    mGreenLedGpio.setValue(true);
                    mBlueLedGpio.setValue(true);
                    mRedLedGpio.close();
                    mGreenLedGpio.close();
                    mBlueLedGpio.close();
                } catch (IOException e){
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }
                break;
            case 2:
                try {
                    mRedLedGpio.setValue(true);
                    mGreenLedGpio.setValue(true);
                    mBlueLedGpio.setValue(true);
                    mButtonGpio.close();
                    mRedLedGpio.close();
                    mGreenLedGpio.close();
                    mBlueLedGpio.close();
                } catch (IOException e){
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }
                break;
            case 3:
                try {
                    mRedLedGpio.setValue(true);
                    mGreenLedGpio.setValue(true);
                    mBluePwmGpio.setEnabled(false);
                    mRedLedGpio.close();
                    mGreenLedGpio.close();
                    mBluePwmGpio.close();
                } catch (IOException e){
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }
                break;
            case 4:
                try {
                    mButtonGpio.close();
                    mRedPwmGpio.setEnabled(false);
                    mGreenPwmGpio.setEnabled(false);
                    mBluePwmGpio.setEnabled(false);
                    mRedPwmGpio.close();
                    mGreenPwmGpio.close();
                    mBluePwmGpio.close();
                } catch (IOException e){
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }
                break;
            case 5:
                try {
                    mRedLedGpio.setValue(true);
                    mGreenLedGpio.setValue(true);
                    mBlueLedGpio.setValue(true);
                    mRedLedGpio.close();
                    mGreenLedGpio.close();
                    mBlueLedGpio.close();
                } catch (IOException e){
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }
                break;
            default:
                Log.d(TAG,"Unexpected number");
                break;
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

        // Terminate the worker thread
        if (mInputThread != null) {
            mInputThread.quitSafely();
        }

        // Attempt to close the UART device
        try {
            closeUart();
        } catch (IOException e) {
            Log.e(TAG, "Error closing UART device:", e);
        }
    }
}