package utils;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author felipe
 */
  public  final class Bus extends Rectangle{
        private EventHandler<ActionEvent> seatEvent;
        
        public void setOnSeatChange(EventHandler<ActionEvent> evt){
            this.seatEvent=evt;
        }
        public EventHandler<ActionEvent> getOnSeatChange(){
            return this.seatEvent;
        }
        
    }
