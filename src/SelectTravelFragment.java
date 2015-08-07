
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import marconi.utils.Fragment;
import marconi.utils.widgets.FloatingButton;
import marconi.utils.widgets.SlidingTabLayout;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Felipe
 */
public class SelectTravelFragment extends Fragment {

    @FXML
    private VBox leftPane, rightPane;
    @FXML
    private FlowPane flowPane;
    @FXML
    private FloatingButton next;
    @FXML
    private DatePicker date;
    @FXML
    private final ObservableList<LocalDate> hoursTravel = FXCollections.observableArrayList();
    @FXML
    private ListView<LocalDate> hoursTravelList;

    @Override
    public void onCreateView(FXMLLoader layoutInflater) {
        try {
            layoutInflater.setLocation(Main.class.getResource("layout/select_travel_fragment.fxml"));
            layoutInflater.load();
        } catch (IOException ex) {
            Logger.getLogger(SelectTravelFragment.class.getName()).log(Level.SEVERE, null, ex);
        }
        initResponsiveDesign();
        next.setOnAction((ActionEvent event) -> {

            if (getArguments().get("tab") != null && getArguments().get("tab") instanceof SlidingTabLayout) {
                ((SlidingTabLayout) getArguments().get("tab")).changeTab(1);

            }

        });

        hoursTravelList.setCellFactory(new Callback<ListView<LocalDate>, ListCell<LocalDate>>() {

            @Override
            public ListCell<LocalDate> call(ListView<LocalDate> param) {
                return new ListCell<LocalDate>() {

                    @Override
                    protected void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            setText(item.getMonth().name());

                        } else {
                            setText(null);
                        }
                    }

                };
            }
        });
//        hoursTravelList.setItems(hoursTravel);

        date.setOnAction((ActionEvent evt) -> {
            hoursTravelList.getItems().add(date.getValue());
        });

    }

    @Override
    public void onResume() {
        next.show();
    }

    private void initResponsiveDesign() {
        flowPane.parentProperty().addListener((ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) -> {
            if (newValue != null) {
                Pane parent = (Pane) newValue;
                flowPane.prefWidthProperty().bind(parent.widthProperty());
                leftPane.prefWidthProperty().bind(Bindings
                        .when(parent.widthProperty().greaterThan(720))
                        .then((parent.widthProperty().divide(2)).subtract(32))
                        .otherwise(parent.widthProperty().subtract(32)));
                rightPane.prefWidthProperty().bind(Bindings
                        .when(parent.widthProperty().greaterThan(720))
                        .then((parent.widthProperty().divide(2)).subtract(32))
                        .otherwise(parent.widthProperty().subtract(32)));
            }
        });

    }
}
