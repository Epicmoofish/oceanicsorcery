package net.oceanic.ancientsorcery.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

//Code yoinked from TheSwirlingVoid

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.OceanicSorceryMod;

public abstract class BaseControllerFiles {

    public static String MOD_CONFIG_PATH;

    private String instancePath;
    protected List<String> instanceDataFileNames = new ArrayList<>();
    public World world;
    public BaseControllerFiles(World world)
    {
        this.MOD_CONFIG_PATH = world.getServer().getSavePath(WorldSavePath.ROOT).resolve(OceanicSorceryMod.MODID).toString();
        this.world = world;
        setBasePath();
        addConfigurationFiles();
    }
    protected static String getModConfigDir() {
        return MOD_CONFIG_PATH;
    }
    protected void setBasePath(String path){
        instancePath = path;
        new File(path).mkdirs();
    }
    protected void addPath(String path)
    {
        new File(instancePath+"/"+path).mkdirs();
    }

    protected void addConfigurationFiles(List<String> fileNames)
    {
        instanceDataFileNames = fileNames;

        try {

            for (String fileName : fileNames)
            {
                File configFile = getConfigurationFile(fileName);
                if (!configFile.exists()){
                    configFile.createNewFile();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void setBasePath();
    protected abstract void addConfigurationFiles();

    private File getConfigurationFile(String filename)
    {
        return new File(instancePath + "/" + filename);
    }
//    protected void addConfigurationFile(String filePathAndName){
//        instanceDataFileNames.add(filePathAndName);
//    }
    public List<File> getConfigurationFiles()
    {
        ArrayList<File> files = new ArrayList<>();
        for (String fileName : instanceDataFileNames)
        {
            files.add(getConfigurationFile(fileName));
        }
        return files;
    }


    public String getPath()
    {
        return instancePath;
    }

    public static String getModConfigPath() {
        return MOD_CONFIG_PATH;
    }
}