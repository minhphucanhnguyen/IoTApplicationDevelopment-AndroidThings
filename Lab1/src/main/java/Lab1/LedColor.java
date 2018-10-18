package Lab1;

import com.google.android.things.pio.Gpio;

import java.io.IOException;

public class LedColor {
    public void setColor(Gpio redPin, int red, Gpio greenPin, int green, Gpio bluePin, int blue) {
        try {
            redPin.setValue(red > 0 ? false : true);
            greenPin.setValue(green > 0 ? false : true);
            bluePin.setValue(blue > 0 ? false : true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
