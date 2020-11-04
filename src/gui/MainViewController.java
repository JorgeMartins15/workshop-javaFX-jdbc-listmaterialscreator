package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemMachine;
	
	@FXML
	private MenuItem menuItemComponents;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemMachineAction() {
		System.out.println("onMenuItemMachineAction");
	}
	
	@FXML
	public void onMenuItemComponentsAction() {
		System.out.println("onMenuItemComponentsAction");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		System.out.println("onMenuItemAboutAction");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}

}
