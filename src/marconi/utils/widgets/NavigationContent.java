/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marconi.utils.widgets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import marconi.utils.ActionNavigation;

/**
 *
 * @author Felipe
 */
public class NavigationContent extends ListView implements ActionNavigation.ActionNavigationContent {

    private static final String CLASS_NAVIGATION_CONTENT = "navigation-content";
    private ActionNavigation actionNavigation;

    public NavigationContent() {
        this.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new NavigationContentAdapater();
            }
        });
        this.getStyleClass().add(CLASS_NAVIGATION_CONTENT);
        this.getStylesheets().add(getClass().getResource("styles/navigation_content.css").toExternalForm());
    }

    public void addItemList(String icon, String title, Class<?> activity) {
        this.getItems().add(new NavigationContentRow(icon, title, activity));
    }
    public void addItemList(String icon, String title, EventHandler<ActionEvent> action) {
        this.getItems().add(new NavigationContentRow(icon, title, action));
    }

    @Override
    public void setActionNavigation(ActionNavigation actionNavigation) {
        this.actionNavigation = actionNavigation;
    }

    private class NavigationContentAdapater extends ListCell<NavigationContentRow> {

        private NavigationContentHolder holder;

        @Override
        protected void updateItem(NavigationContentRow item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (holder == null) {
                    holder = new NavigationContentHolder(item.pathSVG, item.title);
                } else {
                    holder.setIcon(item.pathSVG);
                    holder.setTitle(item.title);
                }
                setGraphic(holder);
                holder.setOnMouseClicked((MouseEvent event) -> {
                    try {
                        if (item.activity != null) {
                            Node node = (Node) item.activity.newInstance();
                            actionNavigation.changeMainContent(node);
                        }else if(item.action!=null){
                            item.action.handle(new ActionEvent(holder,null));
                        }
                        
                        actionNavigation.slideNav();
                    } catch (InstantiationException | IllegalAccessException ex) {
                        Logger.getLogger(NavigationContent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    event.consume();
                });
            }
        }
    }

    private class NavigationContentHolder extends AnchorPane {

        @FXML
        private SVGPath icon;
        @FXML
        private Label title;

        public NavigationContentHolder(String pathSVG, String title) {
            try {
                FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("layout/navigation_row.fxml"));
                fXMLLoader.setController(this);
                fXMLLoader.setRoot(this);
                fXMLLoader.load();
            } catch (IOException ex) {
                Logger.getLogger(NavigationContent.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.icon.setContent(pathSVG);
            this.icon.setFill(Color.rgb(0, 0, 0, 0.5));
            this.title.setText(title);
        }

        public void setIcon(String icon) {
            this.icon.setContent(icon);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

    }

    private class NavigationContentRow {

        protected String pathSVG;
        protected String title;
        protected Class<?> activity;
        protected EventHandler<ActionEvent> action;

        public NavigationContentRow(String pathSVG, String title, Class<?> activity) {
            this.pathSVG = pathSVG;
            this.title = title;
            this.activity = activity;
        }

        public NavigationContentRow(String pathSVG, String title, EventHandler<ActionEvent> action) {
            this.pathSVG = pathSVG;
            this.title = title;
            this.action = action;
        }
    }

}
