package net.oceanic.ancientsorcery;

import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;

public class ItemImbumentInfo {
    public static boolean setImbumentPercentage(ItemStack stack, float imbumentPercentage){
        float actualPercentage = Math.min(imbumentPercentage,100.0f);
        if (imbumentPercentage < 0){
            return false;
        }
        if (!stack.isEmpty() && canImbue(stack)) {
            if (stack.hasNbt()) {
                stack.getNbt().putFloat("imbumentPercentage", actualPercentage);
            } else {
                NbtCompound nbt = new NbtCompound();
                nbt.putFloat("imbumentPercentage", actualPercentage);
                stack.setNbt(nbt);
            }
            return true;
        }
        return false;
    }
    public static float getImbumentPercentage(ItemStack stack){
        if (!stack.isEmpty() && canImbue(stack) && stack.hasNbt() && stack.getNbt().contains("imbumentPercentage")) {
            return stack.getNbt().getFloat("imbumentPercentage");
        }
        return 0;
    }
    public static void setImbumentType(ItemStack stack, String imbumentType){
        if (!stack.isEmpty() && canImbue(stack)) {
            if (stack.hasNbt()) {
                stack.getNbt().putString("imbumentType", imbumentType);
            } else {
                NbtCompound nbt = new NbtCompound();
                nbt.putString("imbumentType", imbumentType);
                stack.setNbt(nbt);
            }
        }
    }
    public static String getImbumentType(ItemStack stack){
        if (!stack.isEmpty() && canImbue(stack) && stack.hasNbt() && stack.getNbt().contains("imbumentType")) {
            return stack.getNbt().getString("imbumentType");
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
            return !getImbumentType(stack).equals("none");
        }
        return false;
    }
    public static int getColor(ItemStack stack){
        return getColor(getImbumentType(stack));
    }
    public static int getColor(String element){
        if (element.equals("gravity")){
            return 0x69008D;
        }
        return -1;
    }
}
