package ecocampus.gui;


import ecocampus.Preconditions;
import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents an OSM tile manager
 *
 */
public class TileManager {
    private Path basePath;
    private String serverName;
    private LinkedHashMap<TileId, Image> memoryCache;
    private final static int MAX_SIZE = 100;

    /**
     * Public constructor
     *
     * @param basePath   path containing the in disk cache
     * @param serverName name of the tile server
     */
    public TileManager(Path basePath, String serverName) {
        this.basePath = basePath;
        this.serverName = serverName;
        this.memoryCache = new LinkedHashMap<>(100, 0.75f, true) {

            /**
             * Method called by put that removes the eldest entry if the LinkedHashMap is at maximum capacity
             * @param eldest the Map.Entry view of the LinkedHashMap
             * @return true if the eldest entry should be removed from the map; false if it should be retained.
             */
            @Override
            protected boolean removeEldestEntry(Map.Entry<TileId, Image> eldest) {
                return size() > MAX_SIZE;
            }
        };
    }

    /**
     * Loads the image of a given tile identity
     * @param tileId the TileId from which we want the image
     * @return the image of the tile
     * @throws IOException
     */
    public Image imageForTileAt(TileId tileId) throws IOException {
        Preconditions.checkArgument(TileId.isValid(tileId.zoomLevel, tileId.X, tileId.Y));

        //if the image is already in the memory cache
        if (memoryCache.containsValue(tileId)) {
            return memoryCache.get(tileId);
        }

        Path fullPath = tileId.createPath(basePath);
        Path imagePath= Path.of(fullPath+"/"+tileId.Y+".png");
        if (!Files.exists(imagePath)) {
            URL u;
            URLConnection c;
            try {
                Files.createDirectories(fullPath);
                u = new URL(tileId.urlName(serverName));
                c = u.openConnection();
                c.setRequestProperty("User-Agent", "JaVelo");

                try (InputStream in = c.getInputStream(); OutputStream out = new FileOutputStream(imagePath.toFile())) {
                    in.transferTo(out);
                }

            } catch (IOException e) {
                throw e;
            }
        }
        try (InputStream in = new FileInputStream(imagePath.toFile())) {
            Image image = new Image(in);
            memoryCache.put(tileId, image);
            return image;
        }

    }


    /**
     * Inner record representing the if of a tile OSM
     * <p>
     * Constructor
     *
     * @param zoomLevel the zoom level of the tile
     * @param X         the X index of the tile
     * @param Y         the Y index of the tile
     */
    public record TileId(int zoomLevel, int X, int Y) {
        public static boolean isValid(int zoomLevel, int X, int Y) {
            double limit = Math.pow(2, zoomLevel);
            return (0 <= zoomLevel && 0 <= X && X <= limit && 0 <= Y && Y <= limit);
        }

        public Path createPath(Path basePath) {
            Path newPath = basePath.resolve(String.valueOf(zoomLevel));
            newPath = newPath.resolve(String.valueOf(X));

            return newPath;
        }

        public String urlName(String serverName) {
            StringBuilder builder = new StringBuilder("https://" + serverName);
            builder.append("/" + zoomLevel + "/" + X + "/" + Y + ".png");
            return builder.toString();
        }
    }
}
