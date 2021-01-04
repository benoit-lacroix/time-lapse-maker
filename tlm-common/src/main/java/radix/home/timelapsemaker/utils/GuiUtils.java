package radix.home.timelapsemaker.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Meter;
import org.apache.pivot.wtk.TextInput;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiUtils {

    public static void updateProgressBar(Meter progressBar, double percentage) {
        log.debug("Updating meter {} to {}", progressBar.getName(), percentage);
        ApplicationContext.queueCallback(() -> progressBar.setPercentage(percentage));
    }

    public static void updateLabel(Label label, String value) {
        log.debug("Updating label {} to {}", label.getName(), value);
        ApplicationContext.queueCallback(() -> label.setText(value));
    }

    public static void updateTextInput(TextInput input, String value) {
        log.debug("Updating text input {} to {}", input.getName(), value);
        ApplicationContext.queueCallback(() -> input.setText(value));
    }

    public static void changeComponentsState(List<Component> components, boolean enable) {
        String action = enable ? "Enabling" : "Disabling";
        components.forEach(component -> ApplicationContext.queueCallback(() -> {
            log.debug("{} component {}", action, component.getName());
            component.setEnabled(enable);
        }));
    }
}
