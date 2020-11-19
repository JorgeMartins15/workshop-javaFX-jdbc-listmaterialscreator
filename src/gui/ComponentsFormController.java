package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Components;
import model.entities.Machine;
import model.exceptions.ValidationException;
import model.services.ComponentsService;
import model.services.MachineService;

public class ComponentsFormController implements Initializable {

	private Components entity;

	private ComponentsService service;

	private MachineService machineService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtQuantity;

	@FXML
	private TextField txtDescription;

	@FXML
	private TextField txtCode;

	@FXML
	private TextField txtProvider1;

	@FXML
	private TextField txtProvider2;

	@FXML
	private ComboBox<Machine> comboBoxMachine;

	@FXML
	private Label labelErrorQuantity;

	@FXML
	private Label labelErrorDescription;

	@FXML
	private Label labelErrorCode;

	@FXML
	private Label labelErrorProvider1;

	@FXML
	private Label labelErrorProvider2;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Machine> obsList;

	public void setComponents(Components entity) {
		this.entity = entity;
	}

	public void setServices(ComponentsService service, MachineService machineService) {
		this.service = service;
		this.machineService = machineService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}

		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Components getFormData() {
		Components obj = new Components();
		ValidationException exception = new ValidationException("Validation Error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setQuantity(Utils.tryParseToInt(txtQuantity.getText()));

		if (txtQuantity.getText() == null || txtQuantity.getText().trim().equals("")) {
			exception.addError("quantity", "Field can't be empty");
		}

		if (txtDescription.getText() == null || txtDescription.getText().trim().equals("")) {
			exception.addError("description", "Field can't be empty");
		}
		obj.setDescription(txtDescription.getText());

		if (txtCode.getText() == null || txtCode.getText().trim().equals("")) {
			exception.addError("code", "Field can't be empty");
		}
		obj.setCode(txtCode.getText());

		if (txtProvider1.getText() == null || txtProvider1.getText().trim().equals("")) {
			exception.addError("provider1", "Field can't be empty");
		}
		obj.setProvider1(txtProvider1.getText());

		if (txtProvider2.getText() == null || txtProvider2.getText().trim().equals("")) {
			exception.addError("provider2", "Field can't be empty");
		}
		obj.setProvider2(txtProvider2.getText());
		
		obj.setMachine(comboBoxMachine.getValue());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldInteger(txtQuantity);
		Constraints.setTextFieldMaxLength(txtDescription, 30);
		Constraints.setTextFieldMaxLength(txtCode, 12);
		Constraints.setTextFieldMaxLength(txtProvider1, 30);
		Constraints.setTextFieldMaxLength(txtProvider2, 30);
		
		initializeComboBoxMachine();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtQuantity.setText(String.valueOf(entity.getQuantity()));
		txtDescription.setText(entity.getDescription());
		txtCode.setText(entity.getCode());
		txtProvider1.setText(entity.getProvider1());
		txtProvider2.setText(entity.getProvider2());
		if (entity.getMachine() == null) {
			comboBoxMachine.getSelectionModel().selectFirst();
		}
		else {
			comboBoxMachine.setValue(entity.getMachine());
		}
	}

	public void loadAssociatedObjects() {
		if (machineService == null) {
			throw new IllegalStateException("MachineService was null");
		}
		List<Machine> list = machineService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxMachine.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

	
		labelErrorQuantity.setText((fields.contains("quantity") ? errors.get("quantity") : ""));
		labelErrorDescription.setText((fields.contains("description") ? errors.get("description") : ""));
		labelErrorCode.setText((fields.contains("code") ? errors.get("code") : ""));
		labelErrorProvider1.setText((fields.contains("provider1") ? errors.get("provider1") : ""));
		labelErrorProvider2.setText((fields.contains("provider2") ? errors.get("provider2") : ""));
		
	}

	private void initializeComboBoxMachine() {
		Callback<ListView<Machine>, ListCell<Machine>> factory = lv -> new ListCell<Machine>() {
			@Override
			protected void updateItem(Machine item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxMachine.setCellFactory(factory);
		comboBoxMachine.setButtonCell(factory.call(null));
	}

}
