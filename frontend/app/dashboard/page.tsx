"use client"

import { Sidebar } from "@/components/shared/sidebar"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { AnalyticsCard } from "@/components/shared/analytics-cards"
import Link from "next/link"
import { Upload, FileText, BarChart3 } from "lucide-react"

export default function DashboardPage() {
  const recentAnalyses = [
    { id: 1, name: "Resume_v3.pdf", date: "2 hours ago", score: 78 },
    { id: 2, name: "Resume_v2.pdf", date: "1 day ago", score: 72 },
    { id: 3, name: "Resume_v1.pdf", date: "3 days ago", score: 65 },
  ]

  return (
    <div className="flex">
      <Sidebar />
      <main className="flex-1 ml-64 min-h-screen bg-neutral-50">
        <div className="p-8 max-w-7xl mx-auto">
          {/* Header */}
          <div className="flex items-center justify-between mb-8">
            <div>
              <h1 className="text-4xl font-bold text-neutral-900">Welcome back!</h1>
              <p className="text-neutral-600 mt-1">Here's what's happening with your resumes</p>
            </div>
            <Link href="/upload">
              <Button className="bg-primary hover:bg-primary-dark gap-2">
                <Upload className="w-4 h-4" />
                Upload Resume
              </Button>
            </Link>
          </div>

          {/* Quick Actions */}
          <Card className="p-6 mb-8 bg-gradient-to-r from-primary/10 to-accent/10 border-primary/20">
            <h3 className="text-lg font-semibold mb-4">Quick Actions</h3>
            <div className="flex gap-4">
              <Link href="/upload" className="flex-1">
                <Button variant="outline" className="w-full justify-start bg-transparent">
                  <Upload className="w-4 h-4 mr-2" />
                  Upload New Resume
                </Button>
              </Link>
              <Link href="/jobs" className="flex-1">
                <Button variant="outline" className="w-full justify-start bg-transparent">
                  <FileText className="w-4 h-4 mr-2" />
                  View Job Matches
                </Button>
              </Link>
            </div>
          </Card>

          {/* Analytics */}
          <div className="grid md:grid-cols-3 gap-6 mb-8">
            <AnalyticsCard
              title="Total Analyzed"
              value="3"
              subtitle="resumes analyzed"
              icon={<BarChart3 className="w-6 h-6" />}
            />
            <AnalyticsCard title="Average Score" value="72%" subtitle="overall resume quality" trend={8} />
            <AnalyticsCard title="Job Matches" value="24" subtitle="matching opportunities" trend={12} />
          </div>

          {/* Recent Analyses */}
          <div>
            <h2 className="text-2xl font-bold mb-6 text-neutral-900">Recent Analyses</h2>
            <div className="space-y-4">
              {recentAnalyses.map((analysis) => (
                <Card
                  key={analysis.id}
                  className="p-4 flex items-center justify-between hover:shadow-md transition-shadow"
                >
                  <div className="flex items-center gap-4">
                    <FileText className="w-10 h-10 text-primary" />
                    <div>
                      <p className="font-semibold text-neutral-900">{analysis.name}</p>
                      <p className="text-sm text-neutral-600">{analysis.date}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-4">
                    <div className="text-right">
                      <p className="text-2xl font-bold text-primary">{analysis.score}%</p>
                      <p className="text-xs text-neutral-600">Score</p>
                    </div>
                    <Link href={`/analysis/${analysis.id}`}>
                      <Button variant="outline">View</Button>
                    </Link>
                  </div>
                </Card>
              ))}
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
