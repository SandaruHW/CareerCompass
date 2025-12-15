"use client"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useToast } from "@/hooks/use-toast"
import { useAuth } from "@/lib/auth-context"
import Link from "next/link"
import { Briefcase, User, Mail, Lock, Phone, Eye, EyeOff } from "lucide-react"

export default function SignupPage() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    password: "",
    confirmPassword: "",
    acceptTerms: false,
  })
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  
  const { register } = useAuth()
  const router = useRouter()
  const { toast } = useToast()

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }))
  }

  const handlePhoneChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value.replace(/\D/g, '') // Remove non-digits
    
    // Add +94 prefix if not present
    if (value.length > 0 && !value.startsWith('94')) {
      // If starts with 0, replace with 94
      if (value.startsWith('0')) {
        value = '94' + value.substring(1)
      } else if (!value.startsWith('94')) {
        value = '94' + value
      }
    }
    
    // Format the number
    let formatted = ''
    if (value.length >= 2) {
      formatted = '+' + value.substring(0, 2) // +94
      if (value.length > 2) {
        formatted += ' ' + value.substring(2, 4) // Area/operator code
        if (value.length > 4) {
          formatted += ' ' + value.substring(4, 7) // First 3 digits
          if (value.length > 7) {
            formatted += ' ' + value.substring(7, 11) // Last 4 digits
          }
        }
      }
    } else if (value.length > 0) {
      formatted = '+' + value
    }
    
    setFormData(prev => ({
      ...prev,
      phoneNumber: formatted
    }))
  }

  const validateForm = () => {
    if (formData.password !== formData.confirmPassword) {
      toast({
        title: "Passwords don't match",
        description: "Please make sure both password fields match.",
        variant: "destructive",
      })
      return false
    }

    if (formData.password.length < 8) {
      toast({
        title: "Password too short",
        description: "Password must be at least 8 characters long.",
        variant: "destructive",
      })
      return false
    }

    // Validate Sri Lankan phone number
    if (formData.phoneNumber) {
      const phoneRegex = /^\+94\s?(7[01245678]|[1-9][1-9])\s?\d{3}\s?\d{4}$/
      const cleanPhone = formData.phoneNumber.replace(/\s/g, '')
      if (!phoneRegex.test(cleanPhone)) {
        toast({
          title: "Invalid phone number",
          description: "Please enter a valid Sri Lankan phone number (+94 77 123 4567).",
          variant: "destructive",
        })
        return false
      }
    }

    if (!formData.acceptTerms) {
      toast({
        title: "Terms not accepted",
        description: "Please accept the terms and conditions to continue.",
        variant: "destructive",
      })
      return false
    }

    return true
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!validateForm()) {
      return
    }

    setIsLoading(true)

    try {
      const registrationData = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        phoneNumber: formData.phoneNumber.replace(/\s/g, ''), // Remove spaces for backend
        password: formData.password,
      }

      await register(registrationData)
      
      toast({
        title: "Account created successfully!",
        description: "Welcome to CareerCompass. You are now logged in.",
      })
      
      router.push("/dashboard")
    } catch (error: any) {
      console.error("Registration failed:", error)
      toast({
        title: "Registration failed",
        description: error.message || "Please try again with different details.",
        variant: "destructive",
      })
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary/5 to-accent/5 flex items-center justify-center p-4">
      <Card className="w-full max-w-lg p-8">
        <div className="text-center mb-8">
          <div className="flex items-center justify-center gap-2 mb-4">
            <Briefcase className="w-8 h-8 text-primary" />
            <span className="text-2xl font-bold text-primary">CareerCompass</span>
          </div>
          <h1 className="text-2xl font-bold text-neutral-900 mb-2">Create Account</h1>
          <p className="text-neutral-600">Sign up to start your career journey</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="firstName" className="text-neutral-700">
                First Name
              </Label>
              <div className="relative">
                <User className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
                <Input
                  id="firstName"
                  name="firstName"
                  placeholder="John"
                  value={formData.firstName}
                  onChange={handleChange}
                  className="pl-10"
                  required
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label htmlFor="lastName" className="text-neutral-700">
                Last Name
              </Label>
              <div className="relative">
                <User className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
                <Input
                  id="lastName"
                  name="lastName"
                  placeholder="Doe"
                  value={formData.lastName}
                  onChange={handleChange}
                  className="pl-10"
                  required
                />
              </div>
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="email" className="text-neutral-700">
              Email
            </Label>
            <div className="relative">
              <Mail className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
              <Input
                id="email"
                name="email"
                type="email"
                placeholder="john@example.com"
                value={formData.email}
                onChange={handleChange}
                className="pl-10"
                required
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="phoneNumber" className="text-neutral-700">
              Phone Number
            </Label>
            <div className="relative">
              <Phone className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
              <Input
                id="phoneNumber"
                name="phoneNumber"
                type="tel"
                placeholder="+94 77 123 4567"
                value={formData.phoneNumber}
                onChange={handlePhoneChange}
                className="pl-10"
                maxLength={15}
              />
            </div>
            <p className="text-xs text-neutral-500">
              Sri Lankan format: +94 77 123 4567 or +94 11 234 5678
            </p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="password" className="text-neutral-700">
              Password
            </Label>
            <div className="relative">
              <Lock className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
              <Input
                id="password"
                name="password"
                type={showPassword ? "text" : "password"}
                placeholder="Create a strong password"
                value={formData.password}
                onChange={handleChange}
                className="pl-10 pr-10"
                required
                minLength={8}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-3 text-neutral-400 hover:text-neutral-600"
              >
                {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
              </button>
            </div>
            <p className="text-xs text-neutral-500">
              At least 8 characters with letters and numbers
            </p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="confirmPassword" className="text-neutral-700">
              Confirm Password
            </Label>
            <div className="relative">
              <Lock className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
              <Input
                id="confirmPassword"
                name="confirmPassword"
                type={showConfirmPassword ? "text" : "password"}
                placeholder="Confirm your password"
                value={formData.confirmPassword}
                onChange={handleChange}
                className="pl-10 pr-10"
                required
              />
              <button
                type="button"
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                className="absolute right-3 top-3 text-neutral-400 hover:text-neutral-600"
              >
                {showConfirmPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
              </button>
            </div>
          </div>

          <div className="flex items-start space-x-2">
            <input
              id="acceptTerms"
              name="acceptTerms"
              type="checkbox"
              checked={formData.acceptTerms}
              onChange={handleChange}
              className="mt-1 rounded border-neutral-300"
              required
            />
            <Label htmlFor="acceptTerms" className="text-sm text-neutral-600 leading-tight">
              I agree to the{" "}
              <Link href="/terms" className="text-primary hover:underline">
                Terms of Service
              </Link>{" "}
              and{" "}
              <Link href="/privacy" className="text-primary hover:underline">
                Privacy Policy
              </Link>
            </Label>
          </div>

          <Button 
            type="submit" 
            className="w-full"
            disabled={isLoading}
          >
            {isLoading ? "Creating Account..." : "Create Account"}
          </Button>

          <div className="text-center">
            <span className="text-neutral-600">Already have an account? </span>
            <Link href="/login" className="text-primary hover:underline font-medium">
              Sign in
            </Link>
          </div>
        </form>
      </Card>
    </div>
  )
}
