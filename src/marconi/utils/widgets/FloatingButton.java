/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils.widgets;

import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

/**
 *
 * @author felipe
 */
public class FloatingButton extends StackPane {

    private final Rectangle circle;
    private SVGPath icon;
    private int actionButton;

    private final int BODY_INDEX = 0;
    private final double DEFAULT_MARGIN = 16;

    private final int ICON_INDEX = 1;

    private String iconPath;

    private final StyleableProperty<Color> background;
    private static final StyleablePropertyFactory<FloatingButton> STYLEABLE_PROPERTY_FACTORY = new StyleablePropertyFactory<>(StackPane.getClassCssMetaData());

    public FloatingButton() {

        setPrefWidth(USE_COMPUTED_SIZE);
        setPrefHeight(USE_COMPUTED_SIZE);

        circle = new Rectangle();
        circle.setWidth(56);
        circle.setHeight(56);
        circle.arcWidthProperty().bind(circle.widthProperty());
        circle.arcHeightProperty().bind(circle.heightProperty());
        circle.setEffect(new DropShadow(16d, 0d, 7.5d, Color.web("#000000", 0.3d)));
        getChildren().add(BODY_INDEX, circle);

        StackPane.setAlignment(this, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(this, new Insets(DEFAULT_MARGIN));

        getStyleClass().add("floating-button");

        background = STYLEABLE_PROPERTY_FACTORY.createStyleableColorProperty(this, "background", "-fx-background", (FloatingButton fb) -> fb.background);
        circle.fillProperty().bind((ObservableValue<Color>) background);
    }
//    public ObservableValue<Color> backgroundProperty(){
//        return  (ObservableValue<Color>) background;
//    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return STYLEABLE_PROPERTY_FACTORY.getCssMetaData();
    }

    /**
     * Metodo para agregar un icono al FloatingButton
     *
     * @param icon contiene la imagen del icono
     */
    public void setIcon(String icon) {
        try {
            this.iconPath = icon;
            this.icon = new SVGPath();
            this.icon.setFill(Color.WHITE);
            this.icon.setMouseTransparent(true);
            this.icon.setContent(icon);
            if (getChildren().size() > ICON_INDEX) {
                getChildren().remove(ICON_INDEX);
            }

        } catch (IndexOutOfBoundsException ex) {
            System.err.println(ex.getMessage());
        } finally {
            getChildren().add(ICON_INDEX, this.icon);
        }
    }

    /**
     * Metodo para obtener el icono del FloatingButtom
     *
     * @return el icono del FloatingButton
     */
    public String getIcon() {
        return iconPath;
    }

    public void fillAnimation(Color colorTo) {
        Timeline fillTransition = new Timeline(new KeyFrame(new Duration(350), new KeyValue(background, colorTo)));
        fillTransition.play();
    }

    public void rotateAnimation(double value) {
        Timeline trasition = new Timeline(new KeyFrame(new Duration(350), new KeyValue(icon.rotateProperty(), value)));
        trasition.play();
    }

    /**
     * Metodo para asignar un tipo de accion cuando se presione el
     * FloatingButton
     *
     * @param action una constante que indica que tipo de accion tendra el boton
     *
     */
    public void setActionButton(int action) {
        actionButton = action;
    }

    /**
     *
     * @return la accion que realiza el FloatingButton
     */
    public int getActionButton() {
        return actionButton;
    }

    /**
     * Metodo para ejecutar la animacion de ocultar el FloatingButton
     *
     */
    public void hide() {
        ScaleTransition scaleTransition = new ScaleTransition(new Duration(250), this);
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);
        scaleTransition.play();
    }

    /**
     * Metodo para ejecutar la animacion de mostrar el FloatingButton
     *
     */
    public void show() {
        ScaleTransition scaleTransition = new ScaleTransition(new Duration(250), this);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();
    }

    public void setOnAction(EventHandler<ActionEvent> evt) {

        circle.setOnMouseClicked((MouseEvent event) -> {
            ScaleTransition scaleTransition = new ScaleTransition(new Duration(250), this);
            scaleTransition.setToX(0);
            scaleTransition.setToY(0);
            scaleTransition.play();
            scaleTransition.setOnFinished((ActionEvent event1) -> {
                evt.handle(new ActionEvent(this, null));

            });

        });
    }
}
