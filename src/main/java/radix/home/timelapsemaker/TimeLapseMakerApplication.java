package radix.home.timelapsemaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import radix.home.timelapsemaker.prefs.PreferencesManager;
import radix.home.timelapsemaker.utils.Messages;

@SpringBootApplication(scanBasePackages = "radix.home")
public class TimeLapseMakerApplication extends Application {

    private static ConfigurableApplicationContext context;
    @Autowired
    private PreferencesManager preferences;
    @Autowired
    private Messages messages;
    private Parent root;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TimeLapseMakerApplication.class);
        springApplication.setBannerMode(Banner.Mode.LOG);
        context = springApplication.run();
        launch(TimeLapseMakerApplication.class, args);
    }

    /**
     * Actions done before {@link TimeLapseMakerApplication#start(Stage)}
     */
    @Override
    public void init() throws Exception {
        context.getAutowireCapableBeanFactory().autowireBean(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main_gui.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        root = fxmlLoader.load();
    }

    /**
     * Start the application
     *
     * @param primaryStage Main stage
     * @throws Exception Any exception while loading application
     */
    @Override
    public void start(Stage primaryStage) {
        preferences.initPreferences();
        primaryStage.setTitle(messages.getValue("about.application.name"));
        primaryStage.setScene(new Scene(root, 600, 200));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/radixhome.png"));
        primaryStage.show();
    }

    /**
     * Actions done when the application stops
     *
     * @throws Exception Any exception while stopping application
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        context.close();
    }
}
