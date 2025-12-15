'use client'

import React, { createContext, useContext, useEffect, useState } from 'react'
import { authService, type User, type AuthResponse, type LoginRequest, type RegisterRequest } from '@/lib/auth-service'

interface AuthContextType {
  user: User | null
  loading: boolean
  isAuthenticated: boolean
  login: (data: LoginRequest) => Promise<AuthResponse>
  register: (data: RegisterRequest) => Promise<AuthResponse>
  logout: () => Promise<void>
  refreshUser: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

interface AuthProviderProps {
  children: React.ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(true)

  const isAuthenticated = !!user && authService.isAuthenticated()

  // Load user on mount
  useEffect(() => {
    loadUser()
  }, [])

  const loadUser = async () => {
    try {
      setLoading(true)
      
      if (!authService.isAuthenticated()) {
        setUser(null)
        return
      }

      const userData = await authService.getCurrentUser()
      setUser(userData)
    } catch (error) {
      console.error('Failed to load user:', error)
      setUser(null)
      // Clear invalid tokens
      await authService.logout()
    } finally {
      setLoading(false)
    }
  }

  const login = async (data: LoginRequest): Promise<AuthResponse> => {
    try {
      setLoading(true)
      const response = await authService.login(data)
      
      // Load user data after successful login
      await loadUser()
      
      return response
    } catch (error) {
      setLoading(false)
      throw error
    }
  }

  const register = async (data: RegisterRequest): Promise<AuthResponse> => {
    try {
      setLoading(true)
      const response = await authService.register(data)
      
      // Load user data after successful registration
      await loadUser()
      
      return response
    } catch (error) {
      setLoading(false)
      throw error
    }
  }

  const logout = async (): Promise<void> => {
    try {
      setLoading(true)
      await authService.logout()
      setUser(null)
    } catch (error) {
      console.error('Logout failed:', error)
      // Still clear local state even if logout request fails
      setUser(null)
    } finally {
      setLoading(false)
    }
  }

  const refreshUser = async (): Promise<void> => {
    if (!authService.isAuthenticated()) {
      setUser(null)
      return
    }

    try {
      const userData = await authService.getCurrentUser()
      setUser(userData)
    } catch (error) {
      console.error('Failed to refresh user:', error)
      setUser(null)
      await logout()
    }
  }

  const value: AuthContextType = {
    user,
    loading,
    isAuthenticated,
    login,
    register,
    logout,
    refreshUser,
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}