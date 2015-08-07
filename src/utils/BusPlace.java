/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author felipe
 */
public class BusPlace extends StackPane {

    private final GridPane gridPane = new GridPane();
    int row = 0, columns = 0, countSeats = 0;
    public static final int NUM_ROWS = 4;
    private final Bus bus = new Bus();
    private final ArrayList<SeatShape> seatsList;

    public BusPlace() {
        seatsList = new ArrayList<>();
        bus.setArcHeight(8);
        bus.setArcWidth(8);
        bus.setFill(Color.TRANSPARENT);
        bus.setStrokeWidth(2);
        bus.setWidth(500);
        bus.setHeight(200);
        bus.setStyle("-fx-stroke:-color-accent");
        gridPane.setAlignment(Pos.CENTER);
        StackPane.setAlignment(gridPane, Pos.CENTER);
        this.getChildren().addAll(bus, gridPane);
    }

    public void initSeats(int numSeats, int numColumns) {
        row = 0;
        columns = 0;
        countSeats = 0;
        
        SeatShape.columns = numColumns;
        gridPane.hgapProperty().unbind();
        gridPane.hgapProperty().bind(bus.widthProperty().divide(numColumns).divide(8));
        
        seatsList.clear();
        gridPane.getChildren().clear();
        
        while (columns < numColumns) {
            while (row < NUM_ROWS + 1) {
                if (countSeats < numSeats) {
                    SeatShape seatsShape = new SeatShape(countSeats + 1, gridPane.hgapProperty(), bus);
                    if (row != 2) {
                        countSeats++;
                        seatsList.add(seatsShape);
                    } else {
                        seatsShape.setVisible(false);
                    }
                    seatsShape.setOnMouseClicked((MouseEvent evt) -> {
                        ((SeatShape) evt.getSource()).toogle();
                    });
                    gridPane.add(seatsShape, columns, row);
                }

                row++;
            }
            row = 0;
            columns++;
        }

    }

    public ArrayList<SeatShape> getSeatsList() {
        return seatsList;
    }

    public void setOnSeatChange(EventHandler<ActionEvent> evt) {
        bus.setOnSeatChange(evt);
    }

    public void syncSeats(BusPlace bus) {
        for (int i = 0; i < bus.getSeatsList().size(); i++) {
            seatsList.get(i).getSelectedProperty().bind(bus.getSeatsList().get(i).getSelectedProperty());
        }
    }

}
