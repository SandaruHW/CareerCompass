"use client"

import { Sidebar } from "@/components/shared/sidebar"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { JobCard } from "@/components/shared/job-card"
import { Search, Filter } from "lucide-react"
import { useState } from "react"

export default function JobsPage() {
  const [searchTerm, setSearchTerm] = useState("")

  const jobs = [
    {
      id: "1",
      title: "Senior React Developer",
      company: "Tech Corp",
      location: "San Francisco, CA",
      skills: ["React", "TypeScript", "Node.js"],
      matchScore: 92,
    },
    {
      id: "2",
      title: "Full Stack Engineer",
      company: "Startup Inc",
      location: "Remote",
      skills: ["React", "Python", "Node.js"],
      matchScore: 88,
    },
    {
      id: "3",
      title: "Frontend Architect",
      company: "Big Tech",
      location: "New York, NY",
      skills: ["React", "TypeScript", "Architecture"],
      matchScore: 85,
    },
    {
      id: "4",
      title: "JavaScript Developer",
      company: "Web Solutions",
      location: "Austin, TX",
      skills: ["JavaScript", "React", "CSS"],
      matchScore: 78,
    },
    {
      id: "5",
      title: "Principal Engineer",
      company: "Enterprise Co",
      location: "Boston, MA",
      skills: ["System Design", "Leadership", "TypeScript"],
      matchScore: 82,
    },
    {
      id: "6",
      title: "DevOps Engineer",
      company: "Cloud First",
      location: "Seattle, WA",
      skills: ["Docker", "Kubernetes", "AWS"],
      matchScore: 65,
    },
  ]

  const filteredJobs = jobs.filter(
    (job) =>
      job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      job.company.toLowerCase().includes(searchTerm.toLowerCase()),
  )

  return (
    <div className="flex">
      <Sidebar />
      <main className="flex-1 ml-64 min-h-screen bg-neutral-50">
        <div className="p-8 max-w-7xl mx-auto">
          {/* Header */}
          <div className="mb-8">
            <h1 className="text-4xl font-bold text-neutral-900 mb-2">Job Opportunities</h1>
            <p className="text-neutral-600">Found {filteredJobs.length} matching opportunities</p>
          </div>

          {/* Search & Filter */}
          <Card className="p-4 mb-8 flex gap-4 items-center">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-3 w-5 h-5 text-neutral-400" />
              <Input
                placeholder="Search jobs or companies..."
                className="pl-10"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
            <Button variant="outline" className="gap-2 bg-transparent">
              <Filter className="w-4 h-4" />
              Filters
            </Button>
          </Card>

          {/* Jobs Grid */}
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredJobs.length > 0 ? (
              filteredJobs.map((job) => <JobCard key={job.id} {...job} />)
            ) : (
              <div className="col-span-full text-center py-12">
                <p className="text-neutral-600">No jobs found matching your search.</p>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  )
}
