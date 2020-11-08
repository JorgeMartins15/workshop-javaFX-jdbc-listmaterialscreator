package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.MachineServices;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemMachine;
	
	@FXML
	private MenuItem menuItemComponents;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemMachineAction() {
		loadView2("/gui/MachineList.fxml");
	}
	
	@FXML
	public void onMenuItemComponentsAction() {
		System.out.println("onMenuItemComponentsAction");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	private void loadView (String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
		}
		catch(IOException e ){
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
			
		}
	}
	
	private void loadView2 (String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			MachineListController controller = loader.getController();
			controller.setMachineService(new MachineServices());
			controller.updateTableView();
			
		}
		catch(IOException e ){
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
			
		}
	}

}
