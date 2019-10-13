precision mediump float;
varying highp vec2 vTextureCoord;
uniform lowp sampler2D sTexture;

const highp mat3 rgb2lms = mat3(
    17.8824,3.45565,0.0299566,
    43.5161,27.1554,0.184309,
    4.11935,3.86714,1.46709
);

const highp mat3 lms2rgb = mat3(
    0.0809444479, -0.0102485335, -.000365296938,
    -0.130504409, 0.0540193266, -0.00412161469,
    0.116721066, -0.113614708, 0.693511405
);

const highp mat3 lms21ms_deficient = mat3(
    1, 0.494207, 0,
    0, 0, 0,
    0, 1.24827, 1
);

const highp mat3 err2mod = mat3(
    0, 0.7, 0.7,
    0,1,0,
    0,0,1
);

void main() {
    lowp vec4 color = texture2D(sTexture, vTextureCoord);

    lowp vec3 rgb = color.rgb;
    lowp vec3 lms = rgb2lms * rgb;

    lowp vec3 _lms = lms21ms_deficient * lms;

    lowp vec3 _rgb = lms2rgb * _lms;

    lowp vec3 error = rgb - _rgb;
    lowp vec3 err = err2mod * error;

    lowp vec3 dtpn = err + rgb;

    dtpn[0] = max(0.0, dtpn[0]);
    dtpn[0] = min(255.0, dtpn[0]);
    dtpn[1] = max(0.0, dtpn[1]);
    dtpn[1] = min(255.0, dtpn[1]);
    dtpn[2] = max(0.0, dtpn[2]);
    dtpn[2] = min(255.0, dtpn[2]);

    gl_FragColor = vec4(dtpn.rgb, color.a);
}