package net.oceanic.ancientsorcery;

import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;

public class ItemImbuementInfo {
    public static boolean setImbuementPercentage(ItemStack stack, float imbumentPercentage){
        float actualPercentage = Math.min(imbumentPercentage,100.0f);
        if (imbumentPercentage < 0){
            return false;
        }
        if (!stack.isEmpty() && canImbue(stack)) {
            if (stack.hasNbt()) {
                stack.getNbt().putFloat("imbuementPercentage", actualPercentage);
            } else {
                NbtCompound nbt = new NbtCompound();
                nbt.putFloat("imbuementPercentage", actualPercentage);
                stack.setNbt(nbt);
            }
            return true;
        }
        return false;
    }
    public static float getImbuementPercentage(ItemStack stack){
        if (!stack.isEmpty() && canImbue(stack) && stack.hasNbt() && stack.getNbt().contains("imbuementPercentage")) {
            return stack.getNbt().getFloat("imbuementPercentage");
        }
        return 0;
    }
    public static void setImbuementType(ItemStack stack, String imbumentType){
        if (!stack.isEmpty() && canImbue(stack)) {
            if (stack.hasNbt()) {
                stack.getNbt().putString("imbuementType", imbumentType);
            } else {
                NbtCompound nbt = new NbtCompound();
                nbt.putString("imbuementType", imbumentType);
                stack.setNbt(nbt);
            }
        }
    }
    public static String getImbuementType(ItemStack stack){
        if (!stack.isEmpty() && canImbue(stack) && stack.hasNbt() && stack.getNbt().contains("imbuementType")) {
            return stack.getNbt().getString("imbuementType");
        }
        return "none";
    }
    public static boolean canImbue(ItemStack stack) {
        if (!stack.isEmpty()){
            return canImbue(stack.getItem());
        }
        return false;
    }
    public static boolean canImbue(Item item) {
        return item instanceof ToolItem || item instanceof ArmorItem || item instanceof BowItem || item instanceof ShearsItem || item instanceof FlintAndSteelItem || item instanceof FishingRodItem || item instanceof CrossbowItem || item instanceof ShieldItem || item instanceof TridentItem;
    }
    public static boolean isImbued(ItemStack stack) {
        if (!stack.isEmpty()){
            return !getImbuementType(stack).equals("none");
        }
        return false;
    }
    public static int getColor(ItemStack stack){
        return getColor(getImbuementType(stack));
    }
    public static int getColor(String element){
        if (element.equals("gravity")){
            return 0x69008D;
        }
        return -1;
    }
}
