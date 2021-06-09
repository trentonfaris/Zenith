#version 430

in vec2 fragTexCoord;

out vec4 outColor;

uniform sampler2D screen;

float calculateGrayscale(vec3 color) {
  return color.r * 0.2126 + color.g * 0.7152 + color.b * 0.0722;
}

void main()
{
	vec3 color = texture(screen, fragTexCoord).rgb;
	float gray = calculateGrayscale(color);

	outColor = vec4(gray, gray, gray, 1.0);
}
