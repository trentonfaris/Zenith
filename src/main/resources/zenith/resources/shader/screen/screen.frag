#version 430

in vec2 fragTexCoord;

out vec4 outColor;

uniform sampler2D screen;

void main()
{
	outColor = vec4(texture(screen, fragTexCoord).rgb, 1.0);
}
