#version 330 core

in vec2 fragTexCoord;
out vec4 fragColor;

uniform sampler2D Texture;
uniform vec4 Color;

void main() {
    vec4 texColor = texture(Texture, fragTexCoord);
    if (texColor.a == 0) discard;
    fragColor = texColor * Color;
}
