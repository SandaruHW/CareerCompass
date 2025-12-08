import type React from "react"
import { Card } from "@/components/ui/card"
import { TrendingUp } from "lucide-react"

interface AnalyticsCardProps {
  title: string
  value: string | number
  subtitle?: string
  icon?: React.ReactNode
  trend?: number
}

export function AnalyticsCard({ title, value, subtitle, icon, trend }: AnalyticsCardProps) {
  return (
    <Card className="p-6">
      <div className="flex items-start justify-between">
        <div>
          <p className="text-neutral-600 text-sm font-medium">{title}</p>
          <p className="text-3xl font-bold text-neutral-900 mt-2">{value}</p>
          {subtitle && <p className="text-neutral-500 text-sm mt-1">{subtitle}</p>}
        </div>
        <div className="flex items-center gap-2">
          {icon && <div className="text-primary">{icon}</div>}
          {trend && (
            <div className="flex items-center gap-1 text-success">
              <TrendingUp className="w-4 h-4" />
              <span className="text-sm font-semibold">{trend}%</span>
            </div>
          )}
        </div>
      </div>
    </Card>
  )
}
