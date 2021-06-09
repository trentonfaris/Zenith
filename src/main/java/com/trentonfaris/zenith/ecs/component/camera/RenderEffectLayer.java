package com.trentonfaris.zenith.ecs.component.camera;

import java.util.Arrays;
import java.util.List;

import com.artemis.Component;

public class RenderEffectLayer extends Component {
	public final List<RenderEffect> renderEffects = Arrays.asList(new FXAAEffect(), new FilmicEffect(),
			new GammaCorrectionEffect());
}
