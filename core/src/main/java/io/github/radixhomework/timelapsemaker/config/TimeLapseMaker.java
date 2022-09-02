package io.github.radixhomework.timelapsemaker.config;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

import java.io.IOException;

public class TimeLapseMaker implements Application {
    private Window window;

    @Override
    public void startup(Display display, Map<String, String> properties) throws IOException, SerializationException {
        BXMLSerializer bxmlSerializer = new BXMLSerializer();
        window = (Window) bxmlSerializer.readObject(this.getClass(), "/views/main.bxml");
        window.setMaximumWidth(600);
        window.setMaximumHeight(200);
        window.setMaximized(true);
        window.setIcon(this.getClass().getResource("/radixhome.png"));
        window.open(display);
    }

    @Override
    public boolean shutdown(boolean optional) {
        if (window != null) {
            window.close();
        }
        return false;
    }

    @Override
    public void suspend() {
        // Nothing to do
    }

    @Override
    public void resume() {
        // Nothing to do
    }
}
