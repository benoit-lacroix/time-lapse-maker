package io.github.radixhomework.timelapsemaker.events;

import io.github.radixhomework.timelapsemaker.enums.EnumOutputFormat;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pivot.wtk.*;

import java.io.File;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ChooseTargetEvent implements ButtonPressListener {

    private final Window parent;
    private final TextInput targetDisplay;

    @Setter
    private String selectedOutputFormat;

    @Override
    public void buttonPressed(Button button) {
        final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
        fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_AS);
        if (!targetDisplay.getText().isEmpty()) {
            fileBrowserSheet.setSelectedFile(new File(targetDisplay.getText()));
        }
        fileBrowserSheet.open(ChooseTargetEvent.this.parent, sheet -> {
            if (fileBrowserSheet.getSelectedFile() != null) {
                EnumOutputFormat outputFormat = EnumOutputFormat.findByLabel(selectedOutputFormat);
                File outputFile = fileBrowserSheet.getSelectedFile();

                // Check if extension already appended to output filename
                if (!outputFile.getName().endsWith(Objects.requireNonNull(outputFormat).getExtension())) {
                    outputFile = new File(outputFile.getAbsolutePath().concat(outputFormat.getExtension()));
                }

                log.info("Setting output file to {}", outputFile.getAbsolutePath());
                ChooseTargetEvent.this.targetDisplay.setText(outputFile.getAbsolutePath());
            }
        });
    }
}
