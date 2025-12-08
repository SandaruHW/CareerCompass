"use client"

import { Sidebar } from "@/components/shared/sidebar"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { AnalyticsCard } from "@/components/shared/analytics-cards"
import { BarChart3, Users, Briefcase, Plus, Edit, Trash2, Search } from "lucide-react"
import { useState } from "react"
import { Input } from "@/components/ui/input"

export default function AdminPage() {
  const [showAddModal, setShowAddModal] = useState(false)

  const jobs = [
    { id: 1, title: "Senior React Developer", company: "Tech Corp", applicants: 24, status: "Active" },
    { id: 2, title: "Full Stack Engineer", company: "Startup Inc", applicants: 18, status: "Active" },
    { id: 3, title: "Frontend Architect", company: "Big Tech", applicants: 31, status: "Active" },
  ]

  const stats = [
    { title: "Daily Uploads", value: 156, icon: <Briefcase className="w-6 h-6" />, trend: 12 },
    { title: "Total Users", value: 2840, icon: <Users className="w-6 h-6" />, trend: 8 },
    { title: "Active Jobs", value: 127, icon: <BarChart3 className="w-6 h-6" />, trend: -3 },
  ]

  const topSkills = ["React (892)", "TypeScript (756)", "Node.js (634)", "Python (521)", "AWS (487)"]

  const topRoles = [
    "Senior Developer (234)",
    "Software Engineer (189)",
    "Frontend Engineer (156)",
    "Full Stack Developer (142)",
    "DevOps Engineer (98)",
  ]

  return (
    <div className="flex">
      <Sidebar isAdmin={true} />
      <main className="flex-1 ml-64 min-h-screen bg-neutral-50">
        <div className="p-8 max-w-7xl mx-auto">
          {/* Header */}
          <div className="flex items-center justify-between mb-8">
            <div>
              <h1 className="text-4xl font-bold text-neutral-900">Admin Dashboard</h1>
              <p className="text-neutral-600 mt-1">Manage jobs, users, and platform analytics</p>
            </div>
            <Button className="bg-primary hover:bg-primary-dark gap-2">
              <Plus className="w-4 h-4" />
              Add Job
            </Button>
          </div>

          {/* Analytics */}
          <div className="grid md:grid-cols-3 gap-6 mb-8">
            {stats.map((stat, idx) => (
              <AnalyticsCard key={idx} title={stat.title} value={stat.value} icon={stat.icon} trend={stat.trend} />
            ))}
          </div>

          <div className="grid lg:grid-cols-3 gap-8 mb-8">
            {/* Jobs Management */}
            <div className="lg:col-span-2">
              <Card className="p-6">
                <div className="flex items-center justify-between mb-6">
                  <h2 className="text-2xl font-bold text-neutral-900">Jobs Management</h2>
                </div>

                <div className="relative mb-4">
                  <Search className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
                  <Input placeholder="Search jobs..." className="pl-10" />
                </div>

                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead>
                      <tr className="border-b border-neutral-200">
                        <th className="text-left py-3 px-4 font-semibold text-neutral-900">Job Title</th>
                        <th className="text-left py-3 px-4 font-semibold text-neutral-900">Company</th>
                        <th className="text-center py-3 px-4 font-semibold text-neutral-900">Applicants</th>
                        <th className="text-center py-3 px-4 font-semibold text-neutral-900">Status</th>
                        <th className="text-right py-3 px-4 font-semibold text-neutral-900">Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {jobs.map((job) => (
                        <tr key={job.id} className="border-b border-neutral-100 hover:bg-neutral-50">
                          <td className="py-3 px-4">{job.title}</td>
                          <td className="py-3 px-4 text-neutral-600">{job.company}</td>
                          <td className="py-3 px-4 text-center font-semibold">{job.applicants}</td>
                          <td className="py-3 px-4 text-center">
                            <span className="px-2 py-1 rounded-full text-xs font-semibold bg-success/10 text-success">
                              {job.status}
                            </span>
                          </td>
                          <td className="py-3 px-4 text-right flex justify-end gap-2">
                            <button className="p-2 hover:bg-neutral-200 rounded transition">
                              <Edit className="w-4 h-4 text-neutral-600" />
                            </button>
                            <button className="p-2 hover:bg-red-100 rounded transition">
                              <Trash2 className="w-4 h-4 text-error" />
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </Card>
            </div>

            {/* Analytics Sidebar */}
            <div className="space-y-6">
              <Card className="p-6">
                <h3 className="font-bold text-neutral-900 mb-4">Top Missing Skills</h3>
                <div className="space-y-3">
                  {topSkills.map((skill, idx) => (
                    <div key={idx} className="flex items-center justify-between">
                      <span className="text-neutral-700">{skill.split(" (")[0]}</span>
                      <span className="text-sm font-semibold text-primary">{skill.split(" (")[1].split(")")[0]}</span>
                    </div>
                  ))}
                </div>
              </Card>

              <Card className="p-6">
                <h3 className="font-bold text-neutral-900 mb-4">Most Popular Roles</h3>
                <div className="space-y-3">
                  {topRoles.map((role, idx) => (
                    <div key={idx} className="flex items-center justify-between">
                      <span className="text-neutral-700">{role.split(" (")[0]}</span>
                      <span className="text-sm font-semibold text-primary">{role.split(" (")[1].split(")")[0]}</span>
                    </div>
                  ))}
                </div>
              </Card>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
