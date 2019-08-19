package Radar_Simulator;

import AI_ATC.AI_ATC;
import ATC_Core.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import jdk.nashorn.internal.objects.Global;
import sun.awt.Mutex;

import java.util.List;
import java.util.Random;

/**
 * Created by saija on 2017-03-28.
 */
public class Radar {
    private Sector sector;
    private AircraftBuilder aircraftBuilder;

    private GridPane gridPane;
    private RadarView radarView;
    private AircraftInformationPane aircraftInformationPane;
    private OptionsPane optionsPane;

    public Mutex mutex;
    private Thread radarSimulationThread;
    private boolean radarThreadStop;
    private long time;

    private AI_ATC aiATC;

    public Radar(Sector sector, GridPane gridPane) {
        this.sector = sector;
        this.gridPane = gridPane;

        aircraftBuilder = new AircraftBuilder();
        createAircrafts();

        aiATC = new AI_ATC();

        createATCInterface();
        addEventListeners();

        startRadarThread();
    }

    public void createATCInterface() {
        // create radar view
        radarView = new RadarView(sector);
        radarView.setAlignment(Pos.TOP_LEFT);
        radarView.setPrefSize(RadarView.DEFAULT_WIDTH, RadarView.DEFAULT_HEIGHT);
        radarView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        radarView.prefWidthProperty().bind(radarView.getAircraftsView().prefHeightProperty());

        GridPane.setVgrow(radarView, Priority.ALWAYS);

        // create aircraft information pane
        aircraftInformationPane = new AircraftInformationPane();
        //aircraftInformationPane.setAlignment(Pos.TOP_RIGHT);
        aircraftInformationPane.setPrefSize(AircraftInformationPane.DEFAULT_IMAGE_WIDTH,
                AircraftInformationPane.DEFAULT_IMAGE_HEIGHT);

        GridPane.setHgrow(aircraftInformationPane, Priority.ALWAYS);
        GridPane.setVgrow(aircraftInformationPane, Priority.ALWAYS);

        optionsPane = new OptionsPane();
        GridPane.setHgrow(optionsPane, Priority.ALWAYS);

        gridPane.add(radarView, 0, 0, 1, 2);
        gridPane.add(aircraftInformationPane, 1 ,0);
        gridPane.add(optionsPane, 1, 1);
    }

    private void addAircraftImageViewActionListeners() {
        // update aircraft information pane with aircraft data
        AircraftsView aircraftsView = radarView.getAircraftsView();
        List<AircraftImageView> aircraftImages = aircraftsView.getAircraftImages();
        for (AircraftImageView imgView : aircraftImages) {
            imgView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    aircraftInformationPane.update(imgView.getAircraft());
                }
            });
        }
    }

    private void addEventListeners() {
        // Redraw aeronautical paths when size changes
        radarView.getAeronauticalPathsView().heightProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                radarView.getAeronauticalPathsView().drawAeronauticalPaths();
            }
        });

        addAircraftImageViewActionListeners();

        // change time
        optionsPane.getTimeComboBox().valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                mutex.lock();
                time = secondsToMilliSeconds(newValue.doubleValue());
                mutex.unlock();
            }
        });

        optionsPane.getResetButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mutex.lock();
                sector.clearAircrafts();
                radarView.getAircraftsView().clearAircraftsView();
                aircraftInformationPane.clear();
                mutex.unlock();

                stopRadarThread();
            }
        });

        optionsPane.getAddAircraftsButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // if flights are added in the middle of simulation, lock the radar thread
                if (!radarThreadStop) {
                    mutex.lock();
                }

                // add aircrafts
                createAircrafts();

                // re-draw aircraft images
                List<Aircraft> aircrafts = sector.getAircrafts();
                List<AircraftImageView> aircraftImages = radarView.getAircraftsView().getAircraftImages();
                if (aircrafts.size() != aircraftImages.size()) {
                    radarView.getAircraftsView().clearAircraftsView();
                    radarView.getAircraftsView().drawAirPlanes(aircrafts);
                }

                // add action listeners to the aircraft images
                addAircraftImageViewActionListeners();

                if (radarThreadStop) {
                    // start radar thread if it is stopped
                    startRadarThread();
                }
                else {
                    // unlock radar thread
                    mutex.unlock();
                }
            }
        });
    }

    private long secondsToMilliSeconds(double seconds) {
        return (long) (seconds * 1000);
    }

    public class RadarSimulationThread implements Runnable {
       @Override
        public void run() {
            while (!radarThreadStop) {

                try {
                    radarSimulationThread.sleep(time);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                    System.exit(-1);
                }

                mutex.lock();
                moveAircrafts();
                getBestPlan();
                mutex.unlock();
            }
        }
    }

    public void moveAircrafts() {
        List<Aircraft> aircrafts = sector.getAircrafts();
        List<AircraftImageView> aircraftImages = radarView.getAircraftsView().getAircraftImages();

        for (int i = 0; i < aircrafts.size(); i++) {
            Aircraft aircraft = aircrafts.get(i);
            Path currentPath = aircraft.getCurrentPath();

            // when aircraft reaches the end of its current path
            Point3D aircraftPosition = aircraft.getPosition();
            Point2D pathPoint = currentPath.getPathPoints().get(currentPath.getPathPoints().size() - 1);
            if (aircraftPosition.getX() == pathPoint.getX() && aircraftPosition.getY() == pathPoint.getY()) {
                FlightRoute flightRoute = aircraft.getFlightRoute();
                int index = flightRoute.indexOf(currentPath);

                if (index != flightRoute.getSize() - 1) {
                    // change current path to next path
                    // change current path
                    index++;
                    Path nextPath = aircraft.getFlightRoute().getPath(index);
                    aircraft.setCurrentPath(nextPath);

                    // change heading
                    aircraft.getVector().setHeading(nextPath.getDirection());
                } else {
                    // remove aircraft from sector that has reached the end of its route
                    removeAircraft(aircraft);
                    i--;
                }
            }
        }

        for (int i = 0; i < aircrafts.size(); i++) {
            // change positions of aircraft and its image representation
            double scaleFactor = radarView.getHeight() / RadarView.DEFAULT_HEIGHT;

            Aircraft aircraft = aircrafts.get(i);
            AircraftImageView aircraftImageView = aircraftImages.get(i);
            Vector vector = aircraft.getVector();

            double lastKiloCheckPoint = aircraft.getLastKiloCheckPoint();
            lastKiloCheckPoint += Utilities.kphToMps(vector.getMagnitude());

            if (lastKiloCheckPoint > Globals.METERS_PER_PIXEL) {
                // each pixel represents a kilometer

                int pixel = (int)(lastKiloCheckPoint / Globals.METERS_PER_PIXEL);
                // scaled pixel size
                double pixelScaled = pixel * scaleFactor;
                // aircraft heading
                double heading = vector.getHeading();

                Point3D position = aircraft.getPosition();

                // actual position
                double x = position.getX();
                double y = position.getY();

                // scale position
                double xScaled = position.getX() * scaleFactor;
                double yScaled = position.getY() * scaleFactor;

                if (heading == 0 || heading == 360) {
                    aircraft.moveAircraft(new Point3D(x + pixel, y, position.getZ())); // move east
                    aircraftImageView.setX(xScaled + pixelScaled);
                    aircraftImageView.setY(yScaled);
                }
                else if (heading == 90) {
                    aircraft.moveAircraft(new Point3D(x, y - pixel, position.getZ())); // move north
                    aircraftImageView.setX(xScaled);
                    aircraftImageView.setY(yScaled - pixelScaled);
                }
                else if (heading == 180) {
                    aircraft.moveAircraft(new Point3D(x - pixel, y, position.getZ())); // move west
                    aircraftImageView.setX(xScaled - pixelScaled);
                    aircraftImageView.setY(yScaled);
                }
                else if (heading == 270) {
                    aircraft.moveAircraft(new Point3D(x, y + pixel, position.getZ())); // move south
                    aircraftImageView.setX(xScaled);
                    aircraftImageView.setY(yScaled + pixelScaled);
                }
                else if (heading > 0 && heading < 90) {
                    aircraft.moveAircraft(new Point3D(x + pixel, y - pixel, position.getZ())); // move northeast
                    aircraftImageView.setX(xScaled + pixelScaled);
                    aircraftImageView.setY(yScaled - pixelScaled);
                }
                else if (heading > 90 && heading < 180) {
                    aircraft.moveAircraft(new Point3D(x - pixel, y - pixel, position.getZ())); // move northwest
                    aircraftImageView.setX(xScaled - pixelScaled);
                    aircraftImageView.setY(yScaled - pixelScaled);
                }
                else if (heading > 180 && heading < 270) {
                    aircraft.moveAircraft(new Point3D(x - pixel, y + pixel, position.getZ())); // move southwest
                    aircraftImageView.setX(xScaled - pixelScaled);
                    aircraftImageView.setY(yScaled + pixelScaled);
                }
                else {
                    aircraft.moveAircraft(new Point3D(x + pixel, y + pixel, position.getZ())); // move southeast
                    aircraftImageView.setX(xScaled + pixelScaled);
                    aircraftImageView.setY(yScaled + pixelScaled);
                }

                lastKiloCheckPoint = lastKiloCheckPoint - (pixel * Globals.METERS_PER_PIXEL);
            }

            // update distance since last kilometer
            aircraft.setLastKiloCheckPoint(lastKiloCheckPoint);
        }
    }


    private void removeAircraft(Aircraft aircraft) {
        sector.removeAircraft(aircraft);
        radarView.getAircraftsView().removeAircraftImage(aircraft);
    }

    private void createAircrafts() {
        Random rand = new Random();
        int numOfAircrafts = 5;
        List<Aircraft> aircrafts = aircraftBuilder.getAircrafts(sector.getPaths(), numOfAircrafts);

        for (Aircraft aircraft : aircrafts) {
            sector.addAircraft(aircraft);
        }
    }

    public void cleanup() {
        // stop radar view thread
        mutex.lock();
        stopRadarThread();
        mutex.unlock();

        try {
            radarSimulationThread.join();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void startRadarThread() {
        time = secondsToMilliSeconds(optionsPane.getTimeComboBox().getValue().doubleValue());
        if (mutex == null)
            mutex = new Mutex();
        radarThreadStop = false;
        radarSimulationThread = new Thread(new RadarSimulationThread());
        radarSimulationThread.start();
    }

    private void stopRadarThread() {
        radarThreadStop = true;
    }

    public void getBestPlan() {
        // connect to artificial intelligence
        // evaluate current sector
        if (aiATC.evaluateSector(sector) > 0) {
            // get best sector with plan
            sector = aiATC.getBestSector(sector);
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    radarView.getAircraftsView().clearAircraftsView();
                    radarView.getAircraftsView().drawAirPlanes(sector.getAircrafts());
                    addAircraftImageViewActionListeners();
                }
            });
        }
    }
}
