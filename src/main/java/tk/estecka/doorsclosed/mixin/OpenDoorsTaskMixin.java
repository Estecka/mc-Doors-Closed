package tk.estecka.doorsclosed.mixin;

import net.minecraft.entity.ai.brain.task.OpenDoorsTask;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(OpenDoorsTask.class)
public class OpenDoorsTaskMixin {

	@ModifyExpressionValue( method="method_46966", at=@At(value="INVOKE", ordinal=1, target="net/minecraft/block/DoorBlock.isOpen (Lnet/minecraft/block/BlockState;)Z") )
	static private boolean DontIgnoreOpenDoors(boolean original){
		return false;
	}

	/**
	 * I am not sure  what the original codes  intends to do, but to me it looks
	 * like it's working backward. It lets the task runs multiple ticks in a row
	 * on the same node, but when the node changes, triggers a 1 second cooldown
	 * that prevents the task  from running  on  the following nodes. This gives
	 * villagers a random chance  to not close a door they pass through, even if
	 * it was already closed when they found it.
	 * 
	 * Negating the expression changes this effect like so: The task will run on
	 * every new node, but will not run more than 1 consecutive times per second
	 * on the same node.
	 */
	@ModifyExpressionValue( method="method_46966", at=@At(value="INVOKE", target="java/util/Objects.equals (Ljava/lang/Object;Ljava/lang/Object;)Z"))
	static private boolean	AlwaysBeAwareOfDoors(boolean original, @Local Path path, @Local ServerWorld world){
		return !original;
	}
}
