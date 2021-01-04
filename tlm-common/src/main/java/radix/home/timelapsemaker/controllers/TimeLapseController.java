package radix.home.timelapsemaker.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Meter;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;
import radix.home.timelapsemaker.enums.EnumFrameRate;
import radix.home.timelapsemaker.enums.EnumOutputFormat;
import radix.home.timelapsemaker.events.ChooseSourceEvent;
import radix.home.timelapsemaker.events.ChooseTargetEvent;
import radix.home.timelapsemaker.events.SelectOutputFormatEvent;
import radix.home.timelapsemaker.services.TimeLapseTask;
import radix.home.timelapsemaker.services.TimeLapseTaskListener;
import radix.home.timelapsemaker.utils.GuiUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TimeLapseController extends Window implements Bindable {

    private static final List<Component> buttons = new ArrayList<>();
    @BXML
    private TextInput sourceDirectory;
    @BXML
    private PushButton chooseSource;
    @BXML
    private TextInput outputFile;
    @BXML
    private PushButton chooseTarget;
    @BXML
    private ListButton outputFormats;
    @BXML
    private ListButton frameRates;
    @BXML
    private TableView tableView;
    @BXML
    private Label status;
    @BXML
    private Meter progressBar;
    @BXML
    private PushButton make;
    @BXML
    private PushButton exit;

    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
        // Filling components list to disable during time lapse assembling
        buttons.add(sourceDirectory);
        buttons.add(chooseSource);
        buttons.add(outputFile);
        buttons.add(chooseTarget);
        buttons.add(outputFormats);
        buttons.add(frameRates);
        buttons.add(tableView);
        buttons.add(make);
        buttons.add(exit);

        // Filling output formats ant frame rates drop-downs
        outputFormats.setListData(EnumOutputFormat.getValues());
        outputFormats.setSelectedItem(EnumOutputFormat.DEFAULT_VALUE);

        frameRates.setListData(EnumFrameRate.getValues());
        frameRates.setSelectedItem(EnumFrameRate.DEFAULT_VALUE);

        // Adding events on buttons and drop-downs
        chooseSource.getButtonPressListeners()
                .add(new ChooseSourceEvent(TimeLapseController.this, sourceDirectory, tableView));

        ChooseTargetEvent chooseTargetEvent = new ChooseTargetEvent(TimeLapseController.this, outputFile,
                (String) outputFormats.getSelectedItem());
        chooseTarget.getButtonPressListeners().add(chooseTargetEvent);

        outputFormats.getListButtonSelectionListeners().add(new SelectOutputFormatEvent(TimeLapseController.this,
                outputFile, chooseTargetEvent));

        exit.getButtonPressListeners().add(button -> System.exit(0));

        make.getButtonPressListeners().add(button -> {
            if (sourceDirectory.getText().isEmpty()) {
                Alert.alert(MessageType.ERROR, "Source directory cannot be empty", TimeLapseController.this);
            } else if (outputFile.getText().isEmpty()) {
                Alert.alert(MessageType.ERROR, "Destination file cannot be empty", TimeLapseController.this);
            } else {
                GuiUtils.changeComponentsState(buttons, false);
                TimeLapseTask task = new TimeLapseTask(sourceDirectory.getText(), outputFile.getText(), progressBar,
                        EnumFrameRate.getByLabel((String) frameRates.getSelectedItem()), status);
                task.execute(new TimeLapseTaskListener(progressBar, status, buttons));
            }
        });
    }
}
