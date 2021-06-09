#version 430

in vec2 fragTexCoord;

out vec4 outColor;

uniform sampler2D screen;

const int bayerMatrix8x8[64] = int[](
  0, 32, 8, 40, 2, 34, 10, 42,
  48, 16, 56, 24, 50, 18, 58, 26,
  12, 44, 4, 36, 14, 46, 6, 38,
  60, 28, 52, 20, 62, 30, 54, 22,
  3, 35, 11, 43, 1, 33, 9, 41,
  51, 19, 59, 27, 49, 17, 57, 25,
  15, 47, 7, 39, 13, 45, 5, 37,
  63, 31, 55, 23, 61, 29, 53, 21
);

float calculateGrayscale(vec3 color) {
  return color.r * 0.2126 + color.g * 0.7152 + color.b * 0.0722;
}

float calculateDithering(float gray, vec2 texCoord) {
	return 0;
}

void main()
{
	vec3 color = texture(screen, fragTexCoord).rgb;
	float gray = calculateGrayscale(color);
	float dithering = calculateDithering(gray);

	outColor = vec4(color * dithering, 1.0);
}
