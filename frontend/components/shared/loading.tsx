"use client"

import { Briefcase } from "lucide-react"

export default function LoadingPage() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-primary/5 to-accent/5 flex items-center justify-center">
      <div className="text-center">
        <div className="flex items-center justify-center gap-2 mb-4">
          <div className="animate-spin">
            <Briefcase className="w-8 h-8 text-primary" />
          </div>
          <span className="text-2xl font-bold text-primary">CareerCompass</span>
        </div>
        <p className="text-neutral-600">Loading your career journey...</p>
      </div>
    </div>
  )
}