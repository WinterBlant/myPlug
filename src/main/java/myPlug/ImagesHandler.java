package myPlug;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ImagesHandler {

    private MimetypesFileTypeMap types;

    ImagesHandler() {
        types = new MimetypesFileTypeMap();
    }

    String getRandomImage(String folder, String lastImage) {
        if (folder.isEmpty()) {
            return null;
        }
        List<String> images = new ArrayList<>();
        collectImages(images, folder);
        int count = images.size();
        if (count == 0) {
            return null;
        }
        Random randomGenerator = new Random();
        String image;
        do {
            int index = randomGenerator.nextInt(images.size());
            image = images.get(index);
        } while (lastImage != null && lastImage.split(",")[0].equals(image));
        return image;
    }

    private void collectImages(List<String> images, String folder) {
        File root = new File(folder);
        if (!root.exists()) {
            return;
        }
        File[] list = root.listFiles();
        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                collectImages(images, f.getAbsolutePath());
            } else {
                if (!isImage(f)) {
                    continue;
                }
                images.add(f.getAbsolutePath());
            }
        }
    }

    private boolean isImage(File file) {
        String[] parts = types.getContentType(file).split("/");
        return parts.length != 0 && "image".equals(parts[0]);
    }

}
