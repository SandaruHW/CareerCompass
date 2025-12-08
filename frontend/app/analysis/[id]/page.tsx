"use client"

import { Sidebar } from "@/components/shared/sidebar"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { SkillBadge } from "@/components/shared/skill-badge"
import { JobCard } from "@/components/shared/job-card"
import { AlertCircle, TrendingUp, Download, Share2 } from "lucide-react"
import { RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, Radar, ResponsiveContainer } from "recharts"

export default function AnalysisPage() {
  const radarData = [
    { skill: "Communication", value: 85 },
    { skill: "Technical", value: 78 },
    { skill: "Leadership", value: 72 },
    { skill: "Problem Solving", value: 88 },
    { skill: "Teamwork", value: 90 },
  ]

  const extractedSkills = [
    { name: "React", confidence: 0.95 },
    { name: "TypeScript", confidence: 0.92 },
    { name: "Node.js", confidence: 0.88 },
    { name: "Python", confidence: 0.85 },
  ]

  const missingSkills = ["Cloud Architecture", "Docker & Kubernetes", "GraphQL"]

  const improvements = [
    "Add quantifiable achievements (e.g., performance improvements)",
    "Include more industry keywords from your target roles",
    "Add certifications and continuous learning initiatives",
    "Highlight leadership and mentoring experiences",
  ]

  const recommendedJobs = [
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
  ]

  return (
    <div className="flex">
      <Sidebar />
      <main className="flex-1 ml-64 min-h-screen bg-neutral-50">
        <div className="p-8 max-w-7xl mx-auto">
          {/* Header */}
          <div className="flex items-center justify-between mb-8">
            <div>
              <h1 className="text-4xl font-bold text-neutral-900">Analysis Results</h1>
              <p className="text-neutral-600 mt-1">Resume_v3.pdf • Analyzed 2 hours ago</p>
            </div>
            <div className="flex gap-4">
              <Button variant="outline" className="gap-2 bg-transparent">
                <Download className="w-4 h-4" />
                Download Report
              </Button>
              <Button variant="outline" className="gap-2 bg-transparent">
                <Share2 className="w-4 h-4" />
                Share
              </Button>
            </div>
          </div>

          <div className="grid lg:grid-cols-3 gap-8">
            {/* Main Content */}
            <div className="lg:col-span-2 space-y-8">
              {/* Overall Score */}
              <Card className="p-8 bg-gradient-to-br from-primary/5 to-accent/5 border-primary/20">
                <div className="flex items-center gap-8">
                  <div>
                    <p className="text-neutral-600 text-sm font-medium">Overall Score</p>
                    <p className="text-6xl font-bold text-primary mt-2">78%</p>
                    <p className="text-neutral-600 mt-2">Great resume! Keep improving.</p>
                  </div>
                  <div className="flex-1">
                    <ResponsiveContainer width="100%" height={200}>
                      <RadarChart data={radarData}>
                        <PolarGrid stroke="#e2e8f0" />
                        <PolarAngleAxis dataKey="skill" />
                        <PolarRadiusAxis />
                        <Radar name="Skills" dataKey="value" stroke="#4f46e5" fill="#4f46e5" fillOpacity={0.6} />
                      </RadarChart>
                    </ResponsiveContainer>
                  </div>
                </div>
              </Card>

              {/* Extracted Skills */}
              <Card className="p-6">
                <h2 className="text-2xl font-bold mb-6 text-neutral-900">Extracted Skills</h2>
                <div className="flex flex-wrap gap-3">
                  {extractedSkills.map((skill) => (
                    <SkillBadge key={skill.name} skill={skill.name} confidence={skill.confidence} />
                  ))}
                </div>
              </Card>

              {/* Missing Skills */}
              <Card className="p-6 border-red-200 bg-red-50">
                <h2 className="text-2xl font-bold mb-4 text-neutral-900 flex items-center gap-2">
                  <AlertCircle className="w-6 h-6 text-error" />
                  Missing Skills
                </h2>
                <p className="text-neutral-600 mb-4">Consider adding these skills to improve your matches:</p>
                <div className="flex flex-wrap gap-3">
                  {missingSkills.map((skill) => (
                    <SkillBadge key={skill} skill={skill} variant="missing" />
                  ))}
                </div>
              </Card>

              {/* AI Suggestions */}
              <Card className="p-6">
                <h2 className="text-2xl font-bold mb-6 text-neutral-900 flex items-center gap-2">
                  <TrendingUp className="w-6 h-6 text-primary" />
                  AI Improvement Suggestions
                </h2>
                <div className="space-y-4">
                  {improvements.map((improvement, idx) => (
                    <div key={idx} className="p-4 bg-neutral-50 rounded-lg border border-neutral-200">
                      <p className="text-neutral-900 font-medium">
                        {idx + 1}. {improvement}
                      </p>
                    </div>
                  ))}
                </div>
              </Card>
            </div>

            {/* Sidebar */}
            <div className="space-y-6">
              <Card className="p-6">
                <h3 className="font-bold text-neutral-900 mb-4">Analysis Summary</h3>
                <div className="space-y-4">
                  <div>
                    <p className="text-sm text-neutral-600">Skills Detected</p>
                    <p className="text-2xl font-bold text-primary">{extractedSkills.length}</p>
                  </div>
                  <div>
                    <p className="text-sm text-neutral-600">Match Score</p>
                    <p className="text-2xl font-bold text-primary">78%</p>
                  </div>
                  <div>
                    <p className="text-sm text-neutral-600">Job Matches</p>
                    <p className="text-2xl font-bold text-primary">24</p>
                  </div>
                </div>
              </Card>

              <Card className="p-6 bg-accent/5 border-accent/20">
                <h3 className="font-bold text-neutral-900 mb-3">Next Steps</h3>
                <ol className="space-y-2 text-sm text-neutral-700">
                  <li>1. Add missing skills</li>
                  <li>2. Implement suggestions</li>
                  <li>3. Re-analyze resume</li>
                  <li>4. Apply to matched jobs</li>
                </ol>
              </Card>
            </div>
          </div>

          {/* Recommended Jobs */}
          <div className="mt-12">
            <h2 className="text-3xl font-bold mb-6 text-neutral-900">Recommended Jobs</h2>
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
              {recommendedJobs.map((job) => (
                <JobCard key={job.id} {...job} />
              ))}
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
