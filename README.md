# IoTApplicationDevelopment-AndroidThings
This repository is created for IoT Application Development course at HCMUT

## Getting started
Raspberry Pi 3 model B running Android Things will be used in this course

### Lab 1
1. Using three pins to control an RGB LED displaying in different colors.
2. Get input from a button and then change the pace of color displaying (ex 1). For example, the RGB LED changes colors in 2s by default. After a button is pressed, the rate will change to 1s, then 0.5s, 0.1s and back to 2s.
3. Similar to exercise 1, control the RGB LED by using PWM to change the brightness of the led. Please read this link for more details of PWM: https://developer.android.com/things/sdk/pio/pwm
4. Get input from a button and change the brightness of each color of the RGB LED. For example, the RGB LED changes the brightness of red, green, blue by default. After a button is pressed, only the red one is changing its brightness, then green, blue and back to three colors.
5. Blink each LED in different paces. The RED LED is blinking every 0.5s, the green is 2s, and the blue is 3s.

#### Hardware
* Raspberry Pi 3 model B
* 3 RGB LEDs (common anode)
* Wires

![alt text](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/master/images/Lab1IoT_bb.jpg)

#### Running
To run exercises, change state of [Define variables](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/3f71e12cbf8dff49b3ba8e6265d4268491927256/app/src/main/java/com/example/conghuong/lab_1_iot/MainActivity.java#L39) 'EnableBTx' to true.

* [Exercise1 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/3f71e12cbf8dff49b3ba8e6265d4268491927256/app/src/main/java/com/example/conghuong/lab_1_iot/MainActivity.java#L227)
* [Exercise2 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/3f71e12cbf8dff49b3ba8e6265d4268491927256/app/src/main/java/com/example/conghuong/lab_1_iot/MainActivity.java#L254)
* [Exercise3 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/3f71e12cbf8dff49b3ba8e6265d4268491927256/app/src/main/java/com/example/conghuong/lab_1_iot/MainActivity.java#L274)
* [Exercise4 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/3f71e12cbf8dff49b3ba8e6265d4268491927256/app/src/main/java/com/example/conghuong/lab_1_iot/MainActivity.java#L315)
* [Exercise5 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/3f71e12cbf8dff49b3ba8e6265d4268491927256/app/src/main/java/com/example/conghuong/lab_1_iot/MainActivity.java#L459)
