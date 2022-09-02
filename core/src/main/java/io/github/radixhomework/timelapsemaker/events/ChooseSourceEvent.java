package io.github.radixhomework.timelapsemaker.events;

import io.github.radixhomework.timelapsemaker.beans.Image;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ChooseSourceEvent implements ButtonPressListener {

    private final Window parent;
    private final TextInput sourceDisplay;
    private final TableView tableView;

    @Override
    public void buttonPressed(Button button) {
        final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
        fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_TO);
        if (!sourceDisplay.getText().isEmpty()) {
            fileBrowserSheet.setSelectedFile(new File(sourceDisplay.getText()));
        }
        fileBrowserSheet.open(ChooseSourceEvent.this.parent,
                sheet -> {
                    if (fileBrowserSheet.getSelectedFile() != null) {
                        log.info("Loading images from directory {}", fileBrowserSheet.getSelectedFile().getAbsolutePath());
                        tableView.clear();
                        List<Image> imageViewList = new ArrayList<>();
                        ChooseSourceEvent.this.sourceDisplay.setText(fileBrowserSheet.getSelectedFile().getAbsolutePath());
                        Arrays.stream(Objects.requireNonNull(fileBrowserSheet.getSelectedFile().listFiles()))
                                .sorted()
                                .forEach(file -> imageViewList.add(new Image(file.getPath())));
                        tableView.setTableData(imageViewList);
                    }
                });
    }
}
