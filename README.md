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
* 1k resistors
* Wires

![alt text](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/master/images/Lab1IoT_bb.jpg)

Schema of [Lab 1](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/tree/master/Lab1)

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
* 1k resistors
* Wires

![alt text](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/master/images/Lab2IoT_bb.jpg)

Schema of [Lab 2](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/tree/master/Lab2)

#### Running
Connect USB TTL serial cable (UART USB) with Raspberry Pi and USB port on PC. You can use TeraTerm on Window or Minicom on Linux to setting up the use of serial port. The baud rate for serial port is 115200.

### Lab 3
Create an Android application running on Raspberry Pi 3 to :
1. Write blocks of data to an RFID card including
* Name, DOB, Student ID
* Authenticate if possible
2. Continuously read an UID and the written data from an RFID card and display this information on a screen.
3. Turn the green LED of an RGB LED on when the card of one of your group members is presented.
4. Flashing the red LED of an RGB LED 5 times in 2 seconds when the other cards are presented.
5. By default, the blue LED of an RGB LED is turned on, and only one LED is turned on at a time.

#### Hardware
* Raspberry Pi 3 model B
* Module RFID RC522
* 13.56 mHz RFID cards
* 1 RGB LED (common anode)
* 1k resistor
* Wires
* HDMI Display or using [Vysor](https://www.vysor.io/) to display wirelessly.

#### Running
Because task 1 relates to Writing to a RFID card and the others relate to Reading a RFID card so the lab is divided into 2 sub-projects: [Lab3_WriteRFID](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/tree/master/Lab3_WriteRFID) & [Lab3_ReadRFID](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/tree/master/Lab3_ReadRFID)

#### Note
A RFID card is devided into 16 sectors, each sector has 4 blocks of data. Each block is composed of 16 bytes. Consequently, each card has 64 blocks, from number 0 to 63.

In each secctor, the last block (blocks number 3, 7, 11, ...) is called the sector trailer. This block stores the 2 keys or passwords and controls the access to the rest of the blocks in the sector (e.g block number 7 controls blocks 4, 5, 6). The first 6 bytes (0 .. 5) are the key A, the last 5 bytes (10 .. 15) are the key B. As we will see, before reading or writing in any block, we must first authenticate us to that sector by providing one of the two keys. The bytes number 6, 7 and 8 store the control bytes, which control the way the blocks may be accessed (read/write). Byte number 9 is not defined in all sector trailers.

The very first block (number 0) is called the manufacturer block and has special characteristics. The first 4 bytes (0 .. 3) are the unique identification (UID). The UID can be seen as the serial number of the card and identify the card in a univocal way. The next byte (number 4) is the CRC check byte so it can be used to check the correct read; it is calculated with the XOR of the 4-byte UID. The rest of the bytes in the block number 0 (bytes 5 .. 15) are the manufacturer data. This block 0 has read-only access for security reasons. We need to authenticate before reading the block number 0 but the UID can be obtained anyway, without the key.

The rest of the blocks (1, and 2; 4, 5 and 6; 8, 9 and 10; etc) are knowns as data blocks and they are available to read and write operations, after the needed authentication process.

| RC522 Pin | RPi Pin name | RPi Pin number |
| --- | --- | --- |
| SDA | BCM8, SS0 | 24 |
| SCK | BCM11, SCKL | 23 |
| MOSI | BCM10, MOSI | 19 |
| MISO | BCM9, MISO | 21 |
| GND | GND | 20 |
| RST | BCM25 | 22 |
| 3.3V | 3.3V | 1 |

![alt text](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/master/images/Lab3IOT_WriteRFID_bb.jpg)

Schema of [Lab3_WriteRFID](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/tree/master/Lab3_WriteRFID)

![alt text](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/master/images/Lab3IOT_ReadRFID%20_bb.jpg)

Schema of [Lab3_ReadRFID](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/tree/master/Lab3_ReadRFID)

### Lab 4
Combine the assignment of Lab 3 that implements MFRC522 with the provided project to:
1. Read the UID of an RFID card, turn on/off LEDs correspondingly.
2. Send the UID to a server.

The syntax to send data is as follows: http://demo1.chipfc.com/SensorValue/update?sensorid=7&sensorvalue=[UID]. 
Please note that the UID is a unique value that are read from an RFID card.

You can check the results in this webpage: http://demo1.chipfc.com/SensorValue/List/7

#### Hardware
* Raspberry Pi 3 model B
* Module RFID RC522
* 13.56 mHz RFID cards
* 1 RGB LED (common anode)
* 1k resistor
* Wires
* HDMI Display or using [Vysor](https://www.vysor.io/) to display wirelessly.

![alt_text](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/blob/master/images/Lab4IOT_bb.jpg)

Schema of [Lab 4](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/tree/master/Lab4)

#### Running
The lab project is put in folder [Lab4](https://github.com/minhphucanhnguyen/IoTApplicationDevelopment-AndroidThings/tree/master/Lab4).
