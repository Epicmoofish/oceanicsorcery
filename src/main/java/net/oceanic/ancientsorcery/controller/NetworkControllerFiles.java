package net.oceanic.ancientsorcery.controller;

import net.oceanic.ancientsorcery.OceanicSorceryMod;

import java.util.ArrayList;
import java.util.List;

// Code also yoinked from TheSwirlingVoid

public class NetworkControllerFiles extends BaseControllerFiles {

    private static final String PIPE_REGION_FILENAME = "pipeMovementSettings.json";
    private static final String PIPE_REGION_DIR_PATH = OceanicSorceryMod.MODID +"/controllerSettings";


    public static String getPipeRegionFilename() {
        return PIPE_REGION_FILENAME;
    }

    @Override
    protected void addPath() {
        super.addPath(PIPE_REGION_DIR_PATH);
    }

    @Override
    protected void addConfigurationFiles() {
        super.addConfigurationFiles(getConfigurationFileNames());
    }

    private List<String> getConfigurationFileNames()
    {
        ArrayList<String> configFiles = new ArrayList<>();

        configFiles.add(PIPE_REGION_FILENAME);

        return configFiles;
    }
}