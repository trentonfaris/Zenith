#version 430

// Quality level 39
#define EDGE_STEP_COUNT 12
#define EDGE_STEPS 1, 1, 1, 1, 1, 1.5, 2, 2, 2, 2, 4, 8
#define EDGE_GUESS 8

struct LuminanceNeighborhood {
  float c, n, s, e, w, ne, se, nw, sw;

  float maxLuminance, minLuminance;
  float contrast;
};

struct Edge {
  bool isHorizontal;

  vec2 direction;

  float oppLuminance;
  float gradient;
};

in vec2 fragTexCoord;

out vec4 outColor;

uniform sampler2D grayscale;
uniform sampler2D screen;
uniform vec2 texelSize;
uniform float contrastThreshold;
uniform float relativeThreshold;

const float edgeSteps[EDGE_STEP_COUNT] = { EDGE_STEPS };

LuminanceNeighborhood sampleLuminanceNeighborhood(vec2 texCoord) {
  LuminanceNeighborhood ln;

  ln.c = texture(grayscale, texCoord).r;

  ln.n = textureOffset(grayscale, texCoord, ivec2(0, 1)).r;
  ln.s = textureOffset(grayscale, texCoord, ivec2(0, -1)).r;
  ln.e = textureOffset(grayscale, texCoord, ivec2(1, 0)).r;
  ln.w = textureOffset(grayscale, texCoord, ivec2(-1, 0)).r;

  ln.ne = textureOffset(grayscale, texCoord, ivec2(1, 1)).r;
  ln.se = textureOffset(grayscale, texCoord, ivec2(1, -1)).r;
  ln.nw = textureOffset(grayscale, texCoord, ivec2(-1, 1)).r;
  ln.sw = textureOffset(grayscale, texCoord, ivec2(-1, -1)).r;

  ln.maxLuminance = max(max(max(max(ln.n, ln.s), ln.e), ln.w), ln.c);
  ln.minLuminance = min(min(min(min(ln.n, ln.s), ln.e), ln.w), ln.c);
  ln.contrast = ln.maxLuminance - ln.minLuminance;

  return ln;
}

float calculatePixelBlendFactor(LuminanceNeighborhood ln) {
  float lowPass = (2.0 * (ln.n + ln.e + ln.s + ln.w) + (ln.ne + ln.se + ln.nw + ln.sw)) / 12.0;
  float highPass = abs(lowPass - ln.c);
  float n = clamp(highPass / ln.contrast, 0.0, 1.0);
  float pb = smoothstep(0.0, 1.0, n);

  return pb * pb;
}

Edge calculateEdge(LuminanceNeighborhood ln, vec2 texelSize) {
  Edge e;

  float horizontal = abs(ln.n + ln.s - 2.0 * ln.c) * 2.0 + abs(ln.ne + ln.se - 2.0 * ln.e) + abs(ln.nw + ln.sw - 2.0 * ln.w);
  float vertical = abs(ln.e + ln.w - 2.0 * ln.c) * 2.0 + abs(ln.ne + ln.nw - 2.0 * ln.n) + abs(ln.se + ln.sw - 2.0 * ln.s);

  e.isHorizontal = horizontal >= vertical;

  e.direction = e.isHorizontal ? vec2(0.0, texelSize[1]) : vec2(texelSize[0], 0.0);

  float posLuminance = e.isHorizontal ? ln.n : ln.e;
  float negLuminance = e.isHorizontal ? ln.s : ln.w;

  float posGradient = abs(posLuminance - ln.c);
  float negGradient = abs(negLuminance - ln.c);

  if (posGradient < negGradient) {
    e.direction = -e.direction;
    e.oppLuminance = negLuminance;
    e.gradient = negGradient;
  } else {
    e.oppLuminance = posLuminance;
    e.gradient = posGradient;
  }

  return e;
}

float calculateEdgeBlendFactor(LuminanceNeighborhood ln, Edge e, vec2 texCoord, vec2 texelSize) {
  vec2 halfwayTexCoord = e.isHorizontal ? texCoord + e.direction * 0.5 : texCoord + e.direction * 0.5;
  float halfwayLuminance = (ln.c + e.oppLuminance) * 0.5;
  float gradientThreshold = e.gradient * 0.25;

  vec2 step = e.isHorizontal ? vec2(texelSize[0], 0.0) : vec2(0.0, texelSize[1]);

  vec2 posOffset = halfwayTexCoord;
  float posLuminanceDelta = 0.0;
  bool posAtEnd = false;

  for (int i = 0; i < EDGE_STEP_COUNT && !posAtEnd; i++) {
    posOffset += step * edgeSteps[i];
    posLuminanceDelta = texture(grayscale, posOffset).r - halfwayLuminance;
    posAtEnd = abs(posLuminanceDelta) >= gradientThreshold;
  }

  vec2 negOffset = halfwayTexCoord;
  float negLuminanceDelta = 0.0;
  bool negAtEnd = false;

  for (int i = 0; i < EDGE_STEP_COUNT && !negAtEnd; i++) {
    negOffset -= step * edgeSteps[i];
    negLuminanceDelta = texture(grayscale, negOffset).r - halfwayLuminance;
    negAtEnd = abs(negLuminanceDelta) >= gradientThreshold;
  }

  float posDistance;
  float negDistance;

  if (e.isHorizontal) {
    posDistance = posOffset[0] - texCoord[0];
    negDistance = texCoord[0] - negOffset[0];
  } else {
    posDistance = posOffset[1] - texCoord[1];
    negDistance = texCoord[1] - negOffset[1];
  }

  float shortestDistance;
  bool deltaSign;

  if (posDistance <= negDistance) {
    shortestDistance = posDistance;
    deltaSign = posLuminanceDelta >= 0;
  } else {
    shortestDistance = negDistance;
    deltaSign = negLuminanceDelta >= 0;
  }

  if (deltaSign == (ln.c - halfwayLuminance >= 0)) {
    return 0.0;
  }

  return 0.5 - shortestDistance / (posDistance + negDistance);
}

void main()
{
	vec3 color = texture(screen, fragTexCoord).rgb;

	LuminanceNeighborhood ln = sampleLuminanceNeighborhood(fragTexCoord);
	if (ln.contrast < max(contrastThreshold, relativeThreshold * ln.maxLuminance)) {
	  outColor = vec4(color, 1.0);
	  return;
	}

	Edge e = calculateEdge(ln, texelSize);

  float pb = calculatePixelBlendFactor(ln);
	float eb = calculateEdgeBlendFactor(ln, e, fragTexCoord, texelSize);

  float b = max(pb, eb);

	vec2 texCoord = fragTexCoord + e.direction * b;

	vec3 blended = textureLod(screen, texCoord, 0).rgb;

	outColor = vec4(blended, 1.0);
}
