package io.github.radixhomework.timelapsemaker.services;

import io.github.radixhomework.timelapsemaker.utils.GuiUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Meter;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class TimeLapseTaskListener implements TaskListener<Void> {

    private final Meter progressBar;
    private final Label status;
    private final List<Component> components;

    @Override
    public void taskExecuted(Task<Void> task) {
        GuiUtils.updateProgressBar(progressBar, 0);
        GuiUtils.updateLabel(status, "Done");
        GuiUtils.changeComponentsState(components, true);
    }

    @Override
    public void executeFailed(Task<Void> task) {
        GuiUtils.updateProgressBar(progressBar, 0);
        GuiUtils.updateLabel(status, "Error");
        GuiUtils.changeComponentsState(components, true);
    }
}
