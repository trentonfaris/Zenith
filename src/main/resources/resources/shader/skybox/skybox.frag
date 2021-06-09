#version 430

in vec3 fragPositionM;

out vec4 outColor;

uniform samplerCube skybox;

void main() {
	outColor = texture(skybox, fragPositionM);
}