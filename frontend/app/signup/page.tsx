"use client"

import type React from "react"

import { useState } from "react"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import Link from "next/link"
import { Briefcase, Mail, Lock, User } from "lucide-react"

export default function SignupPage() {
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    password: "",
    confirmPassword: "",
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary/5 to-accent/5 flex items-center justify-center p-4">
      <Card className="w-full max-w-md p-8">
        <div className="text-center mb-8">
          <div className="flex items-center justify-center gap-2 mb-4">
            <Briefcase className="w-8 h-8 text-primary" />
            <span className="text-2xl font-bold text-primary">CareerCompass</span>
          </div>
          <h1 className="text-2xl font-bold text-neutral-900 mb-2">Create Account</h1>
          <p className="text-neutral-600">Start your career journey today</p>
        </div>

        <form className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="fullName" className="text-neutral-700">
              Full Name
            </Label>
            <div className="relative">
              <User className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
              <Input
                id="fullName"
                name="fullName"
                placeholder="John Doe"
                className="pl-10"
                value={formData.fullName}
                onChange={handleChange}
              />
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
                placeholder="your@email.com"
                className="pl-10"
                value={formData.email}
                onChange={handleChange}
              />
            </div>
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
                type="password"
                placeholder="••••••••"
                className="pl-10"
                value={formData.password}
                onChange={handleChange}
              />
            </div>
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
                type="password"
                placeholder="••••••••"
                className="pl-10"
                value={formData.confirmPassword}
                onChange={handleChange}
              />
            </div>
          </div>

          <label className="flex items-center gap-2">
            <input type="checkbox" className="w-4 h-4 rounded border-neutral-300" />
            <span className="text-sm text-neutral-600">
              I agree to the{" "}
              <a href="#" className="text-primary hover:text-primary-dark">
                Terms of Service
              </a>
            </span>
          </label>

          <Button className="w-full bg-primary hover:bg-primary-dark py-2">Create Account</Button>
        </form>

        <div className="my-6 relative">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-neutral-300"></div>
          </div>
          <div className="relative flex justify-center text-sm">
            <span className="px-2 bg-white text-neutral-500">Or continue with</span>
          </div>
        </div>

        <Button variant="outline" className="w-full bg-transparent">
          Continue with Google
        </Button>

        <p className="text-center text-neutral-600 text-sm mt-6">
          Already have an account?{" "}
          <Link href="/login" className="text-primary hover:text-primary-dark font-semibold">
            Sign in
          </Link>
        </p>
      </Card>
    </div>
  )
}
