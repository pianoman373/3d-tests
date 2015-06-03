#version 150 core

in vec3 vertexColor;
in vec2 UV;
in vec3 LightDirection_cameraspace;
in vec3 Normal_cameraspace;
in vec3 EyeDirection_cameraspace;

out vec4 fragColor;

uniform sampler2D texImage;

void main() {
	// Normal of the computed fragment, in camera space
	vec3 n = normalize( Normal_cameraspace );
	// Direction of the light (from the fragment to the light)
	vec3 l = normalize( LightDirection_cameraspace );

	float cosTheta = clamp( dot( n,l ), 0,1 );

    vec3 imageColor = texture( texImage, UV ).rgb;
    vec3 lightColor = vec3(1, 1, 1);
    vec3 ambientColor = vec3(0.2, 0.2, 0.2);
    float lightPower = 1.2;
    
    // Eye vector (towards the camera)
	vec3 E = normalize(EyeDirection_cameraspace);
	// Direction in which the triangle reflects the light
	vec3 R = reflect(-l,n);
	// Cosine of the angle between the Eye vector and the Reflect vector,
	// clamped to 0
	//  - Looking into the reflection -> 1
	//  - Looking elsewhere -> < 1
	float cosAlpha = clamp( dot( E,R ), 0,1 );
 
	vec3 MaterialDiffuseColor = (ambientColor + lightColor * cosTheta) * imageColor * lightPower;
    vec3 MaterialSpecularColor = lightColor * lightPower * pow(cosAlpha,10);
    
    fragColor = vec4(MaterialDiffuseColor + (MaterialSpecularColor * 0.2), 1);
}