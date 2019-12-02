package talinterface;
import java.io.*;
import java.util.*;
import javafx.stage.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maximo
 */
public class Config implements Serializable {

    public static final long serialVersionUID = 1;

    private File config; //Just the route to the file with the object this printed

    private File defaultPath;

    public Config() {
    }

    /**
     * Constructor for new config files.
     *
     * @param f The path to the config file
     * @param blank if true, a default config will be created. If not, it will
     * be read
     */
    public Config(File f, boolean blank) {
        config = f;
        if (blank) {
            resetToDefault();
        } else {
            try (ObjectInputStream configScanner = new ObjectInputStream(new FileInputStream(f))) {
                Config init = (Config) configScanner.readObject();
                this.defaultPath = init.defaultPath;
            } catch (IOException e) {
                System.err.println("Error reading config file");
            } catch (ClassNotFoundException e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
                System.err.println("Bad format for config file");
                resetToDefault();
            }
        }
    }

    private void resetToDefault() {
        defaultPath = new File(System.getProperty("user.dir"));
    }

    public File getPath() {
        return defaultPath;
    }

    public void setPath(File f) {
        if (!defaultPath.equals(f)) {
            this.defaultPath = f;
            write();
        }
    }

    public void write() {
        try (ObjectOutputStream configPW = new ObjectOutputStream(new FileOutputStream(config))) {
            configPW.writeObject(this);
            configPW.flush();
        } catch (IOException e) {
        }
    }
}
