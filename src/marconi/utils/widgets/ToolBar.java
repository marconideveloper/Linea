/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils.widgets;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import marconi.utils.StackActivityTransition;

/**
 *
 * @author Felipe
 */
public class ToolBar extends javafx.scene.control.ToolBar{
    private final RippleButton homeButton;
    private StackActivityTransition stack;
    public ToolBar() {
        setMinHeight(56);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 0d);
        homeButton=new RippleButton();
    }
    
    public void setStackActivityTransition(StackActivityTransition stack){
       this.stack=stack;
       getItems().add(homeButton);
       initHomeButton();
    }
    public StackActivityTransition getStackActivityTrasition(){
        return stack;
    }
    private void initHomeButton() {
        homeButton.getStyleClass().add("action-item");
        homeButton.toggled(true);
        homeButton.setOnAction((ActionEvent evt) -> {
            if (stack.stackSizeProperty().get() > 1) {
                stack.back();
            } else {
                
            }
            evt.consume();
        });

        SVGPath home = new SVGPath(), leftArrow = new SVGPath();
        home.setContent("M3,6H21V8H3V6M3,11H21V13H3V11M3,16H21V18H3V16Z");
        home.setFill(Color.web("#FFF"));
        home.setScaleX(1.2);
        home.setScaleY(1.2);
        leftArrow.setContent("M20,11V13H8L13.5,18.5L12.08,19.92L4.16,12L12.08,4.08L13.5,5.5L8,11H20Z");
        leftArrow.setFill(Color.web("#FFF"));
        leftArrow.setScaleX(1.2);
        leftArrow.setScaleY(1.2);
        leftArrow.setOpacity(0);
        leftArrow.setRotate(-180);

        homeButton.createDefaultSkin();
        ButtonSkin buttonSkin = homeButton.getButtonSkin();
        buttonSkin.getChildren().addAll(leftArrow, home);
        DoubleBinding layoutX = new DoubleBinding() {
            {
                bind(homeButton.widthProperty(), home.boundsInLocalProperty());
            }

            @Override
            protected double computeValue() {
                return (homeButton.getWidth() / 2) - ((home.getBoundsInLocal().getWidth() * 1.2) / 2);
            }
        };
        DoubleBinding layoutY = new DoubleBinding() {
            {
                bind(homeButton.heightProperty(), home.boundsInLocalProperty());
            }

            @Override
            protected double computeValue() {
                return (homeButton.getHeight() / 2) - ((home.getBoundsInLocal().getHeight()));
            }
        };
        home.layoutXProperty().bind(layoutX);
        home.layoutYProperty().bind(layoutY);
        leftArrow.layoutXProperty().bind(layoutX);
        leftArrow.layoutYProperty().bind(layoutY);

        stack.stackSizeProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Timeline homeButtonMorphing = new Timeline();
            KeyFrame frame1, frame2;
            if (newValue.intValue() > 1) {
                frame1 = new KeyFrame(new Duration(200), new KeyValue(leftArrow.rotateProperty(), 0),
                        new KeyValue(leftArrow.opacityProperty(), 1),
                        new KeyValue(home.rotateProperty(), 180)
                );
                frame2 = new KeyFrame(new Duration(80),
                        new KeyValue(home.opacityProperty(), 0)
                );
            } else {
                frame1 = new KeyFrame(new Duration(200), new KeyValue(leftArrow.rotateProperty(), -180),
                        new KeyValue(leftArrow.opacityProperty(), 0),
                        new KeyValue(home.rotateProperty(), 0)
                );
                frame2 = new KeyFrame(new Duration(80),
                        new KeyValue(home.opacityProperty(), 1)
                );
            }
            homeButtonMorphing.getKeyFrames().addAll(frame1, frame2);
            homeButtonMorphing.play();
        });
    }
}
