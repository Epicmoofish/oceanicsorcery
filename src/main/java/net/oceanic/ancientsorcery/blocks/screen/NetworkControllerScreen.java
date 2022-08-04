package net.oceanic.ancientsorcery.blocks.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.blocks.ElementalNetworkControllerBlock;

import java.util.List;

public class NetworkControllerScreen extends Screen {
    public final List<ElementalNetworkControllerBlock.BlockEntityInfo> info;
    protected NetworkControllerScreen(Text title, List<ElementalNetworkControllerBlock.BlockEntityInfo> info, World world) {
        super(title);
        this.info=info;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        this.itemRenderer.renderGuiItemIcon(info.get(0).getPos());
        super.render(matrices, mouseX, mouseY, delta);
    }
}
