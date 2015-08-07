package utils;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Felipe
 */
public class SeatShape extends StackPane {
    
    private final SimpleBooleanProperty selectedProperty;
    private final SimpleObjectProperty<Paint> color;
    private final SimpleObjectProperty<Paint> colorStroke;
    private int type=0;
            
    public static int columns,rows=BusPlace.NUM_ROWS;
    private Bus bus;
    
    public SeatShape(int numSeat,DoubleProperty hgap,Bus bus) {
        this.bus=bus;
        this.color=new SimpleObjectProperty<>(Color.TRANSPARENT);
        this.colorStroke=new SimpleObjectProperty<>(Color.LIGHTGRAY);
        this.selectedProperty = new SimpleBooleanProperty(false);
        Rectangle body = new Rectangle();
        Rectangle back = new Rectangle();
        body.fillProperty().bind(color);
        back.fillProperty().bind(color);
        body.setStrokeWidth(2);
        back.setStrokeWidth(2);
        back.strokeProperty().bind(colorStroke);
        body.strokeProperty().bind(colorStroke);
        body.widthProperty().bind((bus.widthProperty().divide(columns)).subtract(hgap.add(8)));
        body.heightProperty().bind((bus.heightProperty().divide(rows + 1)).subtract(8));
        body.setArcHeight(6);
        body.setArcWidth(6);
        back.setArcHeight(6);
        back.setArcWidth(6);
        back.widthProperty().bind(body.widthProperty().divide(4));
        back.heightProperty().bind(body.heightProperty());

        StackPane.setAlignment(back, Pos.CENTER_RIGHT);
        Text numSeatText=new Text(numSeat+"");
        numSeatText.fillProperty().bind(Bindings.when(selectedProperty).then(Color.WHITE).otherwise(Color.BLACK));
        StackPane.setAlignment(numSeatText, Pos.CENTER_LEFT);
        StackPane.setMargin(numSeatText, new Insets(0, 0, 0, 8));
        this.getChildren().addAll(body, back,numSeatText);

        selectedProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            Timeline timeline = new Timeline(new KeyFrame(new Duration(350), 
                    new KeyValue(color, newValue ? Color.web("3F51B5") : Color.TRANSPARENT),
                    new KeyValue(colorStroke,  newValue ? Color.web("FFF") : Color.LIGHTGRAY)
            ));
            timeline.play();
        });
    }

    public void toogle() {
        if (selectedProperty.getValue()) {
            selectedProperty.set(false);
        } else {
            selectedProperty.set(true);
        }
        if(bus.getOnSeatChange()!=null){
        bus.getOnSeatChange().handle(new ActionEvent(this, null));
        }
    }
    
    public boolean isSelected(){
        return selectedProperty.getValue();
    } 

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SimpleBooleanProperty getSelectedProperty() {
        return selectedProperty;
    }
    
    
    
   
}
