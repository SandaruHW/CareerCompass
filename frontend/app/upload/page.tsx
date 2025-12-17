"use client"

import { useState } from "react"
import { Sidebar } from "@/components/shared/sidebar"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { FileUploader } from "@/components/shared/file-uploader"
import ResumeUploader from "@/components/shared/resume-uploader"
import { CheckCircle2, AlertCircle, Zap } from "lucide-react"

export default function UploadPage() {
  const [file, setFile] = useState<File | null>(null)
  const [isAnalyzing, setIsAnalyzing] = useState(false)

  const handleFileSelect = (selectedFile: File) => {
    setFile(selectedFile)
  }

  const handleAnalyze = async () => {
    if (!file) return
    setIsAnalyzing(true)
    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 2000))
    setIsAnalyzing(false)
    // Navigate to results
  }

  return (
    <div className="flex">
      <Sidebar />
      <main className="flex-1 ml-64 min-h-screen bg-neutral-50">
        <div className="p-8 max-w-4xl mx-auto">
          <h1 className="text-4xl font-bold text-neutral-900 mb-2">Upload Your Resume</h1>
          <p className="text-neutral-600 mb-8">Get instant AI-powered analysis and career insights</p>

          <div className="grid lg:grid-cols-3 gap-8">
            {/* Upload Section */}
            <div className="lg:col-span-2">
              <ResumeUploader />

              {file && (
                <div className="mt-8">
                  <Button
                    className="w-full bg-primary hover:bg-primary-dark py-3"
                    onClick={handleAnalyze}
                    disabled={isAnalyzing}
                  >
                    {isAnalyzing ? "Analyzing..." : "Analyze Resume"}
                  </Button>
                </div>
              )}
            </div>

            {/* Info Panel */}
            <div className="space-y-6">
              <Card className="p-6 bg-gradient-to-br from-primary/5 to-accent/5 border-primary/20">
                <h3 className="font-semibold text-neutral-900 mb-4 flex items-center gap-2">
                  <Zap className="w-5 h-5 text-primary" />
                  What You'll Get
                </h3>
                <ul className="space-y-3">
                  {[
                    "Extracted skills analysis",
                    "Missing skills identification",
                    "Job title recommendations",
                    "ATS score analysis",
                    "Improvement suggestions",
                  ].map((item, idx) => (
                    <li key={idx} className="flex items-start gap-2 text-sm">
                      <CheckCircle2 className="w-4 h-4 text-success flex-shrink-0 mt-0.5" />
                      <span className="text-neutral-700">{item}</span>
                    </li>
                  ))}
                </ul>
              </Card>

              <Card className="p-6 border-yellow-200 bg-yellow-50">
                <h3 className="font-semibold text-neutral-900 mb-3 flex items-center gap-2">
                  <AlertCircle className="w-5 h-5 text-warning" />
                  Pro Tips
                </h3>
                <ul className="space-y-2 text-sm text-neutral-700">
                  <li>• Use PDF format for best results</li>
                  <li>• Maximum file size: 5MB</li>
                  <li>• Analysis takes less than 30 seconds</li>
                </ul>
              </Card>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
