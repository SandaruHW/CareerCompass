// lib/auth-service.ts
export interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}

export interface RegisterRequest {
  email: string;
  username?: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  role: string;
  emailVerified: boolean;
  lastLoginAt?: string;
}

export interface User {
  id: number;
  email: string;
  username?: string;
  firstName: string;
  lastName: string;
  fullName: string;
  phoneNumber?: string;
  role: string;
  enabled: boolean;
  emailVerified: boolean;
  accountLocked: boolean;
  lastLoginAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ApiError {
  message: string;
  status: number;
  timestamp: string;
  path: string;
}

class AuthService {
  private readonly API_BASE_URL = 'http://localhost:8080/api';
  private readonly TOKEN_KEY = 'careercompass_token';
  private readonly REFRESH_TOKEN_KEY = 'careercompass_refresh_token';

  // Make HTTP request
  private async makeRequest<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.API_BASE_URL}${endpoint}`;
    
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    };

    // Add auth token if available
    const token = this.getToken();
    if (token) {
      config.headers = {
        ...config.headers,
        Authorization: `Bearer ${token}`,
      };
    }

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({
          message: `HTTP ${response.status}: ${response.statusText}`,
          status: response.status,
          timestamp: new Date().toISOString(),
          path: endpoint,
        }));
        
        console.error('API Error Response:', {
          status: response.status,
          endpoint,
          errorData
        });
        
        // Handle specific login errors - pass through the backend error message
        if (endpoint === '/auth/login' && (response.status === 401 || response.status === 403)) {
          // Backend now provides specific messages: "Email not found", "Password is incorrect", etc.
          throw errorData;
        }
        
        throw errorData;
      }

      // Handle no content responses
      if (response.status === 204) {
        return {} as T;
      }

      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  // User registration
  async register(data: RegisterRequest): Promise<AuthResponse> {
    const response = await this.makeRequest<AuthResponse>('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    });

    this.setTokens(response.token, response.refreshToken);
    return response;
  }

  // User login
  async login(data: LoginRequest): Promise<AuthResponse> {
    const response = await this.makeRequest<AuthResponse>('/auth/login', {
      method: 'POST',
      body: JSON.stringify(data),
    });

    this.setTokens(response.token, response.refreshToken);
    return response;
  }

  // Get current user
  async getCurrentUser(): Promise<User> {
    return await this.makeRequest<User>('/auth/me');
  }

  // Refresh token
  async refreshToken(): Promise<AuthResponse> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) {
      throw new Error('No refresh token available');
    }

    const response = await this.makeRequest<AuthResponse>('/auth/refresh', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${refreshToken}`,
      },
    });

    this.setTokens(response.token, response.refreshToken);
    return response;
  }

  // Logout
  async logout(): Promise<void> {
    try {
      await this.makeRequest<void>('/auth/logout', {
        method: 'POST',
      });
    } catch (error) {
      console.error('Logout request failed:', error);
    } finally {
      this.clearTokens();
    }
  }

  // Token management
  private setTokens(token: string, refreshToken: string): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem(this.TOKEN_KEY, token);
      localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
      
      // Set cookies for middleware with 30 day expiry
      const cookieOptions = 'path=/; max-age=2592000; SameSite=lax';
      document.cookie = `auth_token=${token}; ${cookieOptions}`;
      document.cookie = `refresh_token=${refreshToken}; ${cookieOptions}`;
    }
  }

  getToken(): string | null {
    if (typeof window === 'undefined') return null;
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private getRefreshToken(): string | null {
    if (typeof window === 'undefined') return null;
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  private clearTokens(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem(this.TOKEN_KEY);
      localStorage.removeItem(this.REFRESH_TOKEN_KEY);
      
      // Clear cookies by setting them to expire immediately
      document.cookie = 'auth_token=; path=/; max-age=0';
      document.cookie = 'refresh_token=; path=/; max-age=0';
    }
  }

  // Check if user is authenticated
  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      // Basic JWT structure check
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Date.now() / 1000;
      return payload.exp > currentTime;
    } catch (error) {
      console.error('Token validation error:', error);
      return false;
    }
  }
}

export const authService = new AuthService();