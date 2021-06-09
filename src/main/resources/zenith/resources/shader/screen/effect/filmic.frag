#version 430

in vec2 fragTexCoord;

out vec4 outColor;

uniform sampler2D screen;

vec3 calculateFilmicTonemapping(vec3 color) {
	float a = 2.51;
	float b = 0.03;
	float c = 2.43;
	float d = 0.59;
	float e = 0.14;
	vec3 x = color;

	x = x * 0.6;

	return clamp((x * (a * x + b)) / (x * (c * x + d) + e), 0.0, 1.0);
}

void main()
{
	vec3 color = texture(screen, fragTexCoord).rgb;
	color = calculateFilmicTonemapping(color);

	outColor = vec4(color, 1.0);
}
