
import java.util.HashMap;
import javafx.fxml.FXML;
import marconi.utils.Activity;
import marconi.utils.Bundle;
import marconi.utils.widgets.SlidingTabLayout;
import marconi.utils.widgets.ToolBar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Felipe
 */
public class SalesActivity extends Activity{
    @FXML
    ToolBar toolbar;
    @FXML 
    SlidingTabLayout tab;
    @Override
    public void onCreate(Bundle saveInstanceState) {
        setContentView("layout/sales_activity.fxml");
        setActionBar(toolbar);
        HashMap data=new HashMap();
        data.put("tab",tab);
        tab.addTab("Viaje", SelectTravelFragment.class,data);
        tab.addTab("Asientos", SelectSeatsFragment.class,data);
    }
    
}
