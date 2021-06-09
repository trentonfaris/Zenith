#version 430

#define MAX_NUM_FRAG_LIGHTS 1

struct Light {
	vec3 color;
};

struct DirectionalLight {
	Light light;

	vec3 direction;
};

struct PointLight {
	Light light;

	vec3 position;
	float radius;
};

struct SpotLight {
	PointLight pointLight;

	vec3 direction;
	float innerCutoffAngle;
	float outerCutoffAngle;
};

struct Material {
	bool useAlbedoMap;
	sampler2D albedoMap;
	vec3 albedoColor;

	bool useMetallicMap;
	sampler2D metallicMap;
	float metallicValue;

	bool useRoughnessMap;
	sampler2D roughnessMap;
	float roughnessValue;

	bool useAoMap;
	sampler2D aoMap;

	bool useNormalMap;
	sampler2D normalMap;

	bool useHeightMap;
	sampler2D heightMap;
	float heightScale;
};

in vec3 fragPositionT;
in vec3 fragNormalT;
in vec3 fragViewDirectionT;
in vec2 fragTexCoord;

in DirectionalLight fragDirectionalLightsT[MAX_NUM_FRAG_LIGHTS];
in PointLight fragPointLightsT[MAX_NUM_FRAG_LIGHTS];
in SpotLight fragSpotLightsT[MAX_NUM_FRAG_LIGHTS];

in vec3 fragColor;

out vec4 outColor;

uniform int numFragDirectionalLights;
uniform int numFragPointLights;
uniform int numFragSpotLights;

uniform Material material;

const float minHeightLayers = 8.0;
const float maxHeightLayers = 32.0;

const float PI = 3.14159265359;
const float F0 = 0.04;

vec2 displaceTexCoord(vec2 texCoord, vec3 viewDirectionT, sampler2D heightMap,
		float heightScale) {
	float numHeightLayers = mix(maxHeightLayers, minHeightLayers,
			abs(dot(vec3(0.0, 0.0, 1.0), viewDirectionT)));
	float layerHeight = 1.0 / numHeightLayers;
	float currentLayerHeight = 0.0;

	vec2 deltaTexCoord = heightScale * viewDirectionT.xy / numHeightLayers;

	vec2 currentTexCoord = texCoord;
	float currentHeightMapValue = 1.0 - texture(heightMap, currentTexCoord).r;

	while (currentHeightMapValue > currentLayerHeight) {
		currentTexCoord -= deltaTexCoord;

		currentHeightMapValue = 1.0 - texture(heightMap, currentTexCoord).r;
		currentLayerHeight += layerHeight;
	}

	vec2 prevTexCoord = currentTexCoord + deltaTexCoord;

	float heightBeforeCollision = texture(heightMap, prevTexCoord).r
			- currentLayerHeight + layerHeight;
	float heightAfterCollision = currentHeightMapValue - currentLayerHeight;

	float t = heightAfterCollision
			/ (heightAfterCollision - heightBeforeCollision);

	return prevTexCoord * t + currentTexCoord * (1.0 - t);
}

float calcAttenuation(float radius, float distance) {
	if (radius == 0.0) {
		return 0.0;
	}

	return pow(clamp(1.0 - pow(distance / radius, 4.0), 0.0, 1.0), 2.0)
			/ (distance * distance + 1);
}

float distributionGGX(float nh, float roughness) {
	float a = roughness * roughness;
	float a2 = a * a;
	float nh2 = nh * nh;

	return a2 / max(PI * pow(nh2 * (a2 - 1.0) + 1.0, 2.0), 0.0001);
}

vec3 fresnelSchlick(vec3 f0, float vh) {
	return f0 + (1.0 - f0) * pow(1.0 - vh, 5.0);
}

float geometrySchlickGGX(float x, float roughness) {
	float k = pow(roughness + 1.0, 2) / 8.0;

	return x / (x * (1.0 - k) + k);
}

float geometrySmith(float nl, float nv, float roughness) {
	float ggx1 = geometrySchlickGGX(nl, roughness);
	float ggx2 = geometrySchlickGGX(nv, roughness);

	return ggx1 * ggx2;
}

vec3 calcLuminance(vec3 positionT, vec3 normalT, vec3 viewDirectionT,
		vec3 lightDirectionT, vec3 radiance, vec3 albedo, float metallic,
		float roughness) {
	vec3 halfwayDirection = normalize(viewDirectionT + lightDirectionT);

	float nv = max(dot(normalT, viewDirectionT), 0.0001);
	float nl = max(dot(normalT, lightDirectionT), 0.0001);
	float nh = max(dot(normalT, halfwayDirection), 0.0001);
	float vh = max(dot(viewDirectionT, halfwayDirection), 0.0001);

	float d = distributionGGX(nh, roughness);
	float g = geometrySmith(nl, nv, roughness);
	vec3 f = fresnelSchlick(mix(vec3(F0), albedo, metallic),
			clamp(vh, 0.0, 1.0));

	vec3 kS = f;
	vec3 kD = vec3(1.0) - kS;
	kD *= 1.0 - metallic;

	vec3 specular = (d * f * g) / max(4.0 * nl * nv, 0.0001);

	return (kD * albedo / PI + specular) * radiance * nl;
}

vec3 calcDirectionalLight(DirectionalLight directionalLightT, vec3 positionT,
		vec3 normalT, vec3 viewDirectionT, vec3 albedo, float metallic,
		float roughness) {
	vec3 lightDirectionT = -directionalLightT.direction;

	vec3 radiance = directionalLightT.light.color;

	return calcLuminance(positionT, normalT, viewDirectionT, lightDirectionT,
			radiance, albedo, metallic, roughness);
}

vec3 calcPointLight(PointLight pointLightT, vec3 positionT, vec3 normalT,
		vec3 viewDirectionT, vec3 albedo, float metallic, float roughness) {
	vec3 lightDirectionT = normalize(pointLightT.position - positionT);

	float distance = length(pointLightT.position - positionT);
	float attenuation = calcAttenuation(pointLightT.radius, distance);

	vec3 radiance = pointLightT.light.color * attenuation;

	return calcLuminance(positionT, normalT, viewDirectionT, lightDirectionT,
			radiance, albedo, metallic, roughness);
}

vec3 calcSpotLight(SpotLight spotLightT, vec3 positionT, vec3 normalT,
		vec3 viewDirectionT, vec3 albedo, float metallic, float roughness) {
	vec3 lightDirectionT = normalize(
			spotLightT.pointLight.position - positionT);

	float distance = length(spotLightT.pointLight.position - positionT);
	float attenuation = calcAttenuation(spotLightT.pointLight.radius, distance);

	float theta = dot(lightDirectionT, -spotLightT.direction);
	float epsilon = abs(
			cos(spotLightT.innerCutoffAngle)
					- cos(spotLightT.outerCutoffAngle));

	if (epsilon == 0.0) {
		epsilon = 0.001;
	}

	float intensity = clamp(
			(theta - cos(spotLightT.outerCutoffAngle)) / epsilon, 0.0, 1.0);

	vec3 radiance = spotLightT.pointLight.light.color * attenuation * intensity;

	return calcLuminance(positionT, normalT, viewDirectionT, lightDirectionT,
			radiance, albedo, metallic, roughness);
}

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

void main() {
	// Compute fragment lighting in tangent space.
	vec2 texCoord;
	if (material.useHeightMap) {
		texCoord = displaceTexCoord(fragTexCoord, fragViewDirectionT,
				material.heightMap, material.heightScale);
	} else {
		texCoord = fragTexCoord;
	}

	vec3 normalT;
	if (material.useNormalMap) {
		normalT = normalize(
				texture(material.normalMap, texCoord).rgb * 2.0 - 1.0);
	} else {
		normalT = fragNormalT;
	}

	vec3 albedo;
	if (material.useAlbedoMap) {
		albedo = texture(material.albedoMap, texCoord).rgb;
	} else {
		albedo = material.albedoColor;
	}

	float metallic;
	if (material.useMetallicMap) {
		metallic = texture(material.metallicMap, texCoord).r;
	} else {
		metallic = material.metallicValue;
	}

	float roughness;
	if (material.useRoughnessMap) {
		roughness = texture(material.roughnessMap, texCoord).r;
	} else {
		roughness = material.roughnessValue;
	}

	float ao;
	if (material.useAoMap) {
		ao = texture(material.aoMap, texCoord).r;
	} else {
		ao = 1;
	}

	vec3 result = fragColor * albedo * ao;

	int numLights = 0;

	int maxFragDirectionalLights = min(numFragDirectionalLights,
	MAX_NUM_FRAG_LIGHTS);
	for (int i = 0; i < maxFragDirectionalLights; i++) {
		if (numLights < MAX_NUM_FRAG_LIGHTS) {
			result += calcDirectionalLight(fragDirectionalLightsT[i],
					fragPositionT, normalT, fragViewDirectionT, albedo,
					metallic, roughness);
			numLights++;
		}
	}

	int maxFragPointLights = min(numFragPointLights, MAX_NUM_FRAG_LIGHTS);
	for (int i = 0; i < maxFragPointLights; i++) {
		if (numLights < MAX_NUM_FRAG_LIGHTS) {
			result += calcPointLight(fragPointLightsT[i], fragPositionT,
					normalT, fragViewDirectionT, albedo, metallic, roughness);
			numLights++;
		}
	}

	int maxFragSpotLights = min(numFragSpotLights, MAX_NUM_FRAG_LIGHTS);
	for (int i = 0; i < maxFragSpotLights; i++) {
		if (numLights < MAX_NUM_FRAG_LIGHTS) {
			result += calcSpotLight(fragSpotLightsT[i], fragPositionT, normalT,
					fragViewDirectionT, albedo, metallic, roughness);
			numLights++;
		}
	}

	outColor = vec4(pow(calculateFilmicTonemapping(result), vec3(1.0 / 2.2)), 1.0);
}
