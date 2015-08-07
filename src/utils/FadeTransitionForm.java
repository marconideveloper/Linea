/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.util.Duration;

/**
 *
 * @author felipe
 */
public class FadeTransitionForm {

    private static final Duration DURATION = new Duration(250);
    private final Timeline startAnimation, revertAnimation;

    public FadeTransitionForm(Node in, Node out, MenuItem start, Node revert) {
        in.setOpacity(0);
        in.setMouseTransparent(true);

        startAnimation = new Timeline(new KeyFrame(DURATION,
                new KeyValue(in.opacityProperty(), 1),
                new KeyValue(out.opacityProperty(), 0)
        ));
        revertAnimation = new Timeline(new KeyFrame(DURATION,
                new KeyValue(in.opacityProperty(), 0),
                new KeyValue(out.opacityProperty(), 1)
        ));
        
        start.addEventHandler(ActionEvent.ACTION, getStartAnimation(in, out));
        revert.addEventHandler(ActionEvent.ACTION, getRevertAnimation(in, out));

    }

    private EventHandler<ActionEvent> getStartAnimation(Node in, Node out) {
        return (ActionEvent event) -> {
            out.setMouseTransparent(true);
            in.setMouseTransparent(false);
            startAnimation.play();
        };
    }
    private EventHandler<ActionEvent> getRevertAnimation(Node in, Node out) {
        return (ActionEvent event) -> {
            out.setMouseTransparent(false);
            in.setMouseTransparent(true);
            revertAnimation.play();
        };
    }

}
