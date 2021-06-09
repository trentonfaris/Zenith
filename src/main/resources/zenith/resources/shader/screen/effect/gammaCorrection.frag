#version 430

in vec2 fragTexCoord;

out vec4 outColor;

uniform sampler2D screen;

uniform float gamma;

vec3 calculateGammaCorrection(vec3 color) {
	return pow(color, vec3(1.0 / gamma));
}

void main()
{
	vec3 color = texture(screen, fragTexCoord).rgb;
	color = calculateGammaCorrection(color);

	outColor = vec4(color, 1.0);
}
