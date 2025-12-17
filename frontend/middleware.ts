import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

// Define protected routes that require authentication
const protectedRoutes = [
  '/dashboard',
  '/upload',
  '/jobs',
  '/analysis',
  '/admin'
]

// Define public routes that should redirect to dashboard if user is already authenticated
const publicRoutes = [
  '/login',
  '/signup'
]

// Helper function to validate JWT token
function isValidToken(token: string): boolean {
  try {
    // Basic JWT structure check
    const parts = token.split('.')
    if (parts.length !== 3) return false
    
    // Decode payload
    const payload = JSON.parse(atob(parts[1]))
    const currentTime = Date.now() / 1000
    
    // Check if token is not expired
    return payload.exp > currentTime
  } catch (error) {
    return false
  }
}

export default function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl
  const token = request.cookies.get('auth_token')?.value

  // Check if the current path is a protected route
  const isProtectedRoute = protectedRoutes.some(route => 
    pathname.startsWith(route)
  )

  // Check if the current path is a public route
  const isPublicRoute = publicRoutes.some(route => 
    pathname.startsWith(route)
  )

  // Validate token if it exists
  const isAuthenticated = token && isValidToken(token)

  // If user is not authenticated and trying to access protected route
  if (isProtectedRoute && !isAuthenticated) {
    const loginUrl = new URL('/login', request.url)
    loginUrl.searchParams.set('from', pathname)
    return NextResponse.redirect(loginUrl)
  }

  // If user is authenticated and trying to access public routes (login/signup)
  if (isPublicRoute && isAuthenticated) {
    return NextResponse.redirect(new URL('/dashboard', request.url))
  }

  // If token exists but is invalid, clear it by redirecting to login with instructions to clear cookies
  if (isPublicRoute && token && !isAuthenticated) {
    const response = NextResponse.next()
    // Clear invalid tokens
    response.cookies.set('auth_token', '', { maxAge: 0 })
    response.cookies.set('refresh_token', '', { maxAge: 0 })
    return response
  }

  return NextResponse.next()
}

export { middleware }

export const config = {
  matcher: [
    // Match all paths except static files and API routes
    '/((?!api|_next/static|_next/image|favicon.ico|.*\\..*$).*)',
  ],
}