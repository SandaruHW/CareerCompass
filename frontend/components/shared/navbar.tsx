"use client"

import { Button } from "@/components/ui/button"
import Link from "next/link"
import { Briefcase } from "lucide-react"

interface NavbarProps {
  authenticated?: boolean
}

export function Navbar({ authenticated = false }: NavbarProps) {
  return (
    <nav className="sticky top-0 z-50 bg-white border-b border-neutral-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex items-center justify-between">
        <Link href="/" className="flex items-center gap-2 hover:opacity-80 transition">
          <Briefcase className="w-8 h-8 text-primary" />
          <span className="text-2xl font-bold text-primary">CareerCompass</span>
        </Link>

        {authenticated ? (
          <div className="flex items-center gap-4">
            <Link href="/dashboard">
              <Button variant="ghost">Dashboard</Button>
            </Link>
            <Link href="/jobs">
              <Button variant="ghost">Jobs</Button>
            </Link>
            <Button variant="outline">Logout</Button>
          </div>
        ) : (
          <div className="flex items-center gap-4">
            <Link href="/login">
              <Button variant="outline">Sign In</Button>
            </Link>
            <Link href="/signup">
              <Button className="bg-primary hover:bg-primary-dark">Sign Up</Button>
            </Link>
          </div>
        )}
      </div>
    </nav>
  )
}
