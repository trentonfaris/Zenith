#version 430

#define MAX_NUM_VERT_LIGHTS 10
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

layout (location = 0) in vec3 vertPositionM;
layout (location = 1) in vec2 vertTexCoord;
layout (location = 2) in vec3 vertNormalM;
layout (location = 3) in vec3 vertTangentM;
layout (location = 4) in vec3 vertBitangentM;

out vec3 fragPositionT;
out vec3 fragNormalT;
out vec3 fragViewDirectionT;
out vec2 fragTexCoord;

out DirectionalLight fragDirectionalLightsT[MAX_NUM_FRAG_LIGHTS];
out PointLight fragPointLightsT[MAX_NUM_FRAG_LIGHTS];
out SpotLight fragSpotLightsT[MAX_NUM_FRAG_LIGHTS];

out vec3 fragColor;

uniform mat4 view;
uniform mat4 projection;
uniform mat4 modelView;

uniform vec3 ambientColor;

uniform int numVertDirectionalLights;
uniform int numVertSpotLights;
uniform int numVertPointLights;

uniform DirectionalLight vertDirectionalLights[MAX_NUM_VERT_LIGHTS];
uniform PointLight vertPointLights[MAX_NUM_VERT_LIGHTS];
uniform SpotLight vertSpotLights[MAX_NUM_VERT_LIGHTS];

uniform int numFragDirectionalLights;
uniform int numFragPointLights;
uniform int numFragSpotLights;

uniform DirectionalLight fragDirectionalLights[MAX_NUM_FRAG_LIGHTS];
uniform PointLight fragPointLights[MAX_NUM_FRAG_LIGHTS];
uniform SpotLight fragSpotLights[MAX_NUM_FRAG_LIGHTS];

uniform Material material;

const float PI = 3.14159265359;
const float F0 = 0.04;

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

vec3 calcLuminance(vec3 positionT, vec3 normalT, vec3 viewDirection,
		vec3 lightDirection, vec3 radiance, vec3 albedo, float metallic,
		float roughness) {
	vec3 halfwayDirection = normalize(viewDirection + lightDirection);

	float nv = max(dot(normalT, viewDirection), 0.0001);
	float nl = max(dot(normalT, lightDirection), 0.0001);
	float nh = max(dot(normalT, halfwayDirection), 0.0001);
	float vh = max(dot(viewDirection, halfwayDirection), 0.0001);

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

vec3 calcDirectionalLight(DirectionalLight directionalLight, vec3 positionT,
		vec3 normalT, vec3 viewDirection, vec3 albedo, float metallic,
		float roughness) {
	vec3 lightDirection = normalize(-directionalLight.direction);

	vec3 radiance = directionalLight.light.color;

	return calcLuminance(positionT, normalT, viewDirection, lightDirection,
			radiance, albedo, metallic, roughness);
}

vec3 calcPointLight(PointLight pointLight, vec3 positionT, vec3 normalT,
		vec3 viewDirection, vec3 albedo, float metallic, float roughness) {
	vec3 lightDirection = normalize(pointLight.position - positionT);

	float distance = length(pointLight.position - positionT);
	float attenuation = calcAttenuation(pointLight.radius, distance);

	vec3 radiance = pointLight.light.color * attenuation;

	return calcLuminance(positionT, normalT, viewDirection, lightDirection,
			radiance, albedo, metallic, roughness);
}

vec3 calcSpotLight(SpotLight spotLight, vec3 positionT, vec3 normalT,
		vec3 viewDirection, vec3 albedo, float metallic, float roughness) {
	vec3 lightDirection = normalize(spotLight.pointLight.position - positionT);

	float distance = length(spotLight.pointLight.position - positionT);
	float attenuation = calcAttenuation(spotLight.pointLight.radius, distance);

	float theta = dot(lightDirection, normalize(-spotLight.direction));
	float epsilon = abs(
			cos(spotLight.innerCutoffAngle) - cos(spotLight.outerCutoffAngle));

	if (epsilon == 0.0) {
		epsilon = 0.001;
	}

	float intensity = clamp((theta - cos(spotLight.outerCutoffAngle)) / epsilon,
			0.0, 1.0);

	vec3 radiance = spotLight.pointLight.light.color * attenuation * intensity;

	return calcLuminance(positionT, normalT, viewDirection, lightDirection,
			radiance, albedo, metallic, roughness);
}

void main() {
	mat3 normal = mat3(transpose(inverse(modelView)));

	vec3 vertPositionV = vec3(modelView * vec4(vertPositionM, 1.0));
	vec3 vertNormalV = normalize(normal * vertNormalM);
	vec3 viewDirection = normalize(-vertPositionV);

	vec3 t = normalize(normal * vertTangentM);
	vec3 n = normalize(normal * vertNormalM);

	t = normalize(t - n * dot(n, t));

	vec3 b = cross(n, t);
	//b = normalize(normal * vertBitangentM);

	mat3 tbn = transpose(mat3(t, b, n));

	// Transform position and normal from view space to tangent space.
	fragPositionT = tbn * vertPositionV;
	fragNormalT = normalize(tbn * vertNormalV);
	fragViewDirectionT = normalize(tbn * normalize(-vertPositionV));
	fragTexCoord = vertTexCoord;

	// Transform fragment lights from world space to tangent space.
	int maxFragDirectionalLights = min(numFragDirectionalLights,
	MAX_NUM_FRAG_LIGHTS);
	for (int i = 0; i < maxFragDirectionalLights; i++) {
		DirectionalLight directionalLight = fragDirectionalLights[i];
		directionalLight.direction = normalize(
				tbn * vec3(view * vec4(directionalLight.direction, 0.0)));

		fragDirectionalLightsT[i] = directionalLight;
	}

	int maxFragPointLights = min(numFragPointLights, MAX_NUM_FRAG_LIGHTS);
	for (int i = 0; i < maxFragPointLights; i++) {
		PointLight pointLight = fragPointLights[i];
		pointLight.position = tbn * vec3(view * vec4(pointLight.position, 1.0));

		fragPointLightsT[i] = pointLight;
	}

	int maxFragSpotLights = min(numFragSpotLights, MAX_NUM_FRAG_LIGHTS);
	for (int i = 0; i < maxFragSpotLights; i++) {
		SpotLight spotLight = fragSpotLights[i];
		spotLight.pointLight.position = tbn
				* vec3(view * vec4(spotLight.pointLight.position, 1.0));
		spotLight.direction = normalize(
				tbn * vec3(view * vec4(spotLight.direction, 0.0)));

		fragSpotLightsT[i] = spotLight;
	}

	// Assign the homogenous vertex position.
	gl_Position = projection * vec4(vertPositionV, 1.0);

	// Compute vertex lighting in view space.
	vec3 albedo;
	if (material.useAlbedoMap) {
		albedo = texture(material.albedoMap, vertTexCoord).rgb;
	} else {
		albedo = material.albedoColor;
	}

	float metallic;
	if (material.useMetallicMap) {
		metallic = texture(material.metallicMap, vertTexCoord).r;
	} else {
		metallic = material.metallicValue;
	}

	float roughness;
	if (material.useRoughnessMap) {
		roughness = texture(material.roughnessMap, vertTexCoord).r;
	} else {
		roughness = material.roughnessValue;
	}

	float ao;
	if (material.useAoMap) {
		ao = texture(material.aoMap, vertTexCoord).r;
	} else {
		ao = 1;
	}

	vec3 result = ambientColor;

	int numLights = 0;

	int maxVertDirectionalLights = min(numVertDirectionalLights,
	MAX_NUM_VERT_LIGHTS);
	for (int i = 0; i < maxVertDirectionalLights; i++) {
		if (numLights < MAX_NUM_VERT_LIGHTS) {
			result += calcDirectionalLight(vertDirectionalLights[i],
					vertPositionV, vertNormalV, viewDirection, albedo, metallic,
					roughness);
			numLights++;
		}
	}

	int maxVertPointLights = min(numVertPointLights, MAX_NUM_VERT_LIGHTS);
	for (int i = 0; i < maxVertPointLights; i++) {
		if (numLights < MAX_NUM_VERT_LIGHTS) {
			result += calcPointLight(vertPointLights[i], vertPositionV,
					vertNormalV, viewDirection, albedo, metallic, roughness);
			numLights++;
		}
	}

	int maxVertSpotLightsSpot = min(numVertSpotLights, MAX_NUM_VERT_LIGHTS);
	for (int i = 0; i < maxVertSpotLightsSpot; i++) {
		if (numLights < MAX_NUM_VERT_LIGHTS) {
			result += calcSpotLight(vertSpotLights[i], vertPositionV,
					vertNormalV, viewDirection, albedo, metallic, roughness);
			numLights++;
		}
	}

	fragColor = result;
}
