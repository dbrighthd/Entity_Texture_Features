package traben.entity_texture_features.mixin.entity.renderer.feature;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import traben.entity_texture_features.texture_handlers.ETFManager;
import traben.entity_texture_features.texture_handlers.ETFTexture;
import traben.entity_texture_features.entity_handlers.ETFEntityWrapper;

import static traben.entity_texture_features.ETFClientCommon.ETFConfigData;

@Mixin(WolfCollarFeatureRenderer.class)
public abstract class MixinWolfCollarFeatureRenderer extends FeatureRenderer<IronGolemEntity, IronGolemEntityModel<IronGolemEntity>> {


    ETFEntityWrapper etf$entity = null;
    private ETFTexture thisETFTexture = null;

    @SuppressWarnings("unused")
    public MixinWolfCollarFeatureRenderer(FeatureRendererContext<IronGolemEntity, IronGolemEntityModel<IronGolemEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/WolfCollarFeatureRenderer;renderModel(Lnet/minecraft/client/render/entity/model/EntityModel;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFF)V",
                    shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)

    private void etf$applyEmissive(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WolfEntity wolfEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci, float[] fs) {
        //UUID id = livingEntity.getUuid();
        if (thisETFTexture != null)
            thisETFTexture.renderEmissive(matrixStack, vertexConsumerProvider, this.getContextModel());
    }

    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V",
            at = @At(value = "HEAD"))
    private void etf$getEntity(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WolfEntity wolfEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        etf$entity = new ETFEntityWrapper(wolfEntity);
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/feature/WolfCollarFeatureRenderer;renderModel(Lnet/minecraft/client/render/entity/model/EntityModel;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFF)V")
            , index = 1)
    private Identifier etf$returnAlteredTexture(Identifier texture) {

        thisETFTexture = ETFManager.getInstance().getETFTexture(texture, etf$entity, ETFManager.TextureSource.ENTITY_FEATURE, ETFConfigData.removePixelsUnderEmissiveMobs);
        return thisETFTexture.getTextureIdentifier(etf$entity);
    }
}


