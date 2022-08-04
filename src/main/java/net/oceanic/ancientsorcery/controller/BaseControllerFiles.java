package net.oceanic.ancientsorcery.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

//Code yoinked from TheSwirlingVoid

import net.fabricmc.loader.api.FabricLoader;
import net.oceanic.ancientsorcery.OceanicSorceryMod;

public abstract class BaseControllerFiles {

    public static final Path MOD_CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(OceanicSorceryMod.MODID);

    private Path instancePath;
    protected List<String> instanceDataFileNames;

    public BaseControllerFiles()
    {
        addPath();
        addConfigurationFiles();
    }

    protected void addPath(String path)
    {
        instancePath = MOD_CONFIG_PATH.resolve(path);

        new File(instancePath.toString()).mkdirs();
    }

    protected void addConfigurationFiles(List<String> fileNames)
    {
        instanceDataFileNames = fileNames;

        try {

            for (String fileName : fileNames)
            {
                getConfigurationFile(fileName).createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void addPath();
    protected abstract void addConfigurationFiles();

    private File getConfigurationFile(String filename)
    {
        return new File(instancePath.toAbsolutePath() + "/" + filename);
    }

    public List<File> getConfigurationFiles()
    {
        ArrayList<File> files = new ArrayList<>();
        for (String fileName : instanceDataFileNames)
        {
            files.add(getConfigurationFile(fileName));
        }
        return files;
    }


    public Path getPath()
    {
        return instancePath;
    }

    public static Path getModConfigPath() {
        return MOD_CONFIG_PATH;
    }
}