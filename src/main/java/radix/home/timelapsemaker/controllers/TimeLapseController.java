package radix.home.timelapsemaker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import radix.home.timelapsemaker.enums.EnumFrameRate;
import radix.home.timelapsemaker.prefs.EnumPreferences;
import radix.home.timelapsemaker.prefs.PreferencesManager;
import radix.home.timelapsemaker.services.TimeLapseService;

import java.io.File;

@Slf4j
@Component
public class TimeLapseController {
    
    @Autowired
    private TimeLapseService timeLapseService;
    
    @Autowired
    private PreferencesManager preferences;
    
    @FXML
    private ProgressBar progressBar;
    
    @FXML
    private TextField sourceDirectory;
    
    @FXML
    private TextField destinationFile;
    
    @FXML
    private ChoiceBox<String> frameRate;
    
    @FXML
    private Label status;
    
    @FXML
    public void initialize() {
        sourceDirectory.setText(preferences.getPreference(EnumPreferences.SOURCE_DIR));
        for (EnumFrameRate item : EnumFrameRate.values()) {
            frameRate.getItems().add(item.getLabel());
        }
        frameRate.setValue(EnumFrameRate.FR_24.getLabel());
    }
    
    @FXML
    protected void selectSource(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);
        if (directory != null) {
            sourceDirectory.setText(directory.getPath());
        }
    }
    
    @FXML
    protected void selectDestination(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MPEG4", "*.mp4"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            destinationFile.setText(file.getPath());
        }
    }
    
    @FXML
    protected void make(ActionEvent event) {
        if (sourceDirectory == null || sourceDirectory.getText().isEmpty() || destinationFile == null || destinationFile.getText().isEmpty()) {
            log.error("Cannot start building time lapse with source or out file empty");
            status.setText("Error");
        } else {
            preferences.savePreference(EnumPreferences.SOURCE_DIR, sourceDirectory.getText());
            timeLapseService.make(sourceDirectory.getText(), destinationFile.getText(), progressBar,
                    EnumFrameRate.getByLabel(frameRate.getValue()), status);
        }
    }
    
}
