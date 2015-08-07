/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils.widgets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Felipe
 */
public class HeaderIcon extends StackPane {
 
    private Circle base;
    private Label text;

    private final int BASE_INDEX = 0;
    private final int TEXT_INDEX = 1;
    private final String patternStr = "^\\d{1,4}$";

    public HeaderIcon(String title) {
        base = new Circle(50);
        
        base.setFill(Color.web("#03A9F4"));
     
        text = new Label();

        String[] initialWord = title.split(" ");
        StringBuffer initialWords = new StringBuffer();
        for (int i = 0; i < initialWord.length; i++) {

            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(initialWord[i]);
            if (matcher.matches()) {
            
                initialWords.append(String.valueOf(initialWord[i]));
            } else {
                if (initialWord[i].length() > 3) {
                    {
                        initialWords.append(String.valueOf(initialWord[i].charAt(0)).toUpperCase());
                    }
                }

            }

        }
        text.setText(initialWords.toString());
        text.setId("header-item-text");
        text.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if ((base.getRadius() * 2) < (newValue.doubleValue() + 16)) {
                    double scale = ((base.getRadius() * 2)/(newValue.doubleValue() + 16) );
                   
                    text.setScaleX(scale);
                    text.setScaleY(scale);

//                }
                }
//                    base.setRadius((newValue.doubleValue() / 2) + 5);
//                }
            }
        });

        getChildren().add(BASE_INDEX, base);
        getChildren().add(TEXT_INDEX, text);
    }

    public Circle getBase() {
        return base;
    }

    
}
