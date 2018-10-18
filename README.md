IoTApplicationDevelopment-AndroidThings
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
* Button
* 1k registors
* Wires

![alt text](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/master/images/Lab1IoT_bb.jpg)

#### Running
To run exercises, change state of [Define variables](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/4d9d04e50bf7ce6191cacda33ff5a37e27b01b31/Lab1/Lab1/src/main/java/Lab1/MainActivity.java#L38) 'EnableBTx' to true.

* [Exercise1 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/4d9d04e50bf7ce6191cacda33ff5a37e27b01b31/Lab1/Lab1/src/main/java/Lab1/MainActivity.java#L228)
* [Exercise2 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/4d9d04e50bf7ce6191cacda33ff5a37e27b01b31/Lab1/Lab1/src/main/java/Lab1/MainActivity.java#L249)
* [Exercise3 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/4d9d04e50bf7ce6191cacda33ff5a37e27b01b31/Lab1/Lab1/src/main/java/Lab1/MainActivity.java#L269)
* [Exercise4 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/4d9d04e50bf7ce6191cacda33ff5a37e27b01b31/Lab1/Lab1/src/main/java/Lab1/MainActivity.java#L310)
* [Exercise5 Runnable](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/4d9d04e50bf7ce6191cacda33ff5a37e27b01b31/Lab1/Lab1/src/main/java/Lab1/MainActivity.java#L454)

### Lab 2
The previous assignment includes 5 exercises that detail as follows:
1. Using three pins to control an RGB LED displaying in different colors.
2.  Get  input  from  a  button  and  then  change  the  pace  of  color  displaying  (ex  1).  For example, the RGB LED changes colors in 2s by default. After a button is pressed, the rate will change to 1s, then 0.5s, 0.1s and back to 2s. 
3. Similar to exercise 1, control the RGB LED by using PWM to change the brightness of the led. 
Please read this link for more details of PWM: 
https://developer.android.com/things/sdk/pio/pwm
4. Get input from a button and change the brightness of each color of the RGB LED. For example,  the  RGB  LED  changes  the  brightness of red, green, blue by default. 
After a button is pressed, only the red one is changing its brightness, then green, blue and back to three colors.
5. Blink each LED in different paces. The red LED is blinking every 0.5s, the green is 2s, and the blue is 3s.

Your task in this assignment is to implement an app that receives commands from UART and run corresponding exercises above. The commands are detailed as follows:
* ‘O’: App starts ready to receive commands.
* ‘1’: App runs exercises 1.
* ‘2’: App runs exercises 2.
* ‘3’: App runs exercises 3.
* ‘4’: App runs exercises 4.
* ‘5’: App runs exercises 5.
* ‘F’: App stops any running.

#### Hardware
* Raspberry Pi 3 model B
* USB TTL serial cable
* 3 RGB LEDs (common anode)
* Button
* 1k registors
* Wires

![alt text](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/master/images/Lab2IoT_bb.jpg)

#### Running
Connect USB TTL serial cable (UART USB) with Raspberry Pi and USB port on PC. You can use TeraTerm on Window or Minicom on Linux to setting up the use of serial port. The baud rate for serial port is 115200.
