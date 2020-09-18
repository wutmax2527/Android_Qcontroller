package th.co.infinitecorp.www.qcontroller.GPIO;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GPIOService {
    // GPIOs value file path
    public static final String OUTPUT_EN = "/sys/class/gpio/gpio976/value";
    public static final String OUTPUT_1 = "/sys/class/gpio/gpio999/value";
    public static final String OUTPUT_2 = "/sys/class/gpio/gpio934/value";//934
    public static final String OUTPUT_3 = "/sys/class/gpio/gpio947/value";//947
    public static final String OUTPUT_4 = "/sys/class/gpio/gpio1007/value";
    public static final String INPUT_1 = "/sys/class/gpio/gpio1005/value";
    public static final String INPUT_2 = "/sys/class/gpio/gpio1008/value";
    public static final String INPUT_3 = "/sys/class/gpio/gpio979/value";
    public static final String INPUT_4 = "/sys/class/gpio/gpio1000/value";
    public static final String OUT_LED_1 = "/sys/class/gpio/gpio911/value";
    public static final String OUT_LED_2 = "/sys/class/gpio/gpio969/value";
    public static final String OUT_LED_3 = "/sys/class/gpio/gpio1010/value";
    public static final String USB_PWR_EN = "/sys/class/gpio/gpio927/value";
    public static final String USB_SW = "/sys/class/gpio/gpio980/value";

    public static void Init()
    {
        WriteGPIO(OUT_LED_1, 0);
        WriteGPIO(OUT_LED_2, 0);
        WriteGPIO(OUT_LED_3, 0);
    }
    public static void ON_LED_1()
    {
        WriteGPIO(OUT_LED_1, 1);
    }
    public static void OFF_LED_1()
    {
        WriteGPIO(OUT_LED_1, 0);
    }
    public static void ON_LED_2()
    {
        WriteGPIO(OUT_LED_2, 1);
    }
    public static void OFF_LED_2()
    {
        WriteGPIO(OUT_LED_2, 0);
    }
    public static void ON_LED_3()
    {
        WriteGPIO(OUT_LED_3, 1);
    }
    public static void OFF_LED_3()
    {
        WriteGPIO(OUT_LED_3, 0);
    }

    public static void HIGH_OUTPUT_1()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_1, 1);
    }
    public static void LOW_OUTPUT_1()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_1, 0);
    }
    public static void HIGH_OUTPUT_2()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_2, 1);
    }
    public static void LOW_OUTPUT_2()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_2, 0);
    }
    public static void HIGH_OUTPUT_3()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_3, 1);
    }
    public static void LOW_OUTPUT_3()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_3, 0);
    }
    public static void HIGH_OUTPUT_4()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_4, 1);
    }
    public static void LOW_OUTPUT_4()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_4, 0);
    }

    public static void Enable_Uart_OutPort1()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_1, 0);
        WriteGPIO(OUTPUT_2, 0);
    }
    public static void Enable_Uart_OutPort2()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_1, 1);
        WriteGPIO(OUTPUT_2, 0);
    }
    public static void Enable_Uart_QSound()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_1, 0);
        WriteGPIO(OUTPUT_2, 1);
    }
    public static void Enable_Uart_QPrint()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_1, 1);
        WriteGPIO(OUTPUT_2, 1);
    }

    public static void HIGH_SEND_RS485()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_3, 1);
    }
    public static void LOW_RECEIVE_RS485()
    {
        WriteGPIO(OUTPUT_EN, 0);
        WriteGPIO(OUTPUT_3, 0);
    }

    public static int ReadGPIO(String gpioname) {
        int val = 0;

        try {
            File file = new File(gpioname);
            FileInputStream fis = new FileInputStream(file);
            byte buf[] = new byte[8];
            fis.read(buf);
            fis.close();
            if (buf[0] == '1') {
                val = 1;
            } else {
                val = 0;
            }
        } catch (IOException e) {
            Log.e("Read GPIO", "Error: " + e.toString());
        }
        return val;
    }
    public static void WriteGPIO(String gpioname, int val) {
        try {
            File file = new File(gpioname);
            FileOutputStream fos = new FileOutputStream(file);
            byte bytes;
            if (val == 0)
                bytes = '0';
            else
                bytes = '1';
            fos.write(bytes);
            fos.close();
            //Log.i("Write GPIO",  "gpio=" + gpio + ", val=" + bytes);
        } catch (IOException e) {
            Log.e("Write GPIO", "Error: " + e.toString());
        }
    }
}
