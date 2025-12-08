"use client"

import { Sidebar } from "@/components/shared/sidebar"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { SkillBadge } from "@/components/shared/skill-badge"
import { MapPin, Briefcase, DollarSign, Clock } from "lucide-react"

export default function JobDetailPage() {
  return (
    <div className="flex">
      <Sidebar />
      <main className="flex-1 ml-64 min-h-screen bg-neutral-50">
        <div className="p-8 max-w-4xl mx-auto">
          {/* Job Header */}
          <Card className="p-8 mb-8">
            <div className="flex items-start justify-between mb-6">
              <div>
                <h1 className="text-4xl font-bold text-neutral-900">Senior React Developer</h1>
                <p className="text-xl text-neutral-600 mt-2">Tech Corp</p>
              </div>
              <div className="text-right">
                <Badge className="bg-success text-white mb-2">92% Match</Badge>
                <p className="text-sm text-neutral-600">Highly Recommended</p>
              </div>
            </div>

            <div className="grid grid-cols-4 gap-4 py-6 border-y border-neutral-200">
              <div className="flex items-center gap-3">
                <MapPin className="w-5 h-5 text-neutral-400" />
                <div>
                  <p className="text-sm text-neutral-600">Location</p>
                  <p className="font-semibold">San Francisco, CA</p>
                </div>
              </div>
              <div className="flex items-center gap-3">
                <DollarSign className="w-5 h-5 text-neutral-400" />
                <div>
                  <p className="text-sm text-neutral-600">Salary</p>
                  <p className="font-semibold">$150K - $180K</p>
                </div>
              </div>
              <div className="flex items-center gap-3">
                <Clock className="w-5 h-5 text-neutral-400" />
                <div>
                  <p className="text-sm text-neutral-600">Experience</p>
                  <p className="font-semibold">5+ Years</p>
                </div>
              </div>
              <div className="flex items-center gap-3">
                <Briefcase className="w-5 h-5 text-neutral-400" />
                <div>
                  <p className="text-sm text-neutral-600">Job Type</p>
                  <p className="font-semibold">Full Time</p>
                </div>
              </div>
            </div>
          </Card>

          <div className="grid lg:grid-cols-3 gap-8">
            {/* Main Content */}
            <div className="lg:col-span-2 space-y-6">
              {/* Description */}
              <Card className="p-6">
                <h2 className="text-2xl font-bold mb-4 text-neutral-900">About the Role</h2>
                <p className="text-neutral-700 leading-relaxed mb-4">
                  We're looking for an experienced Senior React Developer to join our growing team. You'll work on
                  modern web applications, mentor junior developers, and help shape our technical architecture.
                </p>
                <p className="text-neutral-700 leading-relaxed">
                  This is a unique opportunity to work on challenging problems with cutting-edge technologies in a
                  fast-paced, collaborative environment.
                </p>
              </Card>

              {/* Required Skills */}
              <Card className="p-6">
                <h2 className="text-2xl font-bold mb-4 text-neutral-900">Required Skills</h2>
                <div className="space-y-4">
                  <div>
                    <p className="font-semibold text-neutral-900 mb-2">Your Skills:</p>
                    <div className="flex flex-wrap gap-2">
                      <SkillBadge skill="React" />
                      <SkillBadge skill="TypeScript" />
                      <SkillBadge skill="Node.js" />
                    </div>
                  </div>
                  <div>
                    <p className="font-semibold text-neutral-900 mb-2">Additional Requirements:</p>
                    <div className="flex flex-wrap gap-2">
                      <SkillBadge skill="GraphQL" variant="missing" />
                      <SkillBadge skill="Docker" variant="missing" />
                      <SkillBadge skill="AWS" variant="missing" />
                    </div>
                  </div>
                </div>
              </Card>

              {/* Recommended Improvements */}
              <Card className="p-6 bg-blue-50 border-blue-200">
                <h2 className="text-2xl font-bold mb-4 text-neutral-900">Recommendations to Improve Your Match</h2>
                <ul className="space-y-2">
                  <li className="flex gap-2">
                    <span className="text-primary font-bold">•</span>
                    <span className="text-neutral-700">Add GraphQL experience to your resume</span>
                  </li>
                  <li className="flex gap-2">
                    <span className="text-primary font-bold">•</span>
                    <span className="text-neutral-700">Highlight Docker containerization projects</span>
                  </li>
                  <li className="flex gap-2">
                    <span className="text-primary font-bold">•</span>
                    <span className="text-neutral-700">Include AWS architecture experience</span>
                  </li>
                </ul>
              </Card>
            </div>

            {/* Sidebar */}
            <div className="space-y-6">
              <Card className="p-6">
                <h3 className="font-bold text-neutral-900 mb-4">Application</h3>
                <Button className="w-full bg-primary hover:bg-primary-dark mb-3">Apply Now</Button>
                <Button variant="outline" className="w-full bg-transparent">
                  Save Job
                </Button>
              </Card>

              <Card className="p-6 bg-neutral-100">
                <h3 className="font-bold text-neutral-900 mb-3">Company Info</h3>
                <p className="text-sm text-neutral-700 mb-4">
                  Tech Corp is a leading SaaS company building tools for developers worldwide.
                </p>
                <Button variant="outline" className="w-full bg-transparent">
                  View Company Profile
                </Button>
              </Card>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
