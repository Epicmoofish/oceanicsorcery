package net.oceanic.ancientsorcery.controller;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.OceanicSorceryMod;
import net.oceanic.ancientsorcery.blocks.ElementalNetworkControllerBlock;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

// Code also yoinked from TheSwirlingVoid

public class NetworkControllerFiles extends BaseControllerFiles {

    private static String PIPE_REGION_DIR_PATH;
    private static final String FILE_FORMAT = ".json";
    public static final String NETWORK_KEY = "Network";

    public NetworkControllerFiles(World world) {
        super(world);
        PIPE_REGION_DIR_PATH = getModConfigDir() + "/network_controllers";
    }

    public static NetworkControllerFiles get(World world){
        return new NetworkControllerFiles(world);
    }
    @Override
    protected void setBasePath() {
        super.addPath(PIPE_REGION_DIR_PATH);
    }

    @Override
    protected void addConfigurationFiles() {
        super.addConfigurationFiles(getConfigurationFileNames());
    }
    private List<String> getConfigurationFileNames()
    {
        ArrayList<String> configFiles = new ArrayList<>();

        return configFiles;
    }
    public boolean existsAlready(int id) {
        String dimName = getDimensionNameFromWorld();
        String directoryPath = PIPE_REGION_DIR_PATH + "/" + dimName;
        String checkFilePath = directoryPath + "/" + id + FILE_FORMAT;
        return new File(checkFilePath).exists();
    }
    public int getNewRegionId()
    {
        String dimName = getDimensionNameFromWorld();

        int newFileId = 0;
        while (true)
        {
            String directoryPath = PIPE_REGION_DIR_PATH + "/" + dimName;
            String checkFilePath = directoryPath + "/" + newFileId + FILE_FORMAT;

            if (!new File(checkFilePath).exists())
            {
                return newFileId;
            }
            newFileId++;
        }
    }
    public Set<ElementalNetworkControllerBlock.BlockEntityTransfer> readJson(UUID uuid) {
        String fileIdString = uuid.toString();

        String dimName = getDimensionNameFromWorld();

        File dir = new File(PIPE_REGION_DIR_PATH + "/" + dimName);
        File jsonfile = new File(dir.toPath().toString() + "/" + fileIdString + FILE_FORMAT);
//        getValueFromFile(jsonfile, "transfers").getClass();
        if (getValueFromFile(jsonfile, "transfers") instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) getValueFromFile(jsonfile, "transfers");
            Set<ElementalNetworkControllerBlock.BlockEntityTransfer> blockEntityTransfers = new HashSet<>();
            for (Object json: jsonArray){
                if (json instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject) json;
                    try {
                        BlockPos pos = BlockPos.fromLong(Long.parseLong(((String)jsonObject.get("pos"))));
                        int tier = (int)jsonObject.get("tier");
                        float transferRate = ((BigDecimal)jsonObject.get("transferRate")).floatValue();
                        BlockPos pipePos = BlockPos.fromLong(Long.parseLong(((String)jsonObject.get("pipePos"))));
                        ElementalNetworkControllerBlock.BlockEntityInfo info = new ElementalNetworkControllerBlock.BlockEntityInfo(pos, tier, transferRate, pipePos);
                        OceanicSorceryMod.TransferMode transferMode = OceanicSorceryMod.TransferMode.valueOfLabel((int)jsonObject.get("transferMode"));
                        OceanicSorceryMod.Spell spell = OceanicSorceryMod.Spell.valueOfLabel((String)jsonObject.get("spell"));
                        ElementalNetworkControllerBlock.BlockEntityTransfer transfer = new ElementalNetworkControllerBlock.BlockEntityTransfer(info, transferMode, spell);
                        blockEntityTransfers.add(transfer);
                    } catch (Exception e){
                    }
                }
            }
            return blockEntityTransfers;
        }
        return new HashSet<>();
    }
    public Object getValueFromFile(File configFile, String key)
    {
        try {
            JSONObject obj = new JSONObject(new JSONTokener(new FileInputStream(configFile)));
            return obj.get(key);
        } catch (Exception e) {
            // if the file wasn't found, it'll end up here
            return JSONObject.NULL;
        }
    }
    public boolean alreadyExists(UUID uuid){
        String fileIdString = uuid.toString();

        String dimName = getDimensionNameFromWorld();

        File dir = new File(PIPE_REGION_DIR_PATH + "/" + dimName);
        return new File(dir.toPath().toString() + "/" + fileIdString + FILE_FORMAT).exists();
    }
    public File createNewRegionFile(UUID uuid)
    {
        String fileIdString = uuid.toString();

        String dimName = getDimensionNameFromWorld();

        File dir = new File(PIPE_REGION_DIR_PATH + "/" + dimName);
        File newFile = new File(dir.toPath().toString() + "/" + fileIdString + FILE_FORMAT);

        try {
            dir.mkdirs();
            newFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newFile;
    }
    public File getNetworkFileFromID(int id)
    {

        String fileIdString = Integer.toString(id);
        String dimName = getDimensionNameFromWorld();

        File newFile = new File(PIPE_REGION_DIR_PATH + "/" + dimName + "/" + fileIdString + FILE_FORMAT);

        return newFile;
    }
    private String getDimensionNameFromWorld()
    {
        return world.getRegistryKey().getValue().toString().split(":")[1];
    }

    private String getDimensionPath()
    {
        return (PIPE_REGION_DIR_PATH + "/" + getDimensionNameFromWorld());
    }

    public File[] getDimensionRegionFiles()
    {
        return new File(getDimensionPath()).listFiles();
    }
}