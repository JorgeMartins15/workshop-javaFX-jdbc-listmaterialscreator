package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Components;
import model.services.ComponentsService;
import model.services.MachineService;

public class ComponentsListController implements Initializable, DataChangeListener {

	private ComponentsService service;

	@FXML
	private TableView<Components> tableViewComponents;

	@FXML
	private TableColumn<Components, Integer> tableColumnId;

	@FXML
	private TableColumn<Components, String> tableColumnQuantity;

	@FXML
	private TableColumn<Components, String> tableColumnDescription;
	
	@FXML
	private TableColumn<Components, String> tableColumnCode;
	
	@FXML
	private TableColumn<Components, String> tableColumnProvider1;
	
	@FXML
	private TableColumn<Components, String> tableColumnProvider2;

	@FXML
	private TableColumn<Components, Components> tableColumnEDIT;

	@FXML
	private TableColumn<Components, Components> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Components> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Components obj = new Components();
		createDialogForm(obj, "/gui/ComponentsForm.fxml", parentStage);
	}

	public void ComponentsService(ComponentsService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializedNodes();
	}

	private void initializedNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		tableColumnCode.setCellValueFactory(new PropertyValueFactory<>("code"));
		tableColumnProvider1.setCellValueFactory(new PropertyValueFactory<>("provider1"));
		tableColumnProvider2.setCellValueFactory(new PropertyValueFactory<>("provider2"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewComponents.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Components> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewComponents.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

 	private void createDialogForm(Components obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ComponentsFormController controller = loader.getController();
			controller.setComponents(obj);
			controller.setServices(new ComponentsService(), new MachineService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Components data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		}

		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Components, Components>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Components obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/ComponentsForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Components, Components>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Components obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Components obj) {
		Optional <ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure delete?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getLocalizedMessage(), AlertType.ERROR);
			}
		}
	}

}
