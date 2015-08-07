/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils.widgets;

import com.sun.javafx.scene.control.skin.TableCellSkin;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 *
 * @author Felipe
 * @param <Object> //
 * @param <String>
 */
public  class RippleTableCell<Object, String> extends TableCell<Object, String> {

    private final FadeTransition fade;
    final private double DEFAULT_DURATION = 350;
    private final Circle ripple;
    private TableCellSkin tableCellSkin;
    protected ObservableList skin;

    public RippleTableCell() {
        fade = new FadeTransition(new Duration(DEFAULT_DURATION), this);
        fade.setFromValue(0);
        fade.setToValue(1);
        ripple = new Circle(36, Color.YELLOW);
        disableProperty().bind(emptyProperty());
        this.createRippleEffect();
        
    }

    @Override
    public void startEdit() {
        super.startEdit();
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty); 
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            setText(item.toString());
        }
    }
    
    @Override
    protected Skin<?> createDefaultSkin() {

        tableCellSkin = new TableCellSkin(this);
        tableCellSkin.getChildren().add(ripple);
        tableCellSkin.getChildren().addListener((ListChangeListener.Change c) -> {
            ObservableList observable = c.getList();
            if (observable.indexOf(ripple) == -1) {
                observable.add(0, ripple);
            }
        });
        return tableCellSkin;
      }

    public final void createRippleEffect() {
        ripple.getStyleClass().add("ripple-circle");
        ripple.setScaleX(0);
        ripple.setScaleY(0);
        ripple.setOpacity(0.5);
        FadeTransition fade = new FadeTransition(new Duration(500), ripple);
        fade.setToValue(0);
        ScaleTransition scale = new ScaleTransition(new Duration(250), ripple);
        scale.setToX(3);
        scale.setToY(3);
        ParallelTransition rippleEffect = new ParallelTransition(fade, scale);
        rippleEffect.setInterpolator(Interpolator.EASE_OUT);
        rippleEffect.setOnFinished((ActionEvent event) -> {
            ripple.setOpacity(0.5);
            ripple.setScaleX(0);
            ripple.setScaleY(0);
        });
        
        setOnMouseClicked((MouseEvent event) -> {
            
            ripple.setCenterX(event.getX());
            ripple.setCenterY(event.getY());
            rippleEffect.play();
        });
    }

}
