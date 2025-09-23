#version 330 core
in vec2 texCoord;
out vec4 fragColor;

uniform float MaxSize;
uniform vec4 Color;

#define MODE_FILL 1
#define MODE_STROKE 2
#define MODE_SHADOW 3

uniform int Mode;

float circle(vec2 p, float r, vec2 texCoord) {
    float d = distance(p, texCoord);
    return d;
}

void main() {
    float dist = circle(vec2(0.5, 0.5), 0.5, texCoord);
    float tp = 0.5;
    float f = 1.0 / MaxSize;

    float alpha = 0.0;

    float _step = 1.0 - smoothstep(tp - f, tp + f, dist);
    switch (Mode) {
        case (MODE_FILL):
            alpha = _step;
            break;
        case (MODE_STROKE):
            float innerRadius = tp - f * 3.0;
            float outerRadius = tp + f * 3.0;
            float strokeMask = step(innerRadius, dist) * _step;
            float strokeAlpha = mix(0.3, 1.0, (dist - innerRadius) / (outerRadius - innerRadius));
            alpha = strokeMask * strokeAlpha + _step * 0.2;
            break;

        case (MODE_SHADOW):
            alpha = 1.0 - smoothstep(0.0, 1.0, dist) * 2.0;
            break;
    }

    fragColor = vec4(Color.rgb, alpha * Color.a);
}