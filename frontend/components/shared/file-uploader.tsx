"use client"

import type React from "react"

import { useState } from "react"
import { Card } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Upload, File, X } from "lucide-react"

interface FileUploaderProps {
  onFileSelect?: (file: File) => void
  accept?: string
  maxSize?: number
}

export function FileUploader({
  onFileSelect,
  accept = ".pdf,.doc,.docx",
  maxSize = 5 * 1024 * 1024,
}: FileUploaderProps) {
  const [file, setFile] = useState<File | null>(null)
  const [isDragging, setIsDragging] = useState(false)

  const handleFile = (selectedFile: File) => {
    if (selectedFile.size > maxSize) {
      alert("File is too large. Maximum size is 5MB.")
      return
    }
    setFile(selectedFile)
    onFileSelect?.(selectedFile)
  }

  const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault()
    setIsDragging(false)
    const files = e.dataTransfer.files
    if (files.length > 0) {
      handleFile(files[0])
    }
  }

  return (
    <div className="space-y-4">
      <Card
        onDrop={handleDrop}
        onDragOver={(e) => {
          e.preventDefault()
          setIsDragging(true)
        }}
        onDragLeave={() => setIsDragging(false)}
        className={`p-12 border-2 border-dashed transition cursor-pointer ${
          isDragging ? "border-primary bg-primary/5" : "border-neutral-300 hover:border-primary"
        }`}
      >
        <div className="text-center">
          <Upload className="w-12 h-12 text-neutral-400 mx-auto mb-4" />
          <h3 className="text-lg font-semibold mb-2">Drop your resume here</h3>
          <p className="text-neutral-600 mb-4">or click to browse</p>
          <input
            type="file"
            id="file-input"
            className="hidden"
            accept={accept}
            onChange={(e) => {
              if (e.target.files?.[0]) {
                handleFile(e.target.files[0])
              }
            }}
          />
          <label htmlFor="file-input">
            <Button asChild className="bg-primary hover:bg-primary-dark">
              <span>Select File</span>
            </Button>
          </label>
        </div>
      </Card>

      {file && (
        <Card className="p-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <File className="w-6 h-6 text-primary" />
            <div>
              <p className="font-medium">{file.name}</p>
              <p className="text-sm text-neutral-600">{(file.size / 1024 / 1024).toFixed(2)} MB</p>
            </div>
          </div>
          <button onClick={() => setFile(null)} className="text-neutral-400 hover:text-neutral-600">
            <X className="w-5 h-5" />
          </button>
        </Card>
      )}
    </div>
  )
}
