package ATC_Core;

/**
 * Created by saija on 2017-04-12.
 */
public class Globals {
    // aircraft global information
    public final static String[] AIRCRAFT_NAMES = {"EK 445", "Speed Bird", "9W 119"};
    public final static String[] AIRCRAFT_MODELS = {"Boeing 777", "Boeing 747-8", "Boeing 787"};
    public final static double[] MAX_CRUISING_SPEEDS = {908, 918, 908};
    public final static double[] MAX_CRUISING_ALTITUDE = {43100, 43100, 43000};
    public final static double[] FUEL_CAPACITY = {47890, 63034, 33528};

    // minimum altitude separation in feet
    public final static double MINIMUM_VERTICAL_SEPARATION = 1000;
    public final static double MINIMUM_HORIZONTAL_SEPARATION = 30380.6;

    // area of evaluation in kilometers
    public final static double EVAL_WIDTH = 25;
    public final static double EVAL_HEIGHT = 25;
    public final static double EVAL_DEPTH = 25;

    // sector min and max altitude in feet
    public final static double SECTOR_MAX_ALTITUDE = 80000;
    public final static double SECTOR_MIN_ALTITUDE = 30000;

    public final static double METERS_PER_PIXEL = 1000;
}
