package io.github.radixhomework.timelapsemaker.events;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.ListButtonSelectionListener;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;
import io.github.radixhomework.timelapsemaker.enums.EnumOutputFormat;
import io.github.radixhomework.timelapsemaker.utils.GuiUtils;

@Slf4j
@AllArgsConstructor
public class SelectOutputFormatEvent implements ListButtonSelectionListener {

    private final Window parent;
    private final TextInput targetDisplay;
    private final ChooseTargetEvent chooseTargetEvent;

    @Override
    public void selectedIndexChanged(ListButton listButton, int previousSelectedIndex) {
        // Nothing to do (handled in selectedItemChanged method)
    }

    @Override
    public void selectedItemChanged(ListButton listButton, Object previousSelectedItem) {
        EnumOutputFormat oldFormat = EnumOutputFormat.findByLabel((String) previousSelectedItem);
        EnumOutputFormat newFormat = EnumOutputFormat.findByLabel((String) listButton.getSelectedItem());
        String newOutputFile = targetDisplay.getText().replace(oldFormat.getExtension(), newFormat.getExtension());
        GuiUtils.updateTextInput(targetDisplay, newOutputFile);
        chooseTargetEvent.setSelectedOutputFormat(newFormat.getLabel());
    }
}
