package com.pinpinbox.android.Widget;


import android.graphics.Color;

/**
 * Created by vmage on 2017/5/12.
 */
public class OperaColor{


    /**
     * * Returns the HEX value representing the colour in the default sRGB
     * ColorModel. * * @return the HEX value of the colour in the default sRGB
     * ColorModel
     */
    public String getHex(int r, int g, int b) {
        return toHex(r,g,b);
    }

    /**
     * * Returns a web browser-friendly HEX value representing the colour in the
     * default sRGB * ColorModel. * * @param r red * @param g green * @param b
     * blue * @return a browser-friendly HEX value
     */
    public static String toHex(int r, int g, int b) {
        return "#" + toBrowserHexValue(r) + toBrowserHexValue(g)
                + toBrowserHexValue(b);
    }

    private static String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(
                Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }


    public static int toColorFromString(String colorStr) {


        colorStr = colorStr.substring(1);

        int color = Integer.parseInt(colorStr, 16);

        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);

        color = Color.argb(160,red,green,blue);

        //java.awt.Color[r=0,g=0,b=255]
        return color;
    }


}
