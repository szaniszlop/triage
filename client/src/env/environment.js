export const env = {
    "AUTH0_JWKS_URI": window?._env_?.AUTH0_JWKS_URI,
    "AUTH0_AUDIENCE": window?._env_?.AUTH0_AUDIENCE,
    "AUTH0_ISSUER": window?._env_?.AUTH0_ISSUER,
    "AUTH0_DOMAIN": window?._env_?.AUTH0_DOMAIN,
    "AUTH0_CLIENT_ID": window?._env_?.AUTH0_CLIENT_ID,
    "AUTH0_DEFAULT_SCOPE": window?._env_?.AUTH0_DEFAULT_SCOPE,
    "BACKEND_URL": window?._env_?.BACKEND_URL,
    "BACKEND_API_VERSION": window?._env_?.BACKEND_API_VERSION,
    "BACKEND_AUDIENCE": window?._env_?.BACKEND_AUDIENCE,
    "BACKEND_SCOPE": window?._env_?.BACKEND_SCOPE
}

console.log("This is runtime env variable from environment.js: ", env.AUTH0_JWKS_URI, env.AUTH0_AUDIENCE, env.AUTH0_ISSUER)

