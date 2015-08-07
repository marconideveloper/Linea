/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils.widgets;

import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 *
 * @author felipe
 */
public class EditText extends StackPane {
    
    private final SimpleObjectProperty<TextInputControl> textInputProperty;
    private final Label hint;
    private static final StyleablePropertyFactory<EditText> STYLEABLE_PROPERTY_FACTORY = new StyleablePropertyFactory<>(StackPane.getClassCssMetaData());
    private final StyleableProperty<Color> baselineColor, baselineFocusColor;
    private final int TOP_PADDING = 20;
    private final int DURATION_ANIMATION = 150;
    private final int PREF_WIDTH = 200;
    private final int TEXTINPUT_INDEX = 0;
    
    private final Color HINT_COLOR = Color.web("#999");
    private final double BASELINE_STROKE = 1;
    private final double BASELINE_STROKE_FOCUSED = 2;
    private final double topPaddingContents;
    
    private String key;
    private final Scale  hintScale;
    
    public EditText() {
        getStyleClass().add("edit-text");
        baselineColor = STYLEABLE_PROPERTY_FACTORY.createStyleableColorProperty(this, "baselineColor", "-fx-baseline-color", (EditText t) -> t.baselineColor);
        baselineFocusColor = STYLEABLE_PROPERTY_FACTORY.createStyleableColorProperty(this, "baselisneFocusColor", "-fx-baseline-focus-color", (EditText t) -> t.baselineFocusColor);
        setPrefWidth(PREF_WIDTH);
        setMinWidth(USE_PREF_SIZE);
        
        hint = new Label();
        hint.setTextFill(HINT_COLOR);
        hint.setMouseTransparent(true);
        hintScale=new Scale(1,1, 0, 0);
        hint.getTransforms().add(hintScale);
        
        topPaddingContents = TOP_PADDING;
        Line baseline = new Line(0, 0, 0, 0);
        baseline.strokeProperty().bind(baselineColorProperty());
        baseline.setStrokeWidth(BASELINE_STROKE);
        Line baselineFocused = new Line(0, 0, 0, 0);
        baselineFocused.setOpacity(0);
        baselineFocused.strokeProperty().bind(baselineFocusColorProperty());
        baselineFocused.setStrokeWidth(BASELINE_STROKE_FOCUSED);
        
        StackPane.setAlignment(baseline, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(baselineFocused, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(hint, Pos.TOP_LEFT);
        getChildren().addAll(baseline, baselineFocused, hint);
        StackPane.setMargin(hint, new Insets(TOP_PADDING + 10, 0, 0, 5));
        textInputProperty = new SimpleObjectProperty<>();
        baseline.endXProperty().bind(this.widthProperty());
        createAnimation(baselineFocused);
        this.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            baselineFocused.setEndX(newValue.doubleValue());
        });
        addEditTex(new TextField());
    }
    
     private void createAnimation(Line baseLineFocused) {
        textInputProperty.addListener((ObservableValue<? extends TextInputControl> observable, TextInputControl oldValue, TextInputControl newValue) -> {
            if (newValue != null) {
                newValue.focusedProperty().addListener((ObservableValue<? extends Boolean> observable1, Boolean oldValue1, Boolean newValueFocus) -> {
                    Timeline animation = new Timeline();
                    hint.textFillProperty().unbind();
                    
                    if (newValueFocus) {
                        baseLineFocused.setOpacity(1);
                        KeyFrame floating = new KeyFrame(new Duration(DURATION_ANIMATION), new KeyValue(baseLineFocused.endXProperty(), this.widthProperty().getValue()));
                        if(textInputProperty.getValue().getText().length()==0){
                        KeyFrame floatingLabel = new KeyFrame(new Duration(DURATION_ANIMATION), new KeyValue(hint.translateYProperty(), -topPaddingContents),
                            new KeyValue(hintScale.xProperty(),0.75),
                            new KeyValue(hintScale.yProperty(),0.75),
                            new KeyValue(hint.translateXProperty(),-5),
                            new KeyValue(hint.textFillProperty(), baselineColor.getValue()));
                        animation.getKeyFrames().add(floatingLabel);
                        }
                        animation.getKeyFrames().add(floating);
                        
                    } else {
                        if(textInputProperty.getValue().getText().length()==0){
                         KeyFrame reposeLabel = new KeyFrame(new Duration(DURATION_ANIMATION), new KeyValue(hint.translateYProperty(), 0),
                            new KeyValue(hintScale.xProperty(),1),
                            new KeyValue(hintScale.yProperty(),1),
                            new KeyValue(hint.translateXProperty(),0),
                            new KeyValue(hint.textFillProperty(),HINT_COLOR));
                        animation.getKeyFrames().add(reposeLabel);
                        }
                        
                        KeyFrame repose = new KeyFrame(new Duration(DURATION_ANIMATION), new KeyValue(baseLineFocused.endXProperty(), 0));
                        animation.getKeyFrames().add(repose);
                        animation.setOnFinished((ActionEvent event) -> {
                            baseLineFocused.setOpacity(0);
                        });
                    }
                    animation.play();
                });
            }
        });
        
    }
    
    public final ObservableValue<Color> baselineColorProperty() {
        return (ObservableValue<Color>) baselineColor;
    }
    
    public final void setBaselineColor(Color color) {
        baselineColor.setValue(color);
    }
    
    public final ObservableValue<Color> baselineFocusColorProperty() {
        return (ObservableValue<Color>) baselineFocusColor;
    }
    
    public final void setBaselineFocusColor(Color color) {
        baselineFocusColor.setValue(color);
    }
    
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return STYLEABLE_PROPERTY_FACTORY.getCssMetaData();
    }

    /*Se agrega el control al contenedor */
    private void addEditTex(TextInputControl node) {
        textInputProperty.setValue(node);
        StackPane.setAlignment(node, Pos.CENTER);
        StackPane.setMargin(node, new Insets(TOP_PADDING, 0, 5, 0));
        getChildren().add(TEXTINPUT_INDEX, node);
        
    }

    /**
     * Habilita el EditText como PasswordField
     *
     * @param action true lo habilita, false lo establece como TextField
     * preterminado
     */
    public void setPasswordField(boolean action) {
        if (action == true) {
            getChildren().remove(TEXTINPUT_INDEX);
            addEditTex(new PasswordField());
        } else {
            getChildren().remove(TEXTINPUT_INDEX);
            addEditTex(new TextField());
        }
    }
    
    public boolean getPasswordField() {
        return textInputProperty.getValue() instanceof PasswordField;
    }

    /**
     * Habilita el EditText como TextArea
     *
     * @param action true lo habilita, false lo establece como TextField
     * preterminado
     */
    public void setTextArea(boolean action) {
        if (action) {
            getChildren().remove(TEXTINPUT_INDEX);
            TextArea textArea = new TextArea();
            textArea.setPrefHeight(90);
            textArea.setMinHeight(90);
            addEditTex(textArea);
            
        } else {
            getChildren().remove(TEXTINPUT_INDEX);
            addEditTex(new TextField());
        }
    }
    
    public boolean getTextArea() {
        return textInputProperty.getValue() instanceof TextArea;
    }
    
    public void setHint(String hint) {
        this.hint.setText(hint);
    }
    
    public String getHint() {
        return this.hint.getText();
    }
    
    public String getText() {
        
        return textInputProperty.getValue().getText();
    }
    
    public void setText(String text) {
        if(text.length()>0 && !textInputProperty.getValue().isFocused()){
            hintScale.setX(0.75);
            hintScale.setY(0.75);
            hint.setTranslateX(-5);
            hint.setTranslateY(-topPaddingContents);
            hint.textFillProperty().bind(baselineColorProperty());
        }else if(text.length()<=0 && !textInputProperty.getValue().isFocused()){
            hintScale.setX(1);
            hintScale.setY(1);
            hint.setTranslateX(0);
            hint.setTranslateY(0);
            hint.setTextFill(HINT_COLOR);
        }
       textInputProperty.getValue().setText(text);
        
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
    
    public StringProperty textProperty() {
        return textInputProperty.getValue().textProperty();
    }
}
