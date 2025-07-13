
export class CommonConstants {
    public static readonly BACKEND_END_POINT = "https://library-v1-0-1.onrender.com";
    
    // Auth related constants
    public static readonly TOKEN_EXPIRY_TIME = 3600000; // 1 hour in milliseconds
    public static readonly REFRESH_TOKEN_EXPIRY_TIME = 604800000; // 7 days in milliseconds
    public static readonly TOKEN_REFRESH_THRESHOLD = 300000; // 5 minutes in milliseconds
}
