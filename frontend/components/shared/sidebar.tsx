"use client"

import { Button } from "@/components/ui/button"
import Link from "next/link"
import { useAuth } from "@/lib/auth-context"
import { useToast } from "@/hooks/use-toast"
import { LayoutDashboard, Briefcase, FileText, Settings, LogOut, BarChart3 } from "lucide-react"
import { usePathname } from "next/navigation"

interface SidebarProps {
  isAdmin?: boolean
}

export function Sidebar({ isAdmin = false }: SidebarProps) {
  const pathname = usePathname()
  const { logout } = useAuth()
  const { toast } = useToast()

  const handleLogout = async () => {
    try {
      await logout()
      toast({
        title: "Logged out successfully",
        description: "You have been logged out of your account.",
      })
      // Force a page reload to ensure clean state
      window.location.href = '/'
    } catch (error) {
      console.error('Logout failed:', error)
      toast({
        title: "Logout completed",
        description: "You have been logged out.",
      })
      // Force redirect even if logout fails
      window.location.href = '/'
    }
  }

  const menuItems = [
    { icon: LayoutDashboard, label: "Dashboard", href: "/dashboard" },
    { icon: FileText, label: "Resumes", href: "/resumes" },
    { icon: Briefcase, label: "Jobs", href: "/jobs" },
    ...(isAdmin ? [{ icon: BarChart3, label: "Admin", href: "/admin" }] : []),
  ]

  return (
    <aside className="w-64 bg-neutral-900 text-white p-6 min-h-screen fixed left-0 top-0">
      <Link href="/dashboard" className="flex items-center gap-2 mb-8 hover:opacity-80 transition">
        <Briefcase className="w-8 h-8 text-accent" />
        <span className="text-xl font-bold">CareerCompass</span>
      </Link>

      <nav className="space-y-2 mb-12">
        {menuItems.map((item) => (
          <Link
            key={item.href}
            href={item.href}
            className={`flex items-center gap-3 px-4 py-3 rounded-lg transition ${
              pathname === item.href
                ? "bg-primary text-white"
                : "text-neutral-400 hover:bg-neutral-800 hover:text-white"
            }`}
          >
            <item.icon className="w-5 h-5" />
            {item.label}
          </Link>
        ))}
      </nav>

      <div className="absolute bottom-6 left-6 right-6">
        <Link
          href="/settings"
          className="flex items-center gap-3 px-4 py-3 rounded-lg text-neutral-400 hover:bg-neutral-800 hover:text-white transition mb-2 w-full"
        >
          <Settings className="w-5 h-5" />
          Settings
        </Link>
        <Button 
          variant="outline" 
          className="w-full justify-start gap-3 bg-transparent"
          onClick={handleLogout}
        >
          <LogOut className="w-5 h-5" />
          Logout
        </Button>
      </div>
    </aside>
  )
}
