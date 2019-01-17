package myPlug;

import myPlug.ui.Settings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.wm.impl.IdeBackgroundUtil;
import java.io.File;

public class RandomBackgroundTask implements Runnable {

    private ImagesHandler imagesHandler;

    public RandomBackgroundTask() {
        imagesHandler = new ImagesHandler();
    }

    @Override
    public void run() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String folder = prop.getValue(Settings.FOLDER);
        if (folder == null || folder.isEmpty()) {
            NotificationCenter.notice("Image folder not set");
            return;
        }
        File file = new File(folder);
        if (!file.exists()) {
            NotificationCenter.notice("Image folder not set");
            return;
        }
        String lastImage = prop.getValue(IdeBackgroundUtil.EDITOR_PROP);
        String image = imagesHandler.getRandomImage(folder, lastImage);
        if (image == null) {
            NotificationCenter.notice("No image found");
            return;
        }
        if (image.contains(",")) {
            NotificationCenter.notice("Intellij wont load images with ',' character\n" + image);
        }
        prop.setValue(IdeBackgroundUtil.FRAME_PROP, null);
        prop.setValue(IdeBackgroundUtil.EDITOR_PROP, image);
        // NotificationCenter.notice("Image: " + image.replace(folder + File.separator, ""));
        IdeBackgroundUtil.repaintAllWindows();
    }

}
